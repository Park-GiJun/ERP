package com.gijun.erp.common.exception.attendance;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;

public class VacationNotFoundException extends BaseException {
    public VacationNotFoundException(String detail) {
        super(ErrorCode.RESOURCE_NOT_FOUND, detail);
    }
}