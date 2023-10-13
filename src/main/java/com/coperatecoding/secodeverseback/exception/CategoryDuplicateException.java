package com.coperatecoding.secodeverseback.exception;

public class CategoryDuplicateException extends RuntimeException{
    public CategoryDuplicateException() {
        super();
    }

    public CategoryDuplicateException(String message) {
        super(message);
    }

    public CategoryDuplicateException(String message, Throwable cause) {
        super(message, cause);
    }

    public CategoryDuplicateException(Throwable cause) {
        super(cause);
    }

    protected CategoryDuplicateException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
