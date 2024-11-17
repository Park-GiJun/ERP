package com.gijun.erp.domain.attendance.enums;

import lombok.Getter;

@Getter
public enum ApprovalStatus {
    PENDING("대기"),
    APPROVED("승인"),
    REJECTED("거절");

    private final String description;

    ApprovalStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}