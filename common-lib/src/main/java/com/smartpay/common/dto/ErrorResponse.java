package com.smartpay.common.dto;

public class ErrorResponse {

    private String errorCode;
    private String errorMessage;

    public ErrorResponse() {}

    public ErrorResponse(String errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorCode() { return errorCode; }
    public String getErrorMessage() { return errorMessage; }
}
