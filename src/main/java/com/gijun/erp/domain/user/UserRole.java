package com.gijun.erp.domain.user;

public enum UserRole {
    ROLE_ADMIN("ROLE_ADMIN", "관리자"),
    ROLE_MANAGER("ROLE_MANAGER", "매니저"),
    ROLE_USER("ROLE_USER", "일반 사용자");

    private final String key;
    private final String title;

    UserRole(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public static UserRole fromKey(String key) {
        for (UserRole role : UserRole.values()) {
            if (role.getKey().equals(key)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Invalid UserRole key: " + key);
    }
}
