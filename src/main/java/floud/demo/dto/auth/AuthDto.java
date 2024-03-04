package floud.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class AuthDto {

    private String accessToken;
    private String refreshToken;
}
