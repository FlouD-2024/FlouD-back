package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.domain.enums.PostType;
import floud.demo.dto.community.SavePostRequestDto;
import floud.demo.dto.community.UpdatePostRequestDto;
import floud.demo.service.CommunityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/community")
public class CommunityController {
    private final CommunityService communityService;

    @GetMapping
    public ApiResponse<?> getCommunity(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @RequestParam(name = "post_type")PostType postType){
        return communityService.getCommunity(authorizationHeader, postType);
    }

    @GetMapping("/detail/{community_id}")
    public ApiResponse<?> getCommunityDetail(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @PathVariable(name = "community_id")Long community_id){
        return communityService.getCommunityDetail(authorizationHeader, community_id);
    }

    @PostMapping("/post")
    public ApiResponse<?> savePost(@RequestHeader(value="Authorization") String authorizationHeader,
                                   @RequestBody SavePostRequestDto requestDto){
        return communityService.savePost(authorizationHeader, requestDto);
    }

    @PutMapping("/post")
    public ApiResponse<?> updatePost(@RequestHeader(value="Authorization") String authorizationHeader,
                                     @RequestBody UpdatePostRequestDto requestDto){
        return communityService.updatePost(authorizationHeader, requestDto);
    }

    @DeleteMapping("/post/{community_id}")
    public ApiResponse<?> deletePost(@RequestHeader(value="Authorization") String authorizationHeader,
                                     @PathVariable(name = "community_id") Long community_id){
        return communityService.deletePost(authorizationHeader, community_id);
    }
}
