package com.smartportfolio.exception;

public class InvalidTokenException extends RuntimeException {
    
    public InvalidTokenException() {
        super("Geçersiz token");
    }
    
    public InvalidTokenException(String message) {
        super(message);
    }
}


