package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Friendship;
import floud.demo.domain.Goal;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.domain.enums.FriendshipStatus;
import floud.demo.dto.friendship.FriendshipDto;
import floud.demo.dto.mypage.*;
import floud.demo.repository.FriendshipRepository;
import floud.demo.repository.GoalRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
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
    private final FriendshipRepository friendshipRepository;


    /**
     * 내 정보
     **/
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
        boolean isDuplicated = usersRepository.existsByNickname(nickname);
        return ApiResponse.success(Success.CHECK_NICKNAME_DUPLICATED, Map.of("isDuplicated", isDuplicated));
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

    /**
     * 친구 관리
     **/
    @Transactional
    public ApiResponse<?> getFriendList(){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //Find Friend List
        MypageFriendListResponseDto responseDto = findFriends(users);

        return ApiResponse.success(Success.GET_FRIEND_LIST_SUCCESS,responseDto);
    }

    @Transactional
    public ApiResponse<?> updateFriend(MypageFriendUpdateRequestDto requestDto){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //Find Friendship
        Optional<Friendship> optionalFriendship = friendshipRepository.findById(requestDto.getFriendship_id());
        if(optionalFriendship.isEmpty())
            return ApiResponse.failure(Error.FRIENDSHIP_NOT_FOUND);
        Friendship friendship = optionalFriendship.get();

        //Check whether request nickname and friendship's from user nickname is matched
        if(!friendship.getFrom_user().getNickname().equals(requestDto.getNickname()))
            return ApiResponse.failure(Error.NOT_MATCHED_NICKNAME);

        //Update Friendship
        friendship.updateStatus(requestDto.getFriendshipStatus());

        return ApiResponse.success(Success.UPDATE_FRIEND_SUCCESS, Map.of("nowStatus", friendship.getFriendshipStatus()));
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

    public MypageFriendListResponseDto findFriends(Users me){
        List<Friendship> waitingFriends = friendshipRepository.findAllByWaitingToUser(me.getId());
        List<Friendship> acceptedFriends = friendshipRepository.findAllByUsersId(me.getId());

        List<MyWaiting> myWaitingList = waitingFriends.stream()
                .map(friendship -> buildMyWaiting(friendship, me))
                .collect(Collectors.toList());

        List<MyFriend> myFriendList = acceptedFriends.stream()
                .map(friendship -> buildMyFriend(friendship, me))
                .collect(Collectors.toList());

        return MypageFriendListResponseDto.builder()
                .waitingList(myWaitingList)
                .myFriendList(myFriendList)
                .build();
    }

    private MyWaiting buildMyWaiting(Friendship friendship, Users me) {
        Users friend = getFriend(friendship, me);
        return MyWaiting.builder()
                .friendship_id(friendship.getId())
                .nickname(friend.getNickname())
                .friendshipStatus(friendship.getFriendshipStatus())
                .introduction(friend.getIntroduction())
                .build();
    }

    private MyFriend buildMyFriend(Friendship friendship, Users me) {
        Users friend = getFriend(friendship, me);
        return MyFriend.builder()
                .friendship_id(friendship.getId())
                .nickname(friend.getNickname())
                .friendshipStatus(friendship.getFriendshipStatus())
                .introduction(friend.getIntroduction())
                .build();
    }


    private Users getFriend(Friendship friendship, Users me) {
        if (friendship.getTo_user().equals(me)) {
            return friendship.getFrom_user();
        } else {
            return friendship.getTo_user();
        }
    }
}
