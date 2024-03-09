package floud.demo.common.exception;

import floud.demo.common.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

@ResponseStatus(HttpStatus.UNAUTHORIZED)
public abstract class UnauthorizedException extends ApiException {
    public UnauthorizedException(final String message) {
        super(UNAUTHORIZED, message, null);
    }
}