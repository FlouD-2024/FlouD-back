package floud.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.dto.auth.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

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

                String accessToken = tokenResponseDto.getAccess_token();
                String refreshToken = tokenResponseDto.getRefresh_token();

                return ApiResponse.success(Success.GET_GOOGLE_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
                        .access_token(accessToken)
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

                String accessToken = tokenResponseDto.getAccess_token();
                String refreshToken = tokenResponseDto.getRefresh_token();

                return ApiResponse.success(Success.GET_KAKAO_ACCESS_TOKEN_SUCCESS, TokenResponseDto.builder()
                        .access_token(accessToken)
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
}
