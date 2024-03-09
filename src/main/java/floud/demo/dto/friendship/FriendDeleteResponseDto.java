package floud.demo.dto.friendship;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class FriendDeleteResponseDto {
    private Long friendship_id;
    private String to_user;
    private String from_user;
    private FriendshipStatus friendshipStatus;
}
