package com.coperatecoding.secodeverseback.exception;

public class UserInfoDuplicatedException extends RuntimeException {
    public UserInfoDuplicatedException() {
        super();
    }

    public UserInfoDuplicatedException(String message) {
        super(message);
    }

    public UserInfoDuplicatedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserInfoDuplicatedException(Throwable cause) {
        super(cause);
    }

    protected UserInfoDuplicatedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
