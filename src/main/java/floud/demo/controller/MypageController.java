package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import floud.demo.service.MyPageService;
import lombok.Getter;
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

    @GetMapping("/check")
    public ApiResponse<?> checkDuplicatedName(@RequestParam(name = "nickname") String nickname){
        return  myPageService.checkDuplicatedName(nickname);
    }

    @PutMapping
    public ApiResponse<?> updateMypage(@RequestBody MypageUpdateRequestDto mypageUpdateRequestDto) {
        return myPageService.updateMypage(mypageUpdateRequestDto);
    }

    @GetMapping("/friend")
    public ApiResponse<?> getFriendList(){
        return myPageService.getFriendList();
    }
}
