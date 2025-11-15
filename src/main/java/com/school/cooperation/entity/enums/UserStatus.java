package com.school.cooperation.entity.enums;

/**
 * 用户状态枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum UserStatus {
    /**
     * 活跃状态
     */
    ACTIVE("ACTIVE", "活跃"),

    /**
     * 非活跃状态
     */
    INACTIVE("INACTIVE", "非活跃"),

    /**
     * 暂停使用
     */
    SUSPENDED("SUSPENDED", "暂停使用");

    private final String code;
    private final String description;

    UserStatus(String code, String description) {
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
    public static UserStatus fromCode(String code) {
        for (UserStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown status code: " + code);
    }
}