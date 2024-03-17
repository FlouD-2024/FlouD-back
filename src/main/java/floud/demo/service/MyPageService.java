package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.*;
import floud.demo.domain.enums.AlarmType;
import floud.demo.dto.PageInfo;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import floud.demo.dto.mypage.*;
import floud.demo.dto.mypage.dto.*;
import floud.demo.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MyPageService {
    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final GoalRepository goalRepository;
    private final FriendshipRepository friendshipRepository;
    private final CommunityRepository communityRepository;


    /**
     * 내 정보
     **/
    @Transactional
    public ApiResponse<?> getMyPage(String authorizationHeader){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //set GoalList
        List<MyGoal> goalList = setGoalList(users.getId());

        return ApiResponse.success(Success.GET_MYPAGE_SUCCESS, MypageResponseDto.builder()
                .nickname(users.getNickname())
                .introduction(users.getIntroduction())
                .goalList(goalList)
                .build());
    }

    @Transactional
    public ApiResponse<?> checkDuplicatedName(String authorizationHeader, String nickname){
        return ApiResponse.success(Success.CHECK_NICKNAME_DUPLICATED, Map.of("isDuplicated", checkNicknameDuplicated(nickname)));
    }

    @Transactional
    public ApiResponse<?> updateMyPage(String authorizationHeader, MypageUpdateRequestDto requestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Check Nickname Duplicated
        if(checkNicknameDuplicated(requestDto.getNickname()))
                return ApiResponse.failure(Error.NICKNAME_ALREADY_EXIST);

        //Update User info
        users.updateUserInfo(requestDto);
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
     * 내가 쓴 글
     **/
    @Transactional
    public ApiResponse<?> getMyCommunity(String authorizationHeader, Pageable pageable){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Get my post List
        Page<Community> communityList = communityRepository.findAllByUser(pageable, users.getId());
        List<MyPost> postList = setMyPosts(communityList);
        PageInfo pageInfo = setPageInfo(communityList);

        return ApiResponse.success(Success.GET_MYPAGE_COMMUNITY_SUCCESS, CommunityResponseDto.builder()
                .nickname(users.getNickname())
                .postList(postList)
                .pageInfo(pageInfo)
                .build());
    }


    /**
     * 친구 관리
     **/
    @Transactional
    public ApiResponse<?> getFriendList(String authorizationHeader, Pageable pageable){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Find Friend List
        MypageFriendListResponseDto responseDto = findFriends(pageable, users);

        return ApiResponse.success(Success.GET_FRIEND_LIST_SUCCESS,responseDto);
    }





    private List<MyGoal> setGoalList(Long users_id){
        List<Goal> goals = goalRepository.findAllByUserId(users_id);
        return goals.stream()
                .map(goal -> MyGoal.builder()
                        .goal_id(goal.getId())
                        .content(goal.getContent())
                        .deadline(goal.getDeadline())
                        .build())
                .collect(Collectors.toList());
    }

    private void updateGoal(Users users, List<UpdateGoal> requestDtoGoalList){
        List<Goal> goals = goalRepository.findAllByUserId(users.getId());

        // Delete all existing goals
        goalRepository.deleteAll(goals);

        // Add new goals
        for (UpdateGoal requestGoal: requestDtoGoalList) {
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

    private boolean checkNicknameDuplicated(String nickname){
        return usersRepository.existsByNickname(nickname);
    }


    private List<MyPost> setMyPosts(Page<Community> communityList){
        return communityList.stream()
                .map(community -> MyPost.builder()
                        .community_id(community.getId())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .postType(community.getPostType())
                        .written_at(community.getUpdated_at())
                        .build()).toList();
    }

    private MypageFriendListResponseDto findFriends(Pageable pageable, Users me){
        List<Friendship> waitingFriends = friendshipRepository.findAllByWaitingToUser(me.getId());
        Page<Friendship> acceptedFriends = friendshipRepository.findAllByUsersIdAndPage(pageable, me.getId());

        List<MyWaiting> myWaitingList = waitingFriends.stream()
                .map(friendship -> buildMyWaiting(friendship, me))
                .collect(Collectors.toList());

        List<MyFriend> myFriendList = acceptedFriends.stream()
                .map(friendship -> buildMyFriend(friendship, me))
                .collect(Collectors.toList());

        return MypageFriendListResponseDto.builder()
                .waitingList(myWaitingList)
                .myFriendList(myFriendList)
                .pageInfo(setFriendPageInfo(acceptedFriends))
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

    private PageInfo setPageInfo(Page<Community> postPage){
        return PageInfo.builder()
                .last(!postPage.hasNext())
                .previous(postPage.hasPrevious())
                .nowPage(postPage.getNumber())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();
    }

    private PageInfo setFriendPageInfo(Page<Friendship> friendshipPage){
        return PageInfo.builder()
                .last(!friendshipPage.hasNext())
                .previous(friendshipPage.hasPrevious())
                .nowPage(friendshipPage.getNumber())
                .totalPages(friendshipPage.getTotalPages())
                .totalElements(friendshipPage.getTotalElements())
                .build();
    }

}
