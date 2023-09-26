package com.nebiyu.Kelal.exception;

import com.nebiyu.Kelal.response.AuthorizationResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ControllerAdvice
public class RegistrationExceptionHandler {
    @ExceptionHandler(RegistrationException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<AuthorizationResponse> handleRegistrationException(RegistrationException ex) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(AuthorizationResponse.builder()
                        .error(true)
                        .error_msg(ex.getMessage())
                        .build());
    }
}
