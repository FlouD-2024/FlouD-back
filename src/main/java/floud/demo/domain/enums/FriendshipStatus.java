package floud.demo.domain.enums;

import lombok.Getter;

@Getter
public enum FriendshipStatus {
    WAITING("WAITING"),
    ACCEPT("ACCEPT"),
    REJECT("REJECT");

    private final String friendshipStatus;

    FriendshipStatus(String friendshipStatus) {
        this.friendshipStatus = friendshipStatus;
    }
}
