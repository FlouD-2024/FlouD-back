package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.service.FriendService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/friend")
public class FriendController {
    private final FriendService friendService;

    @GetMapping("memoir/{memoir_id}")
    public ApiResponse<?> getOneMemoir(@PathVariable Long memoir_id){
        return friendService.getMemoirOfFriend(memoir_id);
    }

}
