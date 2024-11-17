package com.gijun.erp.common.exception.attendance;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;

public class InvalidVacationRequestException extends BaseException {
    public InvalidVacationRequestException(String detail) {
        super(ErrorCode.INVALID_INPUT, detail);
    }
}