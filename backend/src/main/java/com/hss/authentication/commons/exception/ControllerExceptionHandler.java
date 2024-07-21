package com.hss.authentication.commons.exception;

import com.hss.authentication.commons.dto.RestResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@ControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<RestResponseDTO> handleAuthException(AuthException ex) {
        return ResponseEntity.badRequest()
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(new RestResponseDTO(ex.getCode(), ex.getLocalizedMessage(), 400, ex.getMessage(), ex.getCause().toString()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity.badRequest()
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(new RestResponseDTO("unknown", ex.getLocalizedMessage(), 400, ex.getMessage(), Optional.ofNullable(ex.getCause()).orElse(new Throwable()).toString()));
    }
}