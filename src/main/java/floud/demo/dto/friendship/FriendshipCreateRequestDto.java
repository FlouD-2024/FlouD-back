package floud.demo.dto.friendship;

import floud.demo.domain.Friendship;
import floud.demo.domain.Users;
import floud.demo.domain.enums.FriendshipStatus;
import lombok.Getter;

@Getter
public class FriendshipCreateRequestDto {
    private String nickname;

    public Friendship toEntity(Users to_user, Users from_user){
        return Friendship.builder()
                .to_user(to_user)
                .from_user(from_user)
                .friendshipStatus(FriendshipStatus.WAITING)
                .build();
    }
}
