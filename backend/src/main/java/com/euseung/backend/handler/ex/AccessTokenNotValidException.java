package com.euseung.backend.handler.ex;

public class AccessTokenNotValidException extends RuntimeException{
    public AccessTokenNotValidException(String message) {
        super(message);
    }
}
