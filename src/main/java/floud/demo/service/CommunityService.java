package floud.demo.service;

import floud.demo.common.exception.NotFoundException;
import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Error;
import floud.demo.common.response.Success;
import floud.demo.domain.Community;
import floud.demo.domain.Users;
import floud.demo.domain.enums.PostType;
import floud.demo.dto.PageInfo;
import floud.demo.dto.community.*;
import floud.demo.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class CommunityService {
    private final AuthService authService;
    private final CommunityRepository communityRepository;

    @Transactional
    public ApiResponse<?> getCommunity(String authorizationHeader, PostType postType, Pageable pageable){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Get Posts of Community
        Page<Post> postPage = setPostList(pageable, postType.toString());
        List<Post>postList = postPage.stream()
                .map(post -> Post.builder()
                        .community_id(post.getCommunity_id())
                        .title(post.getTitle())
                        .content(post.getContent())
                        .postType(post.getPostType())
                        .build())
                .toList();

        //Pageable
        PageInfo pageInfo = PageInfo.builder()
                .last(!postPage.hasNext())
                .nowPage(postPage.getNumber())
                .totalPages(postPage.getTotalPages())
                .totalElements(postPage.getTotalElements())
                .build();

        return ApiResponse.success(Success.GET_COMMUNITY_SUCCESS, CommunityResponseDto.builder()
                .pageInfo(pageInfo)
                .nickname(users.getNickname())
                .postType(postType)
                .postList(postList)
                .build());
    }

    @Transactional
    public ApiResponse<?> getCommunityDetail(String authorizationHeader, Long community_id){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Get Community
        Community community = communityRepository.findById(community_id).orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."){});

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

        return ApiResponse.success(Success.CREATE_COMMUNITY_POST_SUCCESS, setResponseDto(newCommunity));
    }

    @Transactional
    public ApiResponse<?> updatePost(String authorizationHeader, UpdatePostRequestDto requestDto){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Get Community
        Community community = communityRepository.findById(requestDto.getCommunity_id()).orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."){});

        if(!checkMyPost(users, community))
            ApiResponse.failure(Error.NO_PERMISSION_TO_POST);

        community.update(requestDto.getTitle(), requestDto.getContent());
        communityRepository.flush();

        return ApiResponse.success(Success.UPDATE_COMMUNITY_POST_SUCCESS, setResponseDto(community));
    }

    @Transactional
    public ApiResponse<?> deletePost(String authorizationHeader, Long community_id){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        //Get Community
        Community community = communityRepository.findById(community_id).orElseThrow(() -> new NotFoundException("해당 게시글을 찾을 수 없습니다."){});

        if(!checkMyPost(users, community))
            ApiResponse.failure(Error.NO_PERMISSION_TO_POST);

        communityRepository.delete(community);

        return ApiResponse.success(Success.DELETE_COMMUNITY_POST_SUCCESS, Map.of("community_id", community_id));
    }


    private Boolean checkMyPost(Users users, Community community){
        return users.equals(community.getUsers());
    }

    private Page<Post> setPostList(Pageable pageable, String postType){
        Page<Community> communityPage = communityRepository.findAllByPostType(pageable, postType);
        return communityPage.map(community -> Post.builder()
                .community_id(community.getId())
                .title(community.getTitle())
                .content(community.getContent())
                .postType(community.getPostType())
                .build());
    }

    private PostResponseDto setResponseDto(Community community){
        return PostResponseDto.builder()
                .community_id(community.getId())
                .nickname(community.getUsers().getNickname())
                .title(community.getTitle())
                .content(community.getContent())
                .written_at(community.getCreated_at())
                .build();
    }

}
