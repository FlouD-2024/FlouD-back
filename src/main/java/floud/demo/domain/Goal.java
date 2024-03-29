package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

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
    private LocalDate deadline;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Builder
    public Goal(Long id, String content, LocalDate deadline, Users users){
        this.id = id;
        this.content = content;
        this.deadline = deadline;
        this.users = users;
    }

    public void update(String content, LocalDate deadline){
        this.content = content;
        this.deadline = deadline;
    }

}
