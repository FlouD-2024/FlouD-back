package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import floud.demo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/mypage")
public class MypageController {
    private final MyPageService myPageService;
    @GetMapping
    public ApiResponse<?> getMypage(){
        return myPageService.getMypage();
    }

    @PutMapping
    public ApiResponse<?> updateMypage(@RequestBody MypageUpdateRequestDto mypageUpdateRequestDto) {
        return myPageService.updateMypage(mypageUpdateRequestDto);
    }
}
