package floud.demo.domain;

import floud.demo.domain.enums.FriendshipStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class Friendship {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "frinedship_id")
    private Long id;

    @Column
    @Enumerated(value = EnumType.STRING)
    private FriendshipStatus friendshipStatus;

    // 친구 신청 송신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "to_user")
    private Users to_user;

    // 친구 신청 수신자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "from_user")
    private Users from_user;

    @Builder
    public Friendship(Long id, FriendshipStatus friendshipStatus, Users to_user, Users from_user){
        this.id = id;
        this.friendshipStatus = friendshipStatus;
        this.to_user = to_user;
        this.from_user = from_user;
    }

    public void updateStatus(FriendshipStatus friendshipStatus){
        this.friendshipStatus = friendshipStatus;
    }

}
