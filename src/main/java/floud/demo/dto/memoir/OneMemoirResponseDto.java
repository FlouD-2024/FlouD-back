package floud.demo.dto.memoir;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class OneMemoirResponseDto {
    private String nickname;
    private Long memoir_id;
    private String title;
    private String keep_memoir;
    private String problem_memoir;
    private String try_memoir;
}
