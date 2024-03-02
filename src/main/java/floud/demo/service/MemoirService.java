package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Memoir;
import floud.demo.domain.Users;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.repository.MemoirRepository;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.User;
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
        Optional<Users> users = usersRepository.findById(1L);
        if(users.isEmpty())
            return ApiResponse.failure(Error.USERS_NOT_FOUND);
        log.info("유저 이름 -> {}", users.get().getNickname());

        Memoir newMemoir = memoirCreateRequestDto.toEntity(users.get());
        memoirRepository.save(newMemoir);
        return ApiResponse.success(Success.MEMOIR_CREATE_SUCCESS, Map.of("memoir_id", newMemoir.getId()));
    }
}
