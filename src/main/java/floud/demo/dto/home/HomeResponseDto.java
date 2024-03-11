package floud.demo.dto.home;

import floud.demo.dto.home.dto.MyAlarm;
import floud.demo.dto.home.dto.MyGoal;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Builder
public class HomeResponseDto {
    private Boolean isYesterdayMemoir;
    private Boolean isTodayMemoir;
    private List<MyGoal> goalList;
    private String memo;
    private String yesterday_try;
    private List<LocalDate> dateList;
    private List<MyAlarm> alarmList;
}
