package com.smartportfolio.exception;

public class FileNotFoundException extends RuntimeException {
    
    public FileNotFoundException(String fileName) {
        super(String.format("Dosya bulunamadı: %s", fileName));
    }
    
    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}

