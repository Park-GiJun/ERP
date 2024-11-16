package com.gijun.erp.domain.department;

public enum DepartmentType {
    DIVISION("DIVISION", "본부"),
    DEPARTMENT("DEPARTMENT", "부서"),
    TEAM("TEAM", "팀");

    private final String key;
    private final String title;

    DepartmentType(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public static DepartmentType fromKey(String key) {
        for (DepartmentType type : DepartmentType.values()) {
            if (type.getKey().equals(key)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Invalid DepartmentType key: " + key);
    }
}