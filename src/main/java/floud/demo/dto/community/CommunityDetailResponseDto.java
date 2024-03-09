package floud.demo.dto.community;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class CommunityDetailResponseDto {
    private String my_nickname;
    private Long community_id;
    private String writer_nickname;
    private boolean isMine;
    private String title;
    private String content;
    private LocalDateTime created_at;
    private LocalDateTime updated_at;
}
