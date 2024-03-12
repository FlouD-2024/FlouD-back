package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.dto.auth.RedisTokenResponseDto;
import floud.demo.dto.auth.RefreshTokenResponseDto;
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
    public RedirectView successGoogleLogin(@RequestParam("code") String code) {
        return authService.getGoogleAccessToken(code);
    }

    @GetMapping("/google/login")
    public RedirectView redirectToGoogle() {
        return authService.redirectToGoogle();
    }
    @GetMapping("/kakao/login")
    public RedirectView redirectToKakao() {
        return authService.redirectToKakao();
    }


    @GetMapping("/callback/kakao")
    public RedirectView successKakaooLogin(@RequestParam("code") String code) {
        return authService.getKakaoAccessToken(code);
    }

    @GetMapping("/redirect")
    public RedirectView redirectWithTokens(@RequestParam("access_token") String accessToken,
                                           @RequestParam("refresh_token") String refreshToken) {
        // 프론트엔드로 전달할 URL
        String redirectUrl = "http://localhost:3000/redirect";

        // 파라미터 추가
        redirectUrl += "?access_token=" + accessToken + "&refresh_token=" + refreshToken;

        // RedirectView를 사용하여 리다이렉션 URL 생성
        RedirectView redirectView = new RedirectView();
        redirectView.setUrl(redirectUrl);

        return redirectView;
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

    @GetMapping("/get-refresh-token")
    public ApiResponse<?> getRefreshToken(@RequestHeader(value="Authorization") String authorizationHeader) {
        String refreshToken = authService.getRefreshToken(authorizationHeader);
        RedisTokenResponseDto getRefreshToken = RedisTokenResponseDto.builder()
                .refresh_token(refreshToken)
                .build();

        if (refreshToken != null) {
            return ApiResponse.success(Success.GET_REFRESH_TOKEN_SUCCESS, getRefreshToken);
        } else {
            return ApiResponse.failure(Error.REFRESH_TOKEN_NOT_FOUND);
        }
    }
}
