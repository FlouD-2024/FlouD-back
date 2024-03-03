package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Goal;
import floud.demo.domain.Users;
import floud.demo.dto.mypage.MyGoal;
import floud.demo.dto.mypage.MypageResponseDto;
import floud.demo.repository.GoalRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
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

        List<Goal> goals = goalRepository.findAllByUserId(users.getId());
        List<MyGoal> goalList = goals.stream()
                .map(goal -> MyGoal.builder()
                        .goal_id(goal.getId())
                        .content(goal.getContent())
                        .deadline(goal.getDeadLine())
                        .build())
                .collect(Collectors.toList());


        MypageResponseDto responseDto = MypageResponseDto.builder()
                .nickname(users.getNickname())
                .introduction(users.getIntroduction())
                .goalList(goalList)
                .build();


        return ApiResponse.success(Success.GET_MYPAGE_SUCCESS, responseDto);
    }
}
