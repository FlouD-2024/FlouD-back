package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import floud.demo.domain.Memoir;
import jakarta.persistence.*;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Users extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "users_id")
    private Long Id;

    @Column(nullable = false)
    private String social_id;

    @Column(length = 50)
    private String nickname;

    @Column(length = 500)
    private String introduction;

    @Column(length = 500)
    private String memo;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Memoir> memoirList = new ArrayList<>();
}
