package com.gijun.erp.domain.attendance.enums;

public enum VacationType {
    ANNUAL("연차"),
    HALF_DAY("반차"),
    SICK("병가"),
    SPECIAL("특별휴가"),
    UNPAID("무급휴가");

    private final String description;

    VacationType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}