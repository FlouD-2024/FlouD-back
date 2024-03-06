package floud.demo.dto.friendship;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class FindFriendResponseDto {
    private String nickname;
    private String email;
    private String introduction;
}
