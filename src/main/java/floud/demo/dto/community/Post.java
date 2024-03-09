package floud.demo.dto.community;

import floud.demo.domain.enums.PostType;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class Post {
    private Long community_id;
    private String title;
    private String content;
    private PostType postType;
}
