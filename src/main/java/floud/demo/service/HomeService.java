package floud.demo.service;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import floud.demo.domain.Users;
import floud.demo.repository.UsersRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class HomeService {
    private final AuthService authService;
    private final UsersRepository usersRepository;

    public ApiResponse<?> getHome(String authorizationHeader) {
        //Get user
        Users users = authService.findUserByToken(authorizationHeader);
        return ApiResponse.success(Success.GET_HOME_SUCCESS);
    }
}
