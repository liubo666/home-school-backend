package com.school.cooperation.entity.enums;

/**
 * 用户角色枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum UserRole {
    /**
     * 系统管理员
     */
    ADMIN("ADMIN", "系统管理员"),

    /**
     * 学校管理员
     */
    SCHOOL_ADMIN("SCHOOL_ADMIN", "学校管理员"),

    /**
     * 班主任
     */
    TEACHER("TEACHER", "班主任"),

    /**
     * 家长
     */
    PARENT("PARENT", "家长");

    private final String code;
    private final String description;

    UserRole(String code, String description) {
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
    public static UserRole fromCode(String code) {
        for (UserRole role : values()) {
            if (role.getCode().equals(code)) {
                return role;
            }
        }
        throw new IllegalArgumentException("Unknown role code: " + code);
    }
}