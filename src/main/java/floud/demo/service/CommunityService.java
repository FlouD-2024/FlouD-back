package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.domain.Community;
import floud.demo.domain.Users;
import floud.demo.domain.enums.PostType;
import floud.demo.dto.community.CommunityResponseDto;
import floud.demo.dto.community.Post;
import floud.demo.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final AuthService authService;
    private final CommunityRepository communityRepository;
    public ApiResponse<?> getCommunity(String authorizationHeader, PostType postType){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        List<Community> communityList =  communityRepository.findTop30ByPostType(postType.toString());
        List<Post> postList = communityList.stream().
                map(community -> Post.builder()
                        .community_id(community.getId())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .postType(community.getPostType())
                        .build())
                .collect(Collectors.toList());

        return ApiResponse.success(Success.GET_COMMUNITY_FIND_FRIEND_SUCCESS, CommunityResponseDto.builder()
                .nickname(users.getNickname())
                .postType(postType)
                .postList(postList)
                .build());
    }
}
