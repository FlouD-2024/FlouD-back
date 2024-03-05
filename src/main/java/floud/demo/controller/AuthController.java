package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

@RestController
@AllArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @GetMapping("/callback/google")
    public ApiResponse<?> successGoogleLogin(@RequestParam("code") String code) {
        return authService.getGoogleAccessToken(code);
    }

    @GetMapping("/google/login")
    public RedirectView redirectToGoogle() {
        return authService.redirectToGoogle();
    }

    @GetMapping("/callback/kakao")
    public ApiResponse<?> successKakaooLogin(@RequestParam("code") String code) {
        return authService.getKakaoAccessToken(code);
    }

    @GetMapping("/kakao/login")
    public RedirectView redirectToKakao() {
        return authService.redirectToKakao();
    }

    @GetMapping("/decode")
    public ApiResponse<?> getUserInfoByToken(@RequestHeader(value="Authorization") String authorizationHeader) {
        return authService.getUserInfoByToken(authorizationHeader);
    }
}
