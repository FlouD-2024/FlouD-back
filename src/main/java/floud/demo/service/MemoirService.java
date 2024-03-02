package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.dto.memoir.MemoirUpdateRequestDto;
import floud.demo.dto.memoir.OneMemoirResponseDto;
import floud.demo.repository.MemoirRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;

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

        Memoir newMemoir = memoirCreateRequestDto.toEntity(users.get());
        memoirRepository.save(newMemoir);
        return ApiResponse.success(Success.MEMOIR_CREATE_SUCCESS, Map.of("memoir_id", newMemoir.getId()));
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

        return  ApiResponse.success(Success.MEMOIR_UPDATE_SUCCESS, Map.of("memoir_id", memoir.getId()));
    }

    @Transactional
    public ApiResponse<?> getOneMemoir(Long memoir_id){
        //Checking memoir
        Optional<Memoir> optionalMemoir = memoirRepository.findById(memoir_id);
        if(optionalMemoir.isEmpty())
            return ApiResponse.failure(Error.MEMOIR_NOT_FOUND);
        Memoir memoir = optionalMemoir.get();

        OneMemoirResponseDto responseDto = OneMemoirResponseDto.builder()
                .nickname(memoir.getUsers().getNickname())
                .title(memoir.getTitle())
                .keep_memoir(memoir.getKeep_memoir())
                .problem_memoir(memoir.getProblem_memoir())
                .try_memoir(memoir.getTry_memoir())
                .build();

        return  ApiResponse.success(Success.ONE_MEMOIR_GET_SUCCESS, responseDto);

    }
}
