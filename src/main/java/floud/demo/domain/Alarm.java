package floud.demo.domain;

import floud.demo.common.domain.BaseTimeEntity;
import floud.demo.domain.enums.AlarmType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Alarm extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "alarm_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "users_id")
    private Users users;

    @Column(nullable = false)
    private String nickname;

    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private AlarmType alarmType;

    @Column(nullable = false)
    private String message;

    @Builder
    public Alarm(Users users, String nickname, AlarmType alarmType, String message){
        this.users = users;
        this.nickname = nickname;
        this.alarmType = alarmType;
        this.message = message;
    }
}
