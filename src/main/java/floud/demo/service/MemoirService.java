package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.repository.MemoirRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MemoirService {
    private final MemoirRepository memoirRepository;

    @Transactional
    public ApiResponse<?> createMemoir(MemoirCreateRequestDto memoirCreateRequestDto){
        return ApiResponse.success(Success.MEMOIR_CREATE_SUCCESS);
    }
}
