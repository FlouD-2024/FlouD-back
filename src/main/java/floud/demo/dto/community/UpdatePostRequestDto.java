package floud.demo.dto.community;

import floud.demo.domain.enums.PostType;
import lombok.Getter;

@Getter
public class UpdatePostRequestDto {
    private Long community_id;
    private String title;
    private String content;
    private PostType postType;
}
