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

    @GetMapping("/my")
    public ApiResponse<?> getFriendsInfo(@RequestParam(name = "date") LocalDate date){
        return friendService.getFriendsInfo(date);
    }

    @PostMapping()
    public ApiResponse<?> addFriend(@RequestBody FriendshipCreateRequestDto requestDto){
        return friendService.addFriend(requestDto);
    }

    @GetMapping("memoir/{memoir_id}")
    public ApiResponse<?> getOneMemoir(@PathVariable Long memoir_id){
        return friendService.getMemoirOfFriend(memoir_id);
    }

}
