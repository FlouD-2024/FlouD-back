package floud.demo.dto.home.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class MyGoal {
    private String goal;
    private LocalDate deadline;
}
