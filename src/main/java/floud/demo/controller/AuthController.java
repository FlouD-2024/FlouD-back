package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.dto.auth.SocialLoginDecodeResponseDto;
import floud.demo.dto.auth.UsersResponseDto;
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
        UsersResponseDto getUser = authService.getUserInfo(authorizationHeader);
        return ApiResponse.success(Success.GET_USER_INFO_SUCCESS, getUser);
    }

    @PostMapping("/kakao/refresh")
    public ApiResponse<?> getKakaoByRefreshToken(@RequestParam("refresh_token") String refreshToken) {
        RefreshTokenResponseDto result = authService.reissueKakaoByRefresh(refreshToken);
        return ApiResponse.success(Success.GET_REISSUE_ACCESS_TOKEN_SUCCESS,result);
    }

    @PostMapping("/google/refresh")
    public ApiResponse<?> getGoogleByRefreshToken(@RequestParam("refresh_token") String refreshToken) {
        RefreshTokenResponseDto result = authService.reissueGoogleByRefresh(refreshToken);
        return ApiResponse.success(Success.GET_REISSUE_ACCESS_TOKEN_SUCCESS,result);
    }
}
