package floud.demo.dto.community;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PostResponseDto {
    private Long community_id;
    private String nickname;
    private String title;
    private String content;
    private LocalDateTime written_at;
}
