package floud.demo.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.dto.auth.TokenResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Value("${oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;
    @Value("${oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    @Value("${oauth2.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;
    @Value("${oauth2.google.scope}")
    private String scope;

    private String baseUrl = "https://accounts.google.com/o/oauth2/v2/auth";

    public RedirectView redirectToGoogle() {
        String url = baseUrl + "?client_id=" + GOOGLE_CLIENT_ID +
                "&response_type=code" +
                "&redirect_uri=" + GOOGLE_REDIRECT_URI +
                "&scope=" + scope;
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

}
