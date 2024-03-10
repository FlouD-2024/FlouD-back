package floud.demo.dto.community;

import floud.demo.domain.enums.PostType;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Page;

@Getter
@Builder
public class CommunityResponseDto {
    private String nickname;
    private PostType postType;
    private Page<Post> postList;
}
