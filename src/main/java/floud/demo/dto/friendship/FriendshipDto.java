package floud.demo.dto.friendship;

import lombok.Builder;
import lombok.Getter;


@Getter
@Builder
public class FriendshipDto {
    private String friend_nickname;
    private Boolean memoir_status;
    private Long memoir_id;
}
