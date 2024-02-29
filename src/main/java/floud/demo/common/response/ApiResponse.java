package floud.demo.common.response;

import floud.demo.common.Success;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
public class ApiResponse<T> {

    private final int code;
    private final String message;
    private final T data;

    public static <T> ApiResponse<T> success() {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                null
        );
    }

    public static <T> ApiResponse<T> success(Success success, T data) {
        return new ApiResponse<>(
                success.getHttpStatusCode(),
                success.getMessage(),
                data
        );
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(
                Success.SUCCESS.getHttpStatusCode(),
                Success.SUCCESS.getMessage(),
                data
        );
    }

}
