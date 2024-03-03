package floud.demo.domain;

import floud.demo.dto.memoir.MemoirUpdateRequestDto;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
public class Memoir{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "memoir_id")
    private Long id;

    @Column
    private String title;

    @Column
    private String keep_memoir;

    @Column
    private String problem_memoir;

    @Column
    private String try_memoir;

    //YYYY-MM-DD 형식으로 변경
    @CreatedDate
    @Column
    private LocalDate created_at;

    @LastModifiedDate
    @Column
    private  LocalDate updated_at;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Builder
    public Memoir(Long id, String title, String keep_memoir, String problem_memoir, String try_memoir,
                  LocalDate created_at, LocalDate updated_at, Users users){
        this.id = id;
        this.title = title;
        this.keep_memoir = keep_memoir;
        this.problem_memoir = problem_memoir;
        this.try_memoir = try_memoir;
        this.users = users;
    }

    public void update(MemoirUpdateRequestDto updateRequestDto){
        this.title = updateRequestDto.getTitle();
        this.keep_memoir = updateRequestDto.getKeep_memoir();
        this.problem_memoir = updateRequestDto.getProblem_memoir();
        this.try_memoir = updateRequestDto.getTry_memoir();
    }
}
