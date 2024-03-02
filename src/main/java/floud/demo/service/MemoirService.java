package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Memoir;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.repository.MemoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;

@RequiredArgsConstructor
@Service
public class MemoirService {
    private final MemoirRepository memoirRepository;

    @Transactional
    public ApiResponse<?> createMemoir(MemoirCreateRequestDto memoirCreateRequestDto){
        Memoir newMemoir = memoirCreateRequestDto.toEntity();
        return ApiResponse.success(Success.MEMOIR_CREATE_SUCCESS, Map.of("memoir_id", newMemoir.getId()));
    }
}
