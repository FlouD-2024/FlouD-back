package floud.demo.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Error {
    // Default
    ERROR(HttpStatus.BAD_REQUEST, "Request processing failed"),

    // 404 NOT FOUND
    REFRESH_TOKEN_NOT_FOUND(HttpStatus.NOT_FOUND, "리프레시 토큰 정보를 찾을 수 없습니다."),
    USERS_NOT_FOUND(HttpStatus.NOT_FOUND, "유저 정보를 찾을 수 없습니다."),
    FRIEND_NICKNAME_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 닉네임의 회원을 찾을 수 없습니다."),
    FRIENDSHIP_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 친구 관계를 찾을 수 없습니다."),
    MEMOIR_NOT_FOUND(HttpStatus.NOT_FOUND, "해당 회고를 찾을 수 없습니다."),

    // 409 CONFLICT
    NOT_BE_FRIEND_MYSELF(HttpStatus.CONFLICT, "본인과는 친구가 될 수 없습니다."),
    MEMOIR_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 오늘의 회고를 작성하였습니다."),
    FRIENDSHIP_ALREADY_EXIST(HttpStatus.CONFLICT, "이미 친구 관계가 존재합니다."),
    NICKNAME_ALREADY_EXIST(HttpStatus.CONFLICT, "해당 닉네임을 가진 유저가 이미 존재합니다. 닉네임 중복 체크를 해주세요."),
    NOT_MATCHED_NICKNAME(HttpStatus.CONFLICT, "친구 관계 정보가 일치하지 않습니다."),

    // 500 INTERNAL SERVER ERROR
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Internal Server Error"),
    ;

    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}
