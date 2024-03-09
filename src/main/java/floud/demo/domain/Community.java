package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import floud.demo.domain.enums.PostType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Community extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "community_id")
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Builder
    Community(Long id, String title, String content, PostType postType, Users users){
        this.id = id;
        this.title = title;
        this.content = content;
        this.postType = postType;
        this.users = users;
    }
}
