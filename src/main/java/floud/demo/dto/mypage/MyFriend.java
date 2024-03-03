package floud.demo.dto.mypage;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class MyFriend {
    private String nickname;
    private FriendshipStatus friendshipStatus;
    private String introduction;
}
