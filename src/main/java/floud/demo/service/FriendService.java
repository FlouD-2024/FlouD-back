package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Friendship;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.dto.friendship.FriendshipCreateRequestDto;
import floud.demo.dto.friendship.FriendshipDto;
import floud.demo.dto.friendship.FriendshipListResponseDto;
import floud.demo.dto.memoir.OneMemoirResponseDto;
import floud.demo.repository.FriendshipRepository;
import floud.demo.repository.MemoirRepository;
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

@Slf4j
@RequiredArgsConstructor
@Service
public class FriendService {

    private final UsersRepository usersRepository;
    private final MemoirRepository memoirRepository;
    private final FriendshipRepository friendshipRepository;


    @Transactional
    public ApiResponse<?> getFriendsInfo(LocalDate date){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //친구 정보 가져오기
        List<FriendshipDto> friendshipList = findFriendInfo(users, date);
        Integer totalFriendNum = friendshipList.size();

        return ApiResponse.success(Success.GET_FRIEND_LIST_SUCCESS, FriendshipListResponseDto.builder()
                        .my_nickname(users.getNickname())
                        .totalFriendNum(totalFriendNum)
                        .totalPage()
                        .friendshipList(friendshipList)
                        .build());
    }


    @Transactional
    public ApiResponse<?> addFriend(FriendshipCreateRequestDto requestDto){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

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

        return ApiResponse.success(Success.REQUEST_FRIEND_SUCCESS, Map.of("friendship_id", newFriendship.getId()));
    }

    @Transactional
    public ApiResponse<?> getMemoirOfFriend(Long memoir_id){
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

    public List<FriendshipDto> findFriendInfo(Users me, LocalDate date){
        List<Friendship> myfriends = friendshipRepository.findAllByUsersId(me.getId());
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

}
