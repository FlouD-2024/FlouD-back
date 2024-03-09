package floud.demo.dto.friendship;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendUpdateRequestDto {
    private Long friendship_id;
    private String nickname;
    private FriendshipStatus friendshipStatus;
}
