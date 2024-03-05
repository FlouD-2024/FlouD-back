package floud.demo.dto.auth;

import com.fasterxml.jackson.annotation.JsonProperty;
import floud.demo.domain.Users;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class UsersResponseDto {

    private Long users_id;
    @JsonProperty("sub")
    private String social_id;
    private String email;
    private String nickname;

    @Builder
    public UsersResponseDto(Long users_id, String social_id, String email, String nickname) {
        this.users_id = users_id;
        this.social_id = social_id;
        this.email = email;
        this.nickname = nickname;

    }

    public Users toEntity() {
        return Users.builder()
                .id(users_id)
                .social_id(social_id)
                .email(email)
                .nickname(nickname)
                .build();
    }
}
