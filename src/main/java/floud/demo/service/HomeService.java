package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.domain.Alarm;
import floud.demo.domain.Goal;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.dto.home.HomeResponseDto;
import floud.demo.dto.home.dto.MyAlarm;
import floud.demo.dto.home.dto.MyGoal;
import floud.demo.repository.AlarmRepository;
import floud.demo.repository.GoalRepository;
import floud.demo.repository.MemoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final AuthService authService;
    private final MemoirRepository memoirRepository;
    private final GoalRepository goalRepository;
    private final AlarmRepository alarmRepository;

    public ApiResponse<?> getHome(String authorizationHeader, LocalDate date) {
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //어제 회고 조회
        Boolean isYesterdayMemoir = Boolean.FALSE;
        String yesterday_try = "";
        Optional<Memoir> yesterdayMemoir = memoirRepository.findByCreatedAt(users.getId(), LocalDate.now().minusDays(1));
        if(yesterdayMemoir.isPresent()){
            isYesterdayMemoir = Boolean.TRUE;
            yesterday_try = yesterdayMemoir.get().getTry_memoir();
        }
        //디데이 조회
        List<MyGoal> goalList = setMyGoalList(users.getId());

        //알람 조회
        List<Alarm> alarmList = alarmRepository.find2ByUser(users.getId());
        List<MyAlarm> myAlarmList = setAlarmList(alarmList);

        //한달치 회고 조회
        List<LocalDate> dateList = memoirRepository.findCreatedAtByDate(users, date, date.withDayOfMonth(date.lengthOfMonth()));
        Boolean isTodayMemoir = dateList.contains(LocalDate.now());

        HomeResponseDto responseDto = HomeResponseDto.builder()
                .nickname(users.getNickname())
                .isYesterdayMemoir(isYesterdayMemoir)
                .isTodayMemoir(isTodayMemoir)
                .goalList(goalList)
                .memo(users.getMemo())
                .yesterday_try(yesterday_try)
                .dateList(dateList)
                .alarmList(myAlarmList)
                .build();
        return ApiResponse.success(Success.GET_HOME_SUCCESS, responseDto);
    }

    private List<MyGoal> setMyGoalList(Long users_id){
        List<Goal> goalList = goalRepository.findAllByUserId(users_id);
        return goalList.stream()
                .map(goal -> MyGoal.builder()
                        .goal(goal.getContent())
                        .deadline(goal.getDeadline())
                        .build()).toList();

    }
    private List<MyAlarm> setAlarmList(List<Alarm> alarmList){
        return  alarmList.stream()
                .map(alarm -> MyAlarm.builder()
                        .nickname(alarm.getNickname())
                        .alarmType(alarm.getAlarmType())
                        .message(alarm.getMessage())
                        .created_at(alarm.getCreated_at())
                        .build())
                .toList();
    }
}
