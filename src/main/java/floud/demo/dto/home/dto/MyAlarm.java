package floud.demo.dto.home.dto;

import floud.demo.domain.enums.AlarmType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MyAlarm {
    private String nickname;
    private AlarmType alarmType;
    private String message;
    private LocalDateTime created_at;
}
