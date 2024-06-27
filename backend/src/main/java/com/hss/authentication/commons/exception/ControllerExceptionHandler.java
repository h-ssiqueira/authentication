package com.hss.authentication.commons.exception;

import com.hss.authentication.commons.dto.RestResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.URI;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_PROBLEM_JSON;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<RestResponseDTO> handleAuthException(AuthException ex) {
        return ResponseEntity.badRequest()
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(new RestResponseDTO(ex.getCode(),ex.getLocalizedMessage(),400,ex.getMessage(),ex.getCause().toString()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<RestResponseDTO> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
        return ResponseEntity.badRequest()
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(new RestResponseDTO(ex.getBody().getType().toString(),ex.getBody().getTitle(),ex.getBody().getStatus(),ex.getBody().getDetail(),Optional.ofNullable(ex.getBody().getInstance()).orElse(URI.create("")).toString()));
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<RestResponseDTO> handleGenericException(Exception ex) {
        return ResponseEntity.badRequest()
                .contentType(APPLICATION_PROBLEM_JSON)
                .body(new RestResponseDTO("unknown",ex.getLocalizedMessage(),400,ex.getMessage(), Optional.ofNullable(ex.getCause()).orElse(new Throwable()).toString()));
    }
}