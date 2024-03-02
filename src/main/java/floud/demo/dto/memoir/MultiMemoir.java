package floud.demo.dto.memoir;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class MultiMemoir {
    private Long memoir_id;
    private String title;
    private LocalDateTime created_at;
}
