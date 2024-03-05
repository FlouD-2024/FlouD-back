package floud.demo.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.domain.Users;
import floud.demo.dto.auth.TokenResponseDto;
import floud.demo.dto.auth.UsersResponseDto;
import floud.demo.repository.UsersRepository;
import io.jsonwebtoken.io.Decoders;
import jakarta.annotation.Resource;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
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
                "&scope=" + googleScope;
        return new RedirectView(url);
    }


    public ApiResponse<?> getGoogleAccessToken(String code) {
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

                String idToken = tokenResponseDto.getId_token();
                String refreshToken = tokenResponseDto.getRefresh_token();

                return ApiResponse.success(Success.GET_GOOGLE_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
                        .id_token(idToken)
                        .refresh_token(refreshToken)
                        .build());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failure(e);
        }
    }

    public RedirectView redirectToKakao() {
        String url = KAKAO_BASE_URI + "?client_id=" + KAKAO_CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + KAKAO_REDIRECT_URI +
                "&scope=" + kakaoScope;
        System.out.println("url = " + url);
        return new RedirectView(url);
    }

    public ApiResponse<?> getKakaoAccessToken(String code) {
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
            System.out.println("responseEntity = " + responseEntity);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                ObjectMapper objectMapper = new ObjectMapper();
                TokenResponseDto tokenResponseDto = objectMapper.readValue(responseEntity.getBody(), TokenResponseDto.class);

                String id_token = tokenResponseDto.getId_token();
                String refreshToken = tokenResponseDto.getRefresh_token();

                return ApiResponse.success(Success.GET_KAKAO_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
                        .id_token(id_token)
                        .refresh_token(refreshToken)
                        .build());
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ApiResponse.failure(e);
        }
    }

    public ApiResponse<?> getUserInfoByToken(String authorizationHeader) {

        System.out.println("authorizationHeader = " + authorizationHeader);

        String token = authorizationHeader.substring("Bearer ".length());
        UsersResponseDto userinfo = decodeToken(token);
        Users getUser = findUserBySocial_id(userinfo.getSocial_id());
        System.out.println("getUser = " + getUser);
        UsersResponseDto usersInfoResponse = userinfo.builder()
                .users_id(getUser.getId())
                .email(getUser.getEmail())
                .social_id(getUser.getSocial_id())
                .nickname(getUser.getNickname())
                .build();

        return ApiResponse.success(Success.GET_USER_INFO_SUCCESS, usersInfoResponse);
    }

    public UsersResponseDto decodeToken(String token) {
        String decode = decryptBase64UrlToken(token.split("\\.")[1]);
        System.out.println("decode = " + decode);
        return transJsonToMemberInfoDto(decode);
    }

    public String decryptBase64UrlToken(String jwtToken) {
        byte[] decode = Decoders.BASE64URL.decode(jwtToken);
        return new String(decode, StandardCharsets.UTF_8);
    }

    public UsersResponseDto transJsonToMemberInfoDto(String json) {
        try {
            ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            UsersResponseDto dto = mapper.readValue(json, UsersResponseDto.class);
            System.out.println("UsersRespone로 변경= " + dto);
            saveMemberInfo(dto);
            return dto;
        } catch (JsonMappingException e) {
            throw new RuntimeException(e);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public void saveMemberInfo(UsersResponseDto dto) {
        try {
            Optional<Users> user = usersRepository.findBySocial_id(dto.getSocial_id());
            System.out.println("멤버 조회");
            System.out.println("OAuth ID: " + dto.getSocial_id());
            System.out.println("member 존재 여부: " + user.isPresent());
            processMember(user, dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void processMember(Optional<Users> users, UsersResponseDto dto) {
        users.ifPresentOrElse(
                existingUser -> {
                    System.out.println("유저가 이미 존재합니다: " + existingUser);
                },
                () -> {
                    Users newUser = dto.toEntity();
                    newUser.setNickname(generateUniqueNickname()); // 랜덤 닉네임 생성 및 저장
                    System.out.println("새 멤버 저장됨: " + newUser);
                    usersRepository.save(newUser);
                }
        );
    }
    public Users findUserBySocial_id(String social_id) {
        return usersRepository.findBySocial_id(social_id)
                .orElseThrow(() -> new EntityNotFoundException("Member not found"));
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
}
