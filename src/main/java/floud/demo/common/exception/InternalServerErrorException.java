package floud.demo.common.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(INTERNAL_SERVER_ERROR)
public abstract class InternalServerErrorException extends ApiException{
    public InternalServerErrorException (final String message) {
        super(INTERNAL_SERVER_ERROR, message, null);
    }
}