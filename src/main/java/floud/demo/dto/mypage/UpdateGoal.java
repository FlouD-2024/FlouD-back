package floud.demo.dto.mypage;

import floud.demo.domain.Goal;
import floud.demo.domain.Users;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateGoal {
    private String content;
    private LocalDateTime deadline;

    public Goal toEntity(Users users){
        return Goal.builder()
                .content(content)
                .deadline(deadline)
                .users(users)
                .build();
    }
}
