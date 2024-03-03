package floud.demo.dto.mypage;

import floud.demo.domain.Goal;
import lombok.Getter;

import java.util.List;

@Getter
public class MypageUpdateRequestDto {
    private String nickname;
    private String introduction;
    private List<Goal> goalList;
}
