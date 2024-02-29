package floud.demo.common.response;

import floud.demo.common.Success;
import floud.demo.common.Error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ApiResponse<T> {

    private final int code;
    private final boolean success;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getHttpStatus().is2xxSuccessful(),
                Success.SUCCESS.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getHttpStatus().is2xxSuccessful(),
                success.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success, T data) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getHttpStatus().is2xxSuccessful(),
                success.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getHttpStatus().is2xxSuccessful(),
                Success.SUCCESS.getMessage(),
                data
        );
    }


    public static <T> ApiResponse<T> failure(Error error) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getHttpStatus().is2xxSuccessful(),
                error.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> failure(Error error, String message) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getHttpStatus().is2xxSuccessful(),
                message,
                null
        );
    }

    public static <T> ApiResponse<T> failure(Error error, T data) {
        return new ApiResponse<>(
                error.getHttpStatusCode(),
                error.getHttpStatus().is2xxSuccessful(),
                error.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> failure(T data) {
        return new ApiResponse<>(
                Error.ERROR.getHttpStatusCode(),
                Error.ERROR.getHttpStatus().is2xxSuccessful(),
                Error.ERROR.getMessage(),
                data
        );
    }

}
