package com.nebiyu.Kelal.exception;
import com.nebiyu.Kelal.dao.response.Response;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class RegistrationExceptionHandler {
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<Response> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(Response.builder()
                        .error(true)
                        .error_msg(ex.getMessage())
                        .build());
    }
}
