package floud.demo.dto.mypage;

import floud.demo.dto.mypage.dto.UpdateGoal;
import lombok.Getter;

import java.util.List;

@Getter
public class MypageUpdateRequestDto {
    private String nickname;
    private String introduction;
    private List<UpdateGoal> goalList;
}
