package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class MyPageService {

    @Transactional
    public ApiResponse<?> getMypage(){
        return ApiResponse.success(Success.SUCCESS);
    }
}
