package floud.demo.dto.community;

import floud.demo.domain.enums.PostType;
import floud.demo.dto.PageInfo;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommunityResponseDto {
    private String nickname;
    private PostType postType;
    private List<Post> postList;
    private PageInfo pageInfo;
}
