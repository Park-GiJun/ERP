package com.gijun.erp.domain.user;

public enum UserStatus {
    ACTIVE("ACTIVE", "활성"),
    INACTIVE("INACTIVE", "비활성"),
    SUSPENDED("SUSPENDED", "정지"),
    PENDING("PENDING", "대기");

    private final String key;
    private final String title;

    UserStatus(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public static UserStatus fromKey(String key) {
        for (UserStatus status : UserStatus.values()) {
            if (status.getKey().equals(key)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid UserStatus key: " + key);
    }
}
