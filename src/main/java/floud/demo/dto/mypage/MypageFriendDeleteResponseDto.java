package floud.demo.dto.mypage;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageFriendDeleteResponseDto {
    private Long friendship_id;
    private String to_user;
    private String from_user;
    private FriendshipStatus friendshipStatus;
}
