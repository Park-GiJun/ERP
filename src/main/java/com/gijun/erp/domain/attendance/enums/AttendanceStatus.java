package com.gijun.erp.domain.attendance.enums;


import lombok.Getter;

@Getter
public enum AttendanceStatus {
    CHECKED_IN("출근"),
    CHECKED_OUT("퇴근"),
    LATE("지각"),
    ABSENT("결근"),
    VACATION("휴가"),
    BUSINESS_TRIP("출장");

    private final String description;

    AttendanceStatus(String description) {
        this.description = description;
    }

}