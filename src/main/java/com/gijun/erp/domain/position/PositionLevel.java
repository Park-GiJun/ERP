package com.gijun.erp.domain.position;

public enum PositionLevel {
    EXECUTIVE("EXECUTIVE", "임원", 1),
    DIRECTOR("DIRECTOR", "부장", 2),
    MANAGER("MANAGER", "과장", 3),
    ASSISTANT_MANAGER("ASSISTANT_MANAGER", "대리", 4),
    STAFF("STAFF", "사원", 5);

    private final String key;
    private final String title;
    private final int level;

    PositionLevel(String key, String title, int level) {
        this.key = key;
        this.title = title;
        this.level = level;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }

    public int getLevel() {
        return level;
    }

    public static PositionLevel fromKey(String key) {
        for (PositionLevel level : PositionLevel.values()) {
            if (level.getKey().equals(key)) {
                return level;
            }
        }
        throw new IllegalArgumentException("Invalid PositionLevel key: " + key);
    }
}