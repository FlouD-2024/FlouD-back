package floud.demo.controller;

import floud.demo.common.response.ApiResponse;
import floud.demo.dto.memoir.MemoirCreateRequestDto;
import floud.demo.dto.memoir.MemoirUpdateRequestDto;
import floud.demo.service.MemoirService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/memoir")
public class MemoirController {
    private final MemoirService memoirService;
    @PostMapping
    public ApiResponse<?> createMemoir(@RequestBody MemoirCreateRequestDto memoirCreateRequestDto){
        return memoirService.createMemoir(memoirCreateRequestDto);
    }

    @PutMapping("/{memoir_id}")
    public ApiResponse<?> updateMemoir(@PathVariable Long memoir_id, @RequestBody MemoirUpdateRequestDto memoirUpdateRequestDto){
        return memoirService.updateMemoir(memoir_id, memoirUpdateRequestDto);
    }

    @GetMapping("/my")
    public ApiResponse<?> getOneMemoir(@RequestParam(name = "date") LocalDateTime dateTime){
        return memoirService.getOneMemoir(dateTime);
    }

    @GetMapping("/week")
    public ApiResponse<?> getWeekMemoir(@RequestParam(name = "start-date") LocalDateTime startDate){
        LocalDateTime endDate = startDate.plusDays(7);
        return memoirService.getWeekMemoir(startDate, endDate);
    }
}
