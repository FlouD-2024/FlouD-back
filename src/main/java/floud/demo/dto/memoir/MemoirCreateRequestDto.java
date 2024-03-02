package floud.demo.dto.memoir;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class MemoirCreateRequestDto {
    private String keep_memoir;
    private String problem_memoir;
    private String try_memoir;
}
