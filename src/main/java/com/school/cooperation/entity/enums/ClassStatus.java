package com.school.cooperation.entity.enums;

/**
 * 班级状态枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum ClassStatus {
    /**
     * 活跃状态
     */
    ACTIVE("ACTIVE", "活跃"),

    /**
     * 非活跃状态
     */
    INACTIVE("INACTIVE", "非活跃");

    private final String code;
    private final String description;

    ClassStatus(String code, String description) {
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
    public static ClassStatus fromCode(String code) {
        for (ClassStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown class status code: " + code);
    }
}