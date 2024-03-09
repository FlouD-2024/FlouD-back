package floud.demo.dto.mypage;

import floud.demo.dto.mypage.dto.MyPost;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CommunityResponseDto {
    private String nickname;
    private List<MyPost> postList;
}
