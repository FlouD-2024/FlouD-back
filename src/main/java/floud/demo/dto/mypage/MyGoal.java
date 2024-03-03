package floud.demo.dto.mypage;


import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class MyGoal {
    private Long goal_id;
    private String content;
    private LocalDateTime deadline;
}
