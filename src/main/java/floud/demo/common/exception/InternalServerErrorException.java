package floud.demo.common.exception;

import floud.demo.common.response.Error;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
public abstract class InternalServerErrorException extends ApiException{
    public InternalServerErrorException (final Error message) {
        super(INTERNAL_SERVER_ERROR, message, null);
    }
}