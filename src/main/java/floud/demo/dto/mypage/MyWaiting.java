package floud.demo.dto.mypage;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyWaiting {
    private String nickname;
    private FriendshipStatus friendshipStatus;
    private String introduction;
}
