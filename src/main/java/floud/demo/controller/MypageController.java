package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RequiredArgsConstructor
@RestController
@RequestMapping("api/mypage")
public class MypageController {
    private final MyPageService myPageService;
    @GetMapping
    public ApiResponse<?> getMypage(){
        return myPageService.getMypage();
    }
}
