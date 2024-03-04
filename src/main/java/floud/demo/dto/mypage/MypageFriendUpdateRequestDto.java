package floud.demo.dto.mypage;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MypageFriendUpdateRequestDto {
    private Long friendship_id;
    private String nickname;
    private FriendshipStatus friendshipStatus;
}
