package floud.demo.dto.mypage;

import floud.demo.dto.PageInfo;
import floud.demo.dto.mypage.dto.MyFriend;
import floud.demo.dto.mypage.dto.MyWaiting;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageFriendListResponseDto {
    private List<MyWaiting> waitingList;
    private List<MyFriend> myFriendList;
    private PageInfo pageInfo;
}
