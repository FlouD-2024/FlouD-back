package floud.demo.dto.mypage;


import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class MyGoal {
    private Long goal_id;
    private String content;
    private LocalDateTime deadline;
}
