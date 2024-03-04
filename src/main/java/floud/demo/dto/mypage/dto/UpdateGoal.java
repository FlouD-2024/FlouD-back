package floud.demo.dto.mypage.dto;

import floud.demo.domain.Goal;
import floud.demo.domain.Users;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Builder
@Getter
public class UpdateGoal {
    private String content;
    private LocalDate deadline;

    public Goal toEntity(Users users){
        return Goal.builder()
                .content(content)
                .deadline(deadline)
                .users(users)
                .build();
    }
}
