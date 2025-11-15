package com.school.cooperation.entity.enums;

/**
 * 通知类型枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum NotificationType {
    /**
     * 系统通知
     */
    SYSTEM("SYSTEM", "系统通知"),

    /**
     * 档案记录通知
     */
    RECORD("RECORD", "档案记录通知"),

    /**
     * 班级通知
     */
    CLASS("CLASS", "班级通知"),

    /**
     * 学生通知
     */
    STUDENT("STUDENT", "学生通知");

    private final String code;
    private final String description;

    NotificationType(String code, String description) {
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
    public static NotificationType fromCode(String code) {
        for (NotificationType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown notification type code: " + code);
    }
}