package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.*;
import floud.demo.domain.enums.AlarmType;
import floud.demo.domain.enums.FriendshipStatus;
import floud.demo.dto.PageInfo;
import floud.demo.dto.friendship.FindFriendResponseDto;
import floud.demo.dto.friendship.FriendshipCreateRequestDto;
import floud.demo.dto.friendship.FriendshipDto;
import floud.demo.dto.friendship.FriendshipListResponseDto;
import floud.demo.dto.memoir.OneMemoirResponseDto;
import floud.demo.dto.friendship.FriendDeleteResponseDto;
import floud.demo.dto.friendship.FriendUpdateRequestDto;
import floud.demo.repository.AlarmRepository;
import floud.demo.repository.FriendshipRepository;
import floud.demo.repository.MemoirRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendshipService {

    private final AuthService authService;
    private final UsersRepository usersRepository;
    private final MemoirRepository memoirRepository;
    private final FriendshipRepository friendshipRepository;
    private final AlarmRepository alarmRepository;


    @Transactional
    public ApiResponse<?> findFriend(String authorizationHeader, String nickname){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        if(nickname.equals(users.getNickname()))
            return ApiResponse.failure(Error.NOT_BE_FRIEND_MYSELF);

        Optional<Users> optionalFriend = usersRepository.findByNickname(nickname);
        if(optionalFriend.isEmpty())
            return ApiResponse.failure(Error.FRIEND_NICKNAME_NOT_FOUND);
        Users friend = optionalFriend.get();

        return ApiResponse.success(Success.FIND_FRIEND_SUCCESS, FindFriendResponseDto.builder()
                .nickname(friend.getNickname())
                .email(friend.getEmail())
                .introduction(friend.getIntroduction())
                .build());
    }

    @Transactional
    public ApiResponse<?> getFriendsInfo( String authorizationHeader, LocalDate date, Pageable pageable){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //친구 정보 가져오기
        Page<Friendship> myfriends = friendshipRepository.findAllByUsersIdAndPage(pageable, users.getId());

        List<FriendshipDto> friendshipList = findFriendInfo(users, date, myfriends);
        PageInfo pageInfo = setPageInfo(myfriends);

        return ApiResponse.success(Success.GET_FRIEND_LIST_SUCCESS, FriendshipListResponseDto.builder()
                        .my_nickname(users.getNickname())
                        .friendshipList(friendshipList)
                        .pageInfo(pageInfo)
                        .build());
    }


    @Transactional
    public ApiResponse<?> addFriend(String authorizationHeader, FriendshipCreateRequestDto requestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Checking friend is existed
        Optional<Users> optionalFriend = usersRepository.findByNickname(requestDto.getNickname());
        if(optionalFriend.isEmpty())
            return ApiResponse.failure(Error.FRIEND_NICKNAME_NOT_FOUND);
        Users friend = optionalFriend.get();

        //Check friendship already existing
        Optional<Friendship> existingFriendship = friendshipRepository.checkExistingFriendship(users.getId(), friend.getId());
        if (existingFriendship.isPresent()) {
            return ApiResponse.failure(Error.FRIENDSHIP_ALREADY_EXIST);
        }

        //Create Friendship
        Friendship newFriendship = requestDto.toEntity(friend, users);
        friendshipRepository.save(newFriendship);

        //Create Alarm
        createAlarm(users, friend,"새로운 친구 신청이 왔습니다.");

        return ApiResponse.success(Success.REQUEST_FRIEND_SUCCESS, Map.of("friendship_id", newFriendship.getId()));
    }

    @Transactional
    public ApiResponse<?> getMemoirOfFriend(String authorizationHeader, Long memoir_id){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Checking memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findById(memoir_id);
        if(optionalMemoir.isEmpty())
            return ApiResponse.failure(Error.MEMOIR_NOT_FOUND);
        Memoir memoir = optionalMemoir.get();

        return  ApiResponse.success(Success.GET_FRIEND_MEMOIR_SUCCESS, OneMemoirResponseDto.builder()
                .nickname(memoir.getUsers().getNickname())
                .memoir_id(memoir.getId())
                .title(memoir.getTitle())
                .keep_memoir(memoir.getKeep_memoir())
                .problem_memoir(memoir.getProblem_memoir())
                .try_memoir(memoir.getTry_memoir())
                .created_at(memoir.getCreated_at())
                .build());

    }
    @Transactional
    public ApiResponse<?> updateFriend(String authorizationHeader, FriendUpdateRequestDto requestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Find Friendship
        Optional<Friendship> optionalFriendship = friendshipRepository.findById(requestDto.getFriendship_id());
        if(optionalFriendship.isEmpty())
            return ApiResponse.failure(Error.FRIENDSHIP_NOT_FOUND);
        Friendship friendship = optionalFriendship.get();

        //Check whether request nickname and friendship's from user nickname is matched
        if(!friendship.getFrom_user().getNickname().equals(requestDto.getNickname())
                || !friendship.getTo_user().getNickname().equals(users.getNickname()))
            return ApiResponse.failure(Error.NOT_MATCHED_NICKNAME);

        //Update Friendship
        friendship.updateStatus(requestDto.getFriendshipStatus());

        //Create Alarm
        if(requestDto.getFriendshipStatus().equals(FriendshipStatus.ACCEPT))
            createAlarm(users, friendship.getFrom_user(), "친구 신청이 수락 되었습니다. ");

        return ApiResponse.success(Success.UPDATE_FRIEND_SUCCESS, Map.of("nowStatus", friendship.getFriendshipStatus()));
    }

    @Transactional
    public ApiResponse<?> deleteFriend(String authorizationHeader, Long friendship_id){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Find Friendship
        Optional<Friendship> optionalFriendship = friendshipRepository.findById(friendship_id);
        if(optionalFriendship.isEmpty())
            return ApiResponse.failure(Error.FRIENDSHIP_NOT_FOUND);
        Friendship friendship = optionalFriendship.get();

        //Check is user's friendship
        Users to_user = friendship.getTo_user();
        Users from_user = friendship.getFrom_user();
        if(!to_user.equals(users)&&!from_user.equals(users))
            return ApiResponse.failure(Error.NOT_MATCHED_NICKNAME);

        //Update Friendship
        friendship.updateStatus(FriendshipStatus.REJECT);

        return ApiResponse.success(Success.DELETE_FRIEND_SUCCESS, FriendDeleteResponseDto.builder()
                .friendship_id(friendship_id)
                .to_user(friendship.getTo_user().getNickname())
                .from_user(friendship.getFrom_user().getNickname())
                .friendshipStatus(friendship.getFriendshipStatus())
                .build());

    }

    private void createAlarm(Users from_user, Users to_user, String message){
        alarmRepository.save(new Alarm(to_user, from_user.getNickname(), AlarmType.FRIEND, message));
    }

    public List<FriendshipDto> findFriendInfo(Users me, LocalDate date, Page<Friendship> myfriends){
        List<FriendshipDto> myfriendsInfo = new ArrayList<>();
        for (Friendship myfriend : myfriends) {
            Users friend;
            if (myfriend.getTo_user().equals(me)) {
                friend = myfriend.getFrom_user();
            } else {
                friend = myfriend.getTo_user();
            }

            // 당일 작성한 회고 찾기
            Optional<Memoir> optionalMemoir = memoirRepository.findByCreatedAt(friend.getId(), date);
            Boolean memoirStatus = optionalMemoir.isPresent();
            Long memoirId = optionalMemoir.map(Memoir::getId).orElse(0L);

            // FriendshipDto 생성
            FriendshipDto friendshipDto = FriendshipDto.builder()
                    .friend_nickname(friend.getNickname())
                    .memoir_status(memoirStatus)
                    .memoir_id(memoirId)
                    .build();
            myfriendsInfo.add(friendshipDto);
        }
        return myfriendsInfo;
    }

    private PageInfo setPageInfo(Page<Friendship> friendshipPage){
        return PageInfo.builder()
                .last(!friendshipPage.hasNext())
                .previous(friendshipPage.hasPrevious())
                .nowPage(friendshipPage.getNumber())
                .totalPages(friendshipPage.getTotalPages())
                .totalElements(friendshipPage.getTotalElements())
                .build();
    }
}
