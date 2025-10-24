package com.smartportfolio.exception;

public class InvalidCredentialsException extends RuntimeException {
    
    public InvalidCredentialsException() {
        super("Geçersiz kullanıcı adı veya şifre");
    }
    
    public InvalidCredentialsException(String message) {
        super(message);
    }
}


