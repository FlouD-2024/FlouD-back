package floud.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class ApiException extends ResponseStatusException {
    private final boolean success;
    private final Object data;
    public ApiException(final HttpStatus code, final String message, final Object data) {
        super(code, message);
        this.success = false;
        this.data = data;
    }

}