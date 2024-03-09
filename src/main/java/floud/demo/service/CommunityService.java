package floud.demo.service;

import floud.demo.common.exception.NotFoundException;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Community;
import floud.demo.domain.Users;
import floud.demo.domain.enums.PostType;
import floud.demo.dto.community.*;
import floud.demo.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final AuthService authService;
    private final CommunityRepository communityRepository;

    @Transactional
    public ApiResponse<?> getCommunity(String authorizationHeader, PostType postType){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        List<Post> postList = setPostList(postType.toString());

        return ApiResponse.success(Success.GET_COMMUNITY_SUCCESS, CommunityResponseDto.builder()
                .nickname(users.getNickname())
                .postType(postType)
                .postList(postList)
                .build());
    }

    @Transactional
    public ApiResponse<?> getCommunityDetail(String authorizationHeader, Long community_id){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        Community community = communityRepository.findById(community_id)
                .orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."){});

        return ApiResponse.success(Success.GET_COMMUNITY_DETAIL_SUCCESS, CommunityDetailResponseDto.builder()
                        .my_nickname(users.getNickname())
                        .community_id(community.getId())
                        .writer_nickname(community.getUsers().getNickname())
                        .isMine(checkMyPost(users, community))
                        .title(community.getTitle())
                        .content(community.getContent())
                        .written_at(community.getUpdated_at())
                .build());
    }

    @Transactional
    public ApiResponse<?> savePost(String authorizationHeader, SavePostRequestDto requestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Create Community Post
        Community newCommunity = communityRepository.save(requestDto.toEntity(users));

        return ApiResponse.success(Success.CREATE_COMMUNITY_POST_SUCCESS, SavePostResponseDto.builder()
                .community_id(newCommunity.getId())
                .nickname(newCommunity.getUsers().getNickname())
                .title(newCommunity.getTitle())
                .content(newCommunity.getContent())
                .written_at(newCommunity.getCreated_at())
                .build());
    }


    private Boolean checkMyPost(Users users, Community community){
        return users.equals(community.getUsers());
    }

    private List<Post> setPostList(String postType){
        List<Community> communityList =  communityRepository.findTop30ByPostType(postType);

        return communityList.stream().
                map(community -> Post.builder()
                        .community_id(community.getId())
                        .title(community.getTitle())
                        .content(community.getContent())
                        .postType(community.getPostType())
                        .build())
                .collect(Collectors.toList());
    }

}
