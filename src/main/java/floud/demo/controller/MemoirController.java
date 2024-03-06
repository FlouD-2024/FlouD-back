package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.dto.memoir.MemoirUpdateRequestDto;
import floud.demo.service.MemoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/memoir")
public class MemoirController {
    private final MemoirService memoirService;
    @PostMapping
    public ApiResponse<?> createMemoir(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @RequestBody MemoirCreateRequestDto memoirCreateRequestDto){
        return memoirService.createMemoir(authorizationHeader, memoirCreateRequestDto);
    }

    @PutMapping("/{memoir_id}")
    public ApiResponse<?> updateMemoir(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @PathVariable Long memoir_id, @RequestBody MemoirUpdateRequestDto memoirUpdateRequestDto){
        return memoirService.updateMemoir(authorizationHeader, memoir_id, memoirUpdateRequestDto);
    }

    @GetMapping("/my")
    public ApiResponse<?> getOneMemoir(@RequestHeader(value="Authorization") String authorizationHeader,
                                       @RequestParam(name = "date") LocalDate dateTime){
        return memoirService.getOneMemoir(authorizationHeader, dateTime);
    }

    @GetMapping("/week")
    public ApiResponse<?> getWeekMemoir(@RequestHeader(value="Authorization") String authorizationHeader,
                                        @RequestParam(name = "start-date") LocalDate startDate){
        LocalDate endDate = startDate.plusDays(7);
        return memoirService.getWeekMemoir(authorizationHeader, startDate, endDate);
    }
}
