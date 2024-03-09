package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.domain.Community;
import floud.demo.domain.Users;
import floud.demo.domain.enums.PostType;
import floud.demo.repository.CommunityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CommunityService {
    private final AuthService authService;
    private final CommunityRepository communityRepository;
    public ApiResponse<?> getCommunity(String authorizationHeader, PostType postType){
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);

        List<Community> communityList =  communityRepository.findAll();


        return ApiResponse.success(Success.GET_COMMUNITY_FIND_FRIEND_SUCCESS);
    }
}
