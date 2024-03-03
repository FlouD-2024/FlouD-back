package floud.demo.dto.memoir;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class MultiMemoirResponseDto {
    private String nickname;
    private List<MultiMemoir> memoirList;
}
