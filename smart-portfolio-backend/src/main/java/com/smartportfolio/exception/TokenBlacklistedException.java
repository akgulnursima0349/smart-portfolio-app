package com.smartportfolio.exception;

public class TokenBlacklistedException extends RuntimeException {
    
    public TokenBlacklistedException() {
        super("Token geçersiz kılınmış (blacklisted)");
    }
    
    public TokenBlacklistedException(String message) {
        super(message);
    }
}


