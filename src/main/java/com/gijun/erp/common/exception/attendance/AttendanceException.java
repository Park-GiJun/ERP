package com.gijun.erp.common.exception.attendance;

import com.gijun.erp.common.exception.BaseException;
import com.gijun.erp.common.exception.ErrorCode;

public class AttendanceException extends BaseException {
    public AttendanceException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AttendanceException(ErrorCode errorCode, String detail) {
        super(errorCode, detail);
    }
}
