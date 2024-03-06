package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.dto.auth.RedisTokenResponseDto;
import floud.demo.service.RedisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RedisController {

    @Autowired
    private RedisService redisService;

    @GetMapping("/get-refresh-token")
    public ApiResponse<?> getRefreshToken(@RequestParam Long userId) {
        String refreshToken = redisService.getRefreshToken(userId);
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
