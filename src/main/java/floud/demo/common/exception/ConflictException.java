package floud.demo.common.exception;

import floud.demo.common.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;

@ResponseStatus(HttpStatus.CONFLICT)
public abstract class ConflictException extends ApiException {
    public ConflictException(final Error message) {
        super(CONFLICT, message, null);
    }
}