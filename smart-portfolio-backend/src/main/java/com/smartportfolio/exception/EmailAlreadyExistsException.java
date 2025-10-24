package com.smartportfolio.exception;

public class EmailAlreadyExistsException extends RuntimeException {
    
    public EmailAlreadyExistsException(String email) {
        super(String.format("Bu email adresi zaten kullanılıyor: %s", email));
    }
}


