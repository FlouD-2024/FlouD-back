package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.service.MemoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/memoir")
public class MemoirController {
    final private MemoirService memoirService;
    @PostMapping
    public ApiResponse<?> createMemoir(@RequestBody MemoirCreateRequestDto memoirCreateRequestDto){
        return memoirService.createMemoir(memoirCreateRequestDto);
    }
}
