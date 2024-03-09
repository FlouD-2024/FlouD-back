package floud.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

public abstract class ApiException extends ResponseStatusException {
    private final boolean success;
    private final Object data;
    public ApiException(final HttpStatus code, final String message, final Object data) {
        super(code, message);
        this.success = false;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public Object getData() {
        return data;
    }
    public Map<String, Object> toResponseMap() {
        Map<String, Object> response = new HashMap<>();
        response.put("code", this.getStatusCode().value());
        response.put("success", this.success);
        response.put("message", this.getReason());
        response.put("data", this.data);
        return response;
    }
}