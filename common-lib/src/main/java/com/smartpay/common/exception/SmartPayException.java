package com.smartpay.common.exception;

public class SmartPayException extends RuntimeException {
    private final GlobalErrorCode code;

    public SmartPayException(GlobalErrorCode code, String message) {
        super(message);
        this.code = code;
    }

    public GlobalErrorCode getCode() {
        return code;
    }
}
