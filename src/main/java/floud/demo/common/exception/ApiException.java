package floud.demo.common.exception;

import floud.demo.common.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiException extends ResponseStatusException {
    private final boolean success;
    private final Error message;
    private final Object data;
    public ApiException(final HttpStatus code, final Error message, final Object data) {
        super(code, message.toString()); // Error 타입이 toString() 메서드를 적절히 오버라이드한다고 가정
        this.success = false;
        this.message = message;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public Error getError() {
        return message;
    }
    public Object getData() {
        return data;
    }
    public Map<String, Object> toResponseMap() {
        Map<String, Object> exception = new HashMap<>();
        exception.put("code", this.getStatusCode().value());
        exception.put("success", this.success);
        exception.put("message", this.message.toString());
        exception.put("data", this.data);
        return exception;
    }
}