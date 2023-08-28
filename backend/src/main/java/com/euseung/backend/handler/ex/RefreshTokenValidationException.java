package com.euseung.backend.handler.ex;

import lombok.Getter;

@Getter
public class RefreshTokenValidationException extends RuntimeException{

    private static final long serialVersionUID = 1L;

    public RefreshTokenValidationException(String token, String message) {
        super(String.format("Failed for [%s]: %s", token, message));
    }

    public RefreshTokenValidationException(String message) {
        super(message);
    }
}
