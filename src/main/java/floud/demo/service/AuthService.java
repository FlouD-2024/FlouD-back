package floud.demo.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final String GOOGLE_TOKEN_URL = "https://oauth2.googleapis.com/token";

    @Value("{oauth2.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("{oauth2.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;

    @Value("{oauth2.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;


    public ResponseEntity<String> getGoogleAccessToken(String code) {

        System.out.println("GOOGLE_REDIRECT_URI = " + GOOGLE_REDIRECT_URI);
        RestTemplate restTemplate = new RestTemplate();
        Map<String, String> params = new HashMap<>();

        params.put("code", code);
        params.put("client_id", GOOGLE_CLIENT_ID);
        params.put("client_secret", GOOGLE_CLIENT_SECRET);
        params.put("redirect_uri", GOOGLE_REDIRECT_URI);
        params.put("grant_type", "authorization_code");

        ResponseEntity<String> responseEntity = restTemplate.postForEntity(GOOGLE_TOKEN_URL, params, String.class);

        if (responseEntity.getStatusCode() == HttpStatus.OK) {
            return responseEntity;
        }
        return null;
    }
}
