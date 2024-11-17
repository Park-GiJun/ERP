package com.gijun.erp.common.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
    // Common Errors
    INVALID_INPUT("C001", "Invalid input provided"),
    RESOURCE_NOT_FOUND("C002", "Requested resource not found"),
    INTERNAL_SERVER_ERROR("C003", "Internal server error"),
    DATA_INTEGRITY_ERROR("C004", "Data integrity violation"),

    // Authentication Errors
    UNAUTHORIZED("A001", "Unauthorized access"),
    INVALID_TOKEN("A002", "Invalid token"),
    TOKEN_EXPIRED("A003", "Token has expired"),

    // Business Logic Errors
    INSUFFICIENT_PERMISSIONS("B001", "Insufficient permissions"),
    DUPLICATE_ENTRY("B002", "Duplicate entry found"),
    INVALID_OPERATION("B003", "Invalid operation"),

    // Resource Errors
    DEPARTMENT_NOT_FOUND("R001", "Department not found"),
    POSITION_NOT_FOUND("R002", "Position not found"),
    USER_NOT_FOUND("R003", "User not found"),

    // Attendance Errors
    ALREADY_CHECKED_IN("AT001", "이미 출근 처리되었습니다"),
    NO_CHECK_IN_RECORD("AT002", "출근 기록이 없습니다"),
    INVALID_CHECKOUT("AT003", "잘못된 퇴근 처리입니다"),
    ATTENDANCE_NOT_FOUND("AT004", "근태 기록을 찾을 수 없습니다");

    private final String code;
    private final String message;
}