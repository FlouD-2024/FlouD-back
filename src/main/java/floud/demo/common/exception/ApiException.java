package floud.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public abstract class ApiException extends ResponseStatusException {
    private final boolean success;
    public ApiException(final HttpStatus code, final String message) {
        super(code, message);
        this.success = false;
    }

    public boolean isSuccess() {
        return success;
    }
}