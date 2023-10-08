package com.coperatecoding.secodeverseback.exception;

public class UserLockedException extends RuntimeException {
    public UserLockedException() {
        super("정지된 회원입니다");
    }

    public UserLockedException(String message) {
        super(message);
    }

    public UserLockedException(String message, Throwable cause) {
        super(message, cause);
    }

    public UserLockedException(Throwable cause) {
        super(cause);
    }

    protected UserLockedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
