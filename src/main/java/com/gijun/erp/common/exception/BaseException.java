package com.gijun.erp.common.exception;

import lombok.Getter;

@Getter
public class BaseException extends RuntimeException {
    private final ErrorCode errorCode;
    private final String detail;

    public BaseException(ErrorCode errorCode) {
        this(errorCode, null);
    }

    public BaseException(ErrorCode errorCode, String detail) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
        this.detail = detail;
    }
}