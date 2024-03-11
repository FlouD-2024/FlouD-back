package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/home")
public class HomeController {
    private final HomeService homeService;
    @GetMapping
    public ApiResponse<?> getHome(@RequestHeader(value="Authorization") String authorizationHeader){
        return homeService.getHome(authorizationHeader);
    }
}
