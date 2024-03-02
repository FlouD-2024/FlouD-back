package floud.demo.dto.memoir;

import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class MemoirCreateRequestDto {
    private String title;
    private String keep_memoir;
    private String problem_memoir;
    private String try_memoir;

    public Memoir toEntity(){
        return Memoir.builder()
                .title(title)
                .keep_memoir(keep_memoir)
                .problem_memoir(problem_memoir)
                .try_memoir(try_memoir)
                .build();
    }
}
