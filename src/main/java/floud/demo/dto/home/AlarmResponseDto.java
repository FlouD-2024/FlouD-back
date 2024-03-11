package floud.demo.dto.home;

import floud.demo.dto.home.dto.MyAlarm;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class AlarmResponseDto {
    private List<MyAlarm> alarmList;
}
