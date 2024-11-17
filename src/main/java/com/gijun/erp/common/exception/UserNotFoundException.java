package com.gijun.erp.common.exception;

public class UserNotFoundException extends BaseException {
    public UserNotFoundException(Long userId) {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
