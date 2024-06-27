package com.hss.authentication.commons.exception;

import lombok.Getter;

@Getter
public class AuthException extends Exception {

    private final String code;

    public AuthException(String message, String code) {
        super(message);
        this.code = code;
    }
}