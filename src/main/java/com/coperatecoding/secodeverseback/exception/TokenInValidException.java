package com.coperatecoding.secodeverseback.exception;

public class TokenInValidException extends RuntimeException {
    public TokenInValidException() {
        super();
    }

    public TokenInValidException(String message) {
        super(message);
    }

    public TokenInValidException(String message, Throwable cause) {
        super(message, cause);
    }

    public TokenInValidException(Throwable cause) {
        super(cause);
    }

    protected TokenInValidException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
