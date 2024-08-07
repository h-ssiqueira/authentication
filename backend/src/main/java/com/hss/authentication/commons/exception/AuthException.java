package com.hss.authentication.commons.exception;

import lombok.Getter;
import org.springframework.security.core.AuthenticationException;

@Getter
public class AuthException extends AuthenticationException {

    private final String code;

    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }
}