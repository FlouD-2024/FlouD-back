package floud.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RedisTokenResponseDto {
    private String refresh_token;

    @Builder
    public RedisTokenResponseDto(String refresh_token) {
        this.refresh_token = refresh_token;
    }
}
