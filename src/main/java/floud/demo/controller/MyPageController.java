package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.mypage.MypageFriendUpdateRequestDto;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import floud.demo.service.MyPageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/mypage")
public class MyPageController {
    private final MyPageService myPageService;
    @GetMapping
    public ApiResponse<?> getMyPage(@RequestHeader(value="Authorization") String authorizationHeader){
        return myPageService.getMyPage(authorizationHeader);
    }

    @GetMapping("/check")
    public ApiResponse<?> checkDuplicatedName(@RequestHeader(value="Authorization") String authorizationHeader,
                                              @RequestParam(name = "nickname") String nickname){
        return  myPageService.checkDuplicatedName(authorizationHeader, nickname);
    }

    @PutMapping
    public ApiResponse<?> updateMyPage(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @RequestBody MypageUpdateRequestDto mypageUpdateRequestDto) {
        return myPageService.updateMyPage(authorizationHeader, mypageUpdateRequestDto);
    }

    @GetMapping("/friend")
    public ApiResponse<?> getFriendList(@RequestHeader(value="Authorization") String authorizationHeader){
        return myPageService.getFriendList(authorizationHeader);
    }

}
