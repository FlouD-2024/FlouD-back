package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.dto.memoir.*;
import floud.demo.repository.MemoirRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class MemoirService {
    private final UsersRepository usersRepository;
    private final MemoirRepository memoirRepository;

    @Transactional
    public ApiResponse<?> createMemoir(MemoirCreateRequestDto memoirCreateRequestDto){
        //Checking user
        Optional<Users> users = usersRepository.findById(1L);
        if(users.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        log.info("유저 이름 -> {}", users.get().getNickname());

        //Check whether user posts today's memoir
        LocalDate now = LocalDate.now();
        if(memoirRepository.existsByUserAndCreatedAtBetween(users.get().getId(), now))
            return ApiResponse.failure(Error.MEMOIR_ALREADY_EXIST);

        //Create Memoir
        Memoir newMemoir = memoirCreateRequestDto.toEntity(users.get());
        memoirRepository.save(newMemoir);
        return ApiResponse.success(Success.CREATE_MEMOIR_SUCCESS, Map.of("memoir_id", newMemoir.getId()));
    }

    @Transactional
    public ApiResponse<?> updateMemoir(Long memoir_id, MemoirUpdateRequestDto memoirUpdateRequestDto){
        //Checking user
        Optional<Users> users = usersRepository.findById(1L);
        if(users.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        log.info("유저 이름 -> {}", users.get().getNickname());

        //Checking memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findById(memoir_id);
        if(optionalMemoir.isEmpty())
            return ApiResponse.failure(Error.MEMOIR_NOT_FOUND);
        Memoir memoir = optionalMemoir.get();

        //Update Memoir
        memoir.update(memoirUpdateRequestDto);

        return  ApiResponse.success(Success.UPDATE_MEMOIR_SUCCESS, Map.of("memoir_id", memoir.getId()));
    }

    @Transactional
    public ApiResponse<?> getOneMemoir(LocalDate dateTime){
        //Checking user
        Optional<Users> users = usersRepository.findById(1L);
        if(users.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        log.info("유저 이름 -> {}", users.get().getNickname());

        //Check whether user posts today's memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findByCreatedAt(users.get().getId(), dateTime);

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
    public ApiResponse<?> getWeekMemoir(LocalDate startDate, LocalDate endDate){
        //Checking user
        Optional<Users> optionalUsers = usersRepository.findById(1L);
        if(optionalUsers.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        Users users = optionalUsers.get();
        log.info("유저 이름 -> {}", users.getNickname());

        //일주일 간 회고 조회
        List<Memoir> memoirs = memoirRepository.findAllByWeek(users.getId(), startDate, endDate);
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
}
