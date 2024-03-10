package floud.demo.dto.friendship;

import floud.demo.dto.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class FriendshipListResponseDto {
    private String my_nickname;
    private List<FriendshipDto> friendshipList;
    private PageInfo pageInfo;
}
