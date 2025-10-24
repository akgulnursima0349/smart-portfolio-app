package com.smartportfolio.exception;

public class UsernameAlreadyExistsException extends RuntimeException {
    
    public UsernameAlreadyExistsException(String username) {
        super(String.format("Bu kullanıcı adı zaten kullanılıyor: %s", username));
    }
}


