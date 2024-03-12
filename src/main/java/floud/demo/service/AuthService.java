package floud.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import floud.demo.common.exception.ApiException;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Users;
import floud.demo.dto.auth.RefreshTokenResponseDto;
import floud.demo.dto.auth.SocialLoginDecodeResponseDto;
import floud.demo.dto.auth.TokenResponseDto;
import floud.demo.dto.auth.UsersResponseDto;
import floud.demo.repository.UsersRepository;
import io.jsonwebtoken.io.Decoders;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.RedisConnectionFailureException;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Autowired
    private final UsersRepository usersRepository;

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";
    private final String KAKAO_TOKEN_URL = "https://kauth.kakao.com/oauth/token";


    // redis
    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    private final String REDIS_KEY_PREFIX = "refresh_token:";


    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${oauth2.google.scope}")
    private String googleScope;

    @Value("${oauth2.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value("${oauth2.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;
    @Value("${oauth2.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;
    @Value("${oauth2.kakao.scope}")
    private String kakaoScope;


    private String GOOGLE_BASE_URI = "https://accounts.google.com/o/oauth2/v2/auth";
    private String KAKAO_BASE_URI = "https://kauth.kakao.com/oauth/authorize";

    public RedirectView redirectToGoogle() {
        String url = GOOGLE_BASE_URI + "?client_id=" + GOOGLE_CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&scope=" + googleScope +
                "&access_type=offline";
        return new RedirectView(url);
    }

    /**
     * 구글 로그인시 id_token 발급하는 메소드
     */

    public RedirectView getGoogleAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponseDto tokenResponseDto = objectMapper.readValue(responseEntity.getBody(), TokenResponseDto.class);

                String id_token = tokenResponseDto.getId_token();
                String refreshToken = tokenResponseDto.getRefresh_token();


                UsersResponseDto userInfo = getUserInfo(id_token);
                System.out.println("userInfo = " + userInfo.getUsers_id());

                // 정책에 따라 refresh token이 발급되는 경우에면 저장함
                saveRefreshToken(userInfo.getUsers_id(),refreshToken);

                String redirectUrl = "http://localhost:3000/redirect";
                redirectUrl += "?access_token=" + id_token + "&refresh_token=" + refreshToken;

                // RedirectView를 사용하여 리다이렉션 수행
                RedirectView redirectView = new RedirectView();
                redirectView.setUrl(redirectUrl);

                return redirectView;

//                return ApiResponse.success(Success.GET_GOOGLE_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
//                        .id_token(id_token)
//                        .refresh_token(refreshToken)
//                        .build());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    /**
     * 백엔드 테스트를 위한 redirect view api
     */
    public RedirectView redirectToKakao() {
        String url = KAKAO_BASE_URI + "?client_id=" + KAKAO_CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + KAKAO_REDIRECT_URI +
                "&scope=" + kakaoScope;
        return new RedirectView(url);
    }

    /**
     * 카카오 로그인시 id_token 발급하는 메소드
     */

    public RedirectView getKakaoAccessToken(String code) {
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params= new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<String> responseEntity = restTemplate.postForEntity(KAKAO_TOKEN_URL, request, String.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponseDto tokenResponseDto = objectMapper.readValue(responseEntity.getBody(), TokenResponseDto.class);

                String id_token = tokenResponseDto.getId_token();
                String refreshToken = tokenResponseDto.getRefresh_token();

                UsersResponseDto userInfo = getUserInfo(id_token);
                saveRefreshToken(userInfo.getUsers_id(),refreshToken);

                String redirectUrl = "http://localhost:3000/redirect";
                redirectUrl += "?access_token=" + id_token + "&refresh_token=" + refreshToken;

                // RedirectView를 사용하여 리다이렉션 수행
                RedirectView redirectView = new RedirectView();
                redirectView.setUrl(redirectUrl);

                return redirectView;

//                return ApiResponse.success(Success.GET_KAKAO_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
//                        .id_token(id_token)
//                        .refresh_token(refreshToken)
//                        .build());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException();
        }
    }

    public SocialLoginDecodeResponseDto getUserSocialInfo(String authorizationHeader) {

        String token = authorizationHeader.substring("Bearer ".length());
        SocialLoginDecodeResponseDto socialUserinfo = decodeToken(token); // id_token으로 유저 정보를 가져옴 -> 1. social_id 2. email
        if (socialUserinfo == null) return null;

        return socialUserinfo.builder()
                .email(socialUserinfo.getEmail())
                .social_id(socialUserinfo.getSocial_id())
                .build();
    }

    public Users findUserByToken(String authorizationHeader) {

        String token = authorizationHeader.substring("Bearer ".length());
        SocialLoginDecodeResponseDto userinfo = decodeToken(token);
        Users getUser = findUserBySocial_id(userinfo.getSocial_id());

        return getUser;
    }

    public UsersResponseDto getUserInfo(String authorizationHeader) {

        String token = authorizationHeader.substring("Bearer ".length());
        SocialLoginDecodeResponseDto userinfo = decodeToken(token); // 1. 토큰 통해 social ID 가져옴
        Users getUser = findUserBySocial_id(userinfo.getSocial_id()); // 2. 유저가 없으면 DB에 없다는 것임
        Users newUser = new Users();

        // 유저가 없으면 저장함
        if (getUser == null) {
            newUser.setEmail(userinfo.getEmail());
            newUser.setSocial_id(userinfo.getSocial_id());
            newUser.setNickname(generateUniqueNickname());
            usersRepository.save(newUser);
            return UsersResponseDto.builder()
                    .users_id(newUser.getId())
                    .nickname(newUser.getNickname())
                    .email(newUser.getEmail())
                    .social_id(newUser.getSocial_id())
                    .build();
        }
        // 유저가 있으면 해당 유저를 반환함
        return UsersResponseDto.builder()
                .users_id(getUser.getId())
                .nickname(getUser.getNickname())
                .email(getUser.getEmail())
                .social_id(getUser.getSocial_id())
                .nickname(getUser.getNickname())
                .build();
    }


    /**
     * 해당 메소드는 토큰에서 유저 정보를 가져오는 것이다.
     */
    public SocialLoginDecodeResponseDto decodeToken(String token) {
        String decode = decryptBase64UrlToken(token.split("\\.")[1]);
        System.out.println("decode = " + decode);
        return transJsonToMemberInfoDto(decode);
    }

    public String decryptBase64UrlToken(String jwtToken) {
        byte[] decode = Decoders.BASE64URL.decode(jwtToken);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public SocialLoginDecodeResponseDto transJsonToMemberInfoDto(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            SocialLoginDecodeResponseDto dto = mapper.readValue(json, SocialLoginDecodeResponseDto.class);
            return dto;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }


    public Users findUserBySocial_id(String social_id) {
        return usersRepository.findBySocial_id(social_id).orElse(null);
    }

    private List<String> loadWordsFromFile() throws IOException {
        ClassPathResource resource = new ClassPathResource("random_nickname.txt");
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(resource.getInputStream()))) {
            return reader.lines().collect(Collectors.toList());
        }
    }
    private String generateUniqueNickname() {
        try {
            List<String> words = loadWordsFromFile();
            String selectedWord = words.get(new Random().nextInt(words.size()));
            int randomSuffix = (int) (Math.random() * 1000);
            return selectedWord + randomSuffix;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public RefreshTokenResponseDto reissueKakaoByRefresh(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("client_secret", KAKAO_CLIENT_SECRET);

        // 헤더와 파라미터를 합쳐서 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Map<String, String>> responseEntity = new RestTemplate().exchange(
                KAKAO_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        return RefreshTokenResponseDto.builder()
                .id_token(responseEntity.getBody().get("id_token"))
                .refresh_token(responseEntity.getBody().get("refresh_token"))
                .build();
    }


    public RefreshTokenResponseDto reissueGoogleByRefresh(String refreshToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();

        params.add("refresh_token", refreshToken);
        params.add("grant_type", "refresh_token");
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);

        // 헤더와 파라미터를 합쳐서 요청 엔터티 생성
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(params, headers);

        ResponseEntity<Map<String, String>> responseEntity = new RestTemplate().exchange(
                GOOGLE_TOKEN_URL,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, String>>() {}
        );

        return RefreshTokenResponseDto.builder()
                .id_token(responseEntity.getBody().get("id_token"))
                .refresh_token(responseEntity.getBody().get("refresh_token"))
                .build();
    }

    /**
     * redis refresh token 저장
     */
    public void saveRefreshToken(Long userId, String refreshToken) {
        System.out.println("userId = " + userId);
        String key = REDIS_KEY_PREFIX + userId;
        redisTemplate.opsForValue().set(key, refreshToken);
    }

    /**
     * redis refresh token 가져오기
     */
    public String getRefreshToken(String authorizationHeader) {
        String token = authorizationHeader.substring("Bearer ".length());
        SocialLoginDecodeResponseDto userinfo = decodeToken(token); // 1. 토큰 통해 social ID 가져옴
        Users getUser = findUserBySocial_id(userinfo.getSocial_id()); // 2. 유저가 없으면 DB에 없다는 것임
        String key = REDIS_KEY_PREFIX + getUser.getId();
        System.out.println("key = " + redisTemplate.opsForValue().get(key));
        return redisTemplate.opsForValue().get(key);
    }
}
