package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Goal;
import floud.demo.domain.Users;
import floud.demo.dto.mypage.MyGoal;
import floud.demo.dto.mypage.MypageResponseDto;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import floud.demo.dto.mypage.UpdateGoal;
import floud.demo.repository.GoalRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final UsersRepository usersRepository;
    private final GoalRepository goalRepository;
    @Transactional
    public ApiResponse<?> getMypage(){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //set GoalList
        List<MyGoal> goalList = setGoalList(users.getId());

        return ApiResponse.success(Success.GET_MYPAGE_SUCCESS, MypageResponseDto.builder()
                .nickname(users.getNickname())
                .introduction(users.getIntroduction())
                .goalList(goalList)
                .build());
    }

    @Transactional
    public ApiResponse<?> checkDuplicatedName(String nickname){

        return ApiResponse.success(Success.SUCCESS);
    }

    @Transactional
    public ApiResponse<?> updateMypage(MypageUpdateRequestDto requestDto){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //Update Introduction
        users.updateIntroduction(requestDto.getIntroduction());
        usersRepository.save(users);

        //Update Goals
        updateGoal(users, requestDto.getGoalList());

        //set GoalList
        List<MyGoal> updatedGoalList = setGoalList(users.getId());

        //MypageResonsedto 리턴하기
        return ApiResponse.success(Success.UPDATE_MYPAGE_SUCCESS, MypageResponseDto.builder()
                .nickname(users.getNickname())
                .introduction(users.getIntroduction())
                .goalList(updatedGoalList)
                .build());
    }

    public List<MyGoal> setGoalList(Long users_id){
        List<Goal> goals = goalRepository.findAllByUserId(users_id);
        return goals.stream()
                .map(goal -> MyGoal.builder()
                        .goal_id(goal.getId())
                        .content(goal.getContent())
                        .deadline(goal.getDeadline())
                        .build())
                .collect(Collectors.toList());
    }

    public void updateGoal(Users users, List<UpdateGoal> requestDtoGoalList){
        List<Goal> goals = goalRepository.findAllByUserId(users.getId());

        // Delete all existing goals
        goalRepository.deleteAll(goals);

        // Add new goals
        for (UpdateGoal requestGoal : requestDtoGoalList) {
            log.info("수정된 디데이 -> {}", requestGoal.getContent());
            log.info("수정된 데드라인 -> {}", requestGoal.getDeadline());
            UpdateGoal goal = UpdateGoal.builder()
                    .content(requestGoal.getContent())
                    .deadline(requestGoal.getDeadline())
                    .build();
            Goal newGoal = goal.toEntity(users);
            goalRepository.save(newGoal);
        }
    }

}
