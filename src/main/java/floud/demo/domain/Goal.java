package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@Entity
public class Goal extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "goal_id")
    private Long id;

    @Column(length = 50, nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime deadLine;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Builder
    public Goal(Long id, String content, LocalDateTime deadLine){
        this.id = id;
        this.content = content;
        this.deadLine = deadLine;
    }
}
