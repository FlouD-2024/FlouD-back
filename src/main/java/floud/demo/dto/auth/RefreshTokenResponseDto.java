package floud.demo.dto.auth;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class RefreshTokenResponseDto {

    private String id_token;
    private String refresh_token;

    @Builder
    public RefreshTokenResponseDto(String id_token, String refresh_token) {
        this.id_token = id_token;
        this.refresh_token = refresh_token;
    }

}
