package floud.demo.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
public abstract class ForbiddenException extends ApiException {
    public ForbiddenException(final String message) {
        super(FORBIDDEN, message, null);
    }
}