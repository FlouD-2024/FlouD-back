package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.common.response.Success;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
public class HealthCheckController {

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public ApiResponse<?> healthCheck() {
        return ApiResponse.success(Success.SUCCESS);
    }
}
