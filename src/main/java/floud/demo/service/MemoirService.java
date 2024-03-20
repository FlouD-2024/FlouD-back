package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Alarm;
import floud.demo.domain.Friendship;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.domain.enums.AlarmType;
import floud.demo.dto.memoir.*;
import floud.demo.repository.AlarmRepository;
import floud.demo.repository.FriendshipRepository;
import floud.demo.repository.MemoirRepository;
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
public class MemoirService {
    private final AuthService authService;
    private final MemoirRepository memoirRepository;
    private final AlarmRepository alarmRepository;
    private final FriendshipRepository friendshipRepository;

    @Transactional
    public ApiResponse<?> createMemoir(String authorizationHeader, MemoirCreateRequestDto memoirCreateRequestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Check whether user posts today's memoir
        LocalDate now = LocalDate.now();
        log.debug(now.toString());
        if(memoirRepository.existsByUserAndCreatedAtBetween(users.getId(), now))
            return ApiResponse.failure(Error.MEMOIR_ALREADY_EXIST);

        //Create Memoir
        Memoir newMemoir = memoirCreateRequestDto.toEntity(users);
        memoirRepository.save(newMemoir);

        //Create Alarm
        createAlarm(users);

        return ApiResponse.success(Success.CREATE_MEMOIR_SUCCESS, Map.of("memoir_id", newMemoir.getId()));
    }

    @Transactional
    public ApiResponse<?> updateMemoir(String authorizationHeader, Long memoir_id, MemoirUpdateRequestDto memoirUpdateRequestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Checking memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findById(memoir_id);
        if(optionalMemoir.isEmpty())
            return ApiResponse.failure(Error.MEMOIR_NOT_FOUND);
        Memoir memoir = optionalMemoir.get();

        if(!checkMyMemoir(users, memoir))
            return ApiResponse.failure(Error.NO_PERMISSION_TO_MEMOIR);

        //Update Memoir
        memoir.update(memoirUpdateRequestDto);

        return  ApiResponse.success(Success.UPDATE_MEMOIR_SUCCESS, Map.of("memoir_id", memoir.getId()));
    }

    @Transactional
    public ApiResponse<?> getOneMemoir(String authorizationHeader, LocalDate dateTime){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Check whether user posts today's memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findByCreatedAt(users.getId(), dateTime);

        if(optionalMemoir.isEmpty())
            return ApiResponse.failure(Error.MEMOIR_NOT_FOUND);

        Memoir memoir = optionalMemoir.get();

        OneMemoirResponseDto responseDto = OneMemoirResponseDto.builder()
                .nickname(memoir.getUsers().getNickname())
                .memoir_id(memoir.getId())
                .title(memoir.getTitle())
                .keep_memoir(memoir.getKeep_memoir())
                .problem_memoir(memoir.getProblem_memoir())
                .try_memoir(memoir.getTry_memoir())
                .created_at(memoir.getCreated_at())
                .build();

        return  ApiResponse.success(Success.GET_MY_MEMOIR_SUCCESS, responseDto);

    }

    @Transactional
    public ApiResponse<?> getWeekMemoir(String authorizationHeader, LocalDate startDate, LocalDate endDate){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //일주일 간 회고 조회
        List<Memoir> memoirs = memoirRepository.findAllByDate(users.getId(), startDate, endDate);
        log.info("start-date -> {}", startDate);
        log.info("end-date -> {}", endDate);

        List<MultiMemoir> multiMemoirs = memoirs.stream()
                .map(memoir -> MultiMemoir.builder()
                        .memoir_id(memoir.getId())
                        .title(memoir.getTitle())
                        .created_at(memoir.getCreated_at())
                        .build())
                .collect(Collectors.toList());


        MultiMemoirResponseDto responseDto  = MultiMemoirResponseDto.builder()
                .nickname(users.getNickname())
                .memoirList(multiMemoirs)
                .build();

        return  ApiResponse.success(Success.GET_MULTIPLE_MEMOIR_SUCCESS, responseDto);

    }

    private boolean checkMyMemoir(Users users, Memoir memoir){
        return  users.equals(memoir.getUsers());
    }

    private void createAlarm(Users user){
        List<Users> friendList = findMyFriendList(user);
        for (Users friend : friendList) {
            String message = "최근 회고를 작성했습니다.";
            alarmRepository.save(new Alarm(friend, user.getNickname(), AlarmType.MEMOIR, message));
        }
    }

    private List<Users> findMyFriendList(Users me){
        List<Friendship> myfriendship = friendshipRepository.findAllByUsersId(me.getId());
        List<Users> friendList = new ArrayList<>();
        for (Friendship myfriend : myfriendship) {
            Users friend;
            if (myfriend.getTo_user().equals(me)) {
                friend = myfriend.getFrom_user();
            } else {
                friend = myfriend.getTo_user();
            }
            friendList.add(friend);
        }
        return friendList;
    }

}
