package floud.demo.dto.mypage.dto;

import floud.demo.domain.enums.PostType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyPost {
    private Long community_id;
    private String title;
    private String content;
    private PostType postType;
    private LocalDateTime written_at;
}
