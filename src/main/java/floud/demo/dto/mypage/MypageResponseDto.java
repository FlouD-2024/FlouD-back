package floud.demo.dto.mypage;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MypageResponseDto {
    private String nickname;
    private String introduction;
    private List<MyGoal> goalList;
}
