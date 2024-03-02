package floud.demo.common.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public enum Success {
    // Default
    SUCCESS(HttpStatus.OK, "Request successfully processed"),

    //200 SUCCESS
    MEMOIR_UPDATE_SUCCESS(HttpStatus.OK , "성공적으로 회고를 수정하였습니다."),
    GET_MY_MEMOIR_SUCCESS(HttpStatus.OK , "나의 회고를 성공적으로 조회하였습니다."),
    GET_FRIEND_MEMOIR_SUCCESS(HttpStatus.OK , "친구의 회고를 성공적으로 조회하였습니다."),
    MULTIPLE_MEMOIR_GET_SUCCESS(HttpStatus.OK , "주차별 회고 목록을 성공적으로 조회하였습니다."),

    //201 CREATED SUCCESS
    MEMOIR_CREATE_SUCCESS(HttpStatus.CREATED, "성공적으로 회고를 등록하였습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}