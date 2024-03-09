package floud.demo.config.handler;

import floud.demo.common.exception.ApiException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;


import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<Object> handleApiException(ApiException ex, WebRequest request) {
        Map<String, Object> responseBody = new LinkedHashMap<>();

        responseBody.put("code", ex.getStatusCode().value());
        responseBody.put("success", ex.getStatusCode().is2xxSuccessful());
        responseBody.put("message", ex.getBody().getDetail());
        responseBody.put("data",null);

        return new ResponseEntity<>(responseBody, HttpStatus.valueOf(ex.getStatusCode().value()));

    }


}
