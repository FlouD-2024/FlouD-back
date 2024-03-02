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

    //201 CREATED SUCCESS
    MEMOIR_CREATE_SUCCESS(HttpStatus.CREATED, "성공적으로 회고를 등록하였습니다."),
    ;


    private final HttpStatus httpStatus;
    private final String message;

    public int getHttpStatusCode() {
        return httpStatus.value();
    }
}