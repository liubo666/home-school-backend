package com.school.cooperation.entity.enums;

/**
 * 重要程度枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum Importance {
    /**
     * 高
     */
    HIGH("HIGH", "高"),

    /**
     * 中
     */
    MEDIUM("MEDIUM", "中"),

    /**
     * 低
     */
    LOW("LOW", "低");

    private final String code;
    private final String description;

    Importance(String code, String description) {
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    /**
     * 根据代码获取枚举
     */
    public static Importance fromCode(String code) {
        for (Importance importance : values()) {
            if (importance.getCode().equals(code)) {
                return importance;
            }
        }
        throw new IllegalArgumentException("Unknown importance code: " + code);
    }
}