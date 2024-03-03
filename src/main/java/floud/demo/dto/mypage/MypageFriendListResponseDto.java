package floud.demo.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageFriendListResponseDto {
    private List<MyWaiting> waitingList;
    private List<MyFriend> myFriendList;
}
