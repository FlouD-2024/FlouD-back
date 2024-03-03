package floud.demo.dto.mypage.dto;

import floud.demo.domain.enums.FriendshipStatus;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class MyWaiting {
    private Long friendship_id;
    private String nickname;
    private FriendshipStatus friendshipStatus;
    private String introduction;
}
