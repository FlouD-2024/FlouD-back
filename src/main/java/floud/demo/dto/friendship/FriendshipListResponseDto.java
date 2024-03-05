package floud.demo.dto.friendship;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FriendshipListResponseDto {
    private String my_nickname;
    private Long totalFriendNum;
    private Long totalPage;
    private List<FriendshipDto> friendshipList;
}
