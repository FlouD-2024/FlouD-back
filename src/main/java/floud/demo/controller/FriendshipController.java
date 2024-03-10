package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.friendship.FriendshipCreateRequestDto;
import floud.demo.dto.friendship.FriendUpdateRequestDto;
import floud.demo.service.FriendshipService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friend")
public class FriendshipController {
    private final FriendshipService friendService;

    @GetMapping("")
    public ApiResponse<?> findFriend(@RequestHeader(value="Authorization") String authorizationHeader,
                                     @RequestParam(name = "nickname") String nickname){
        return friendService.findFriend(authorizationHeader, nickname);
    }

    @GetMapping("/my")
    public ApiResponse<?> getFriendsInfo(@RequestHeader(value="Authorization") String authorizationHeader,
                                         @RequestParam(name = "date") LocalDate date,
                                         @PageableDefault(size = 8) Pageable pageable){
        return friendService.getFriendsInfo(authorizationHeader, date, pageable);
    }

    @PostMapping("/request")
    public ApiResponse<?> addFriend(@RequestHeader(value="Authorization") String authorizationHeader,
                                    @RequestBody FriendshipCreateRequestDto requestDto){
        return friendService.addFriend(authorizationHeader, requestDto);
    }

    @GetMapping("memoir/{memoir_id}")
    public ApiResponse<?> getOneMemoir(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @PathVariable Long memoir_id){
        return friendService.getMemoirOfFriend(authorizationHeader, memoir_id);
    }
    @PutMapping
    public ApiResponse<?> updateFriend(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @RequestBody FriendUpdateRequestDto requestDto){
        return friendService.updateFriend(authorizationHeader, requestDto);
    }

    @PutMapping("/{friendship_id}")
    public ApiResponse<?> deleteFriend(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @PathVariable(name = "friendship_id") Long friendship_id){
        return friendService.deleteFriend(authorizationHeader, friendship_id);
    }
}
