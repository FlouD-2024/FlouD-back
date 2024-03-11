package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.service.HomeService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/home")
public class HomeController {
    private final HomeService homeService;
    @GetMapping
    public ApiResponse<?> getHome(@RequestHeader(value="Authorization") String authorizationHeader,
                                  @RequestParam(name = "date") LocalDate dateTime){
        return homeService.getHome(authorizationHeader, dateTime);
    }

    @GetMapping("/alarm")
    public ApiResponse<?> getAlarm(@RequestHeader(value="Authorization") String authorizationHeader){
        return homeService.getAlarm(authorizationHeader);
    }
}
