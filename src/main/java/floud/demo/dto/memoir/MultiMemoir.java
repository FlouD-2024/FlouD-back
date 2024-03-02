package floud.demo.dto.memoir;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public class MultiMemoir {
    private Long memoir_id;
    private String title;
    private LocalDateTime created_at;
}
