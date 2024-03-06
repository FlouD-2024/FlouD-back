package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.friendship.FriendshipCreateRequestDto;
import floud.demo.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("")
    public ApiResponse<?> findFriend(@RequestHeader(value="Authorization") String authorizationHeader, @RequestParam(name = "nickname") String nickname){
        return friendService.findFriend(authorizationHeader, nickname);
    }

    @GetMapping("/my")
    public ApiResponse<?> getFriendsInfo(@RequestHeader(value="Authorization") String authorizationHeader, @RequestParam(name = "date") LocalDate date){
        return friendService.getFriendsInfo(authorizationHeader, date);
    }

    @PostMapping("/request")
    public ApiResponse<?> addFriend(@RequestHeader(value="Authorization") String authorizationHeader, @RequestBody FriendshipCreateRequestDto requestDto){
        return friendService.addFriend(authorizationHeader, requestDto);
    }

    @GetMapping("memoir/{memoir_id}")
    public ApiResponse<?> getOneMemoir(@RequestHeader(value="Authorization") String authorizationHeader, @PathVariable Long memoir_id){
        return friendService.getMemoirOfFriend(authorizationHeader, memoir_id);
    }

}
