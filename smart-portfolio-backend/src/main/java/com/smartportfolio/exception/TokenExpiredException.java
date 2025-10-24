package com.smartportfolio.exception;

public class TokenExpiredException extends RuntimeException {
    
    public TokenExpiredException() {
        super("Token süresi dolmuş");
    }
    
    public TokenExpiredException(String message) {
        super(message);
    }
}


