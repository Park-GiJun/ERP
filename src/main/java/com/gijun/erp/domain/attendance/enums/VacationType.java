package com.gijun.erp.domain.attendance.enums;

public enum VacationType {
    ANNUAL("연차", 1.0),
    HALF_DAY_AM("오전 반차", 0.5),
    HALF_DAY_PM("오후 반차", 0.5),
    SICK("병가", 1.0),
    SPECIAL("특별휴가", 1.0);

    private final String description;
    private final double days;

    VacationType(String description, double days) {
        this.description = description;
        this.days = days;
    }

    public String getDescription() {
        return description;
    }

    public double getDays() {
        return days;
    }
}