package floud.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class SocialLoginDecodeResponseDto {

    @JsonProperty("sub")
    private String social_id;
    private String email;

    @Builder
    public SocialLoginDecodeResponseDto(String social_id, String email) {
        this.social_id = social_id;
        this.email = email;
    }
}
