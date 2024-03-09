package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import floud.demo.domain.Memoir;
import floud.demo.dto.mypage.MypageUpdateRequestDto;
import jakarta.persistence.*;

import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
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

    @Column(length = 50)
    private String email;

    @Column(length = 500)
    @ColumnDefault("") //디폴트 빈 문자열 설정
    private String introduction;

    @Column(length = 500)
    @ColumnDefault("") //디폴트 빈 문자열 설정
    private String memo;

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Memoir> memoirList = new ArrayList<>();

    @OneToMany(mappedBy = "users", fetch = FetchType.LAZY)
    private List<Goal> goalList = new ArrayList<>();

    @Builder
    public Users(Long id, String social_id, String nickname, String email,String introduction, String memo, List<Memoir> memoirList){
        this.Id = id;
        this.social_id = social_id;
        this.nickname = nickname;
        this.email = email;
        this.introduction = introduction;
        this.memo = memo;
        this.memoirList = memoirList;
    }

    public void updateUserInfo(MypageUpdateRequestDto requestDto){
        this.nickname = requestDto.getNickname();
        this.introduction = requestDto.getIntroduction();
    }
}
