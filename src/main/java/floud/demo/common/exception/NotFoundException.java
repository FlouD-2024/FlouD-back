package floud.demo.common.exception;


import org.springframework.http.HttpStatus;

import static org.springframework.http.HttpStatus.NOT_FOUND;

public abstract class NotFoundException extends ApiException {
    public NotFoundException(final String message) {
        super(HttpStatus.NOT_FOUND, message, null);
    }
}