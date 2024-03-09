package floud.demo.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import floud.demo.common.response.Error;

@ResponseStatus(HttpStatus.FORBIDDEN)
public abstract class ForbiddenException extends ApiException {
    public ForbiddenException(final Error message) {
        super(HttpStatus.FORBIDDEN, message, null);
    }
}