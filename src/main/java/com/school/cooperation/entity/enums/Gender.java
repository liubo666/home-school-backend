package com.school.cooperation.entity.enums;

/**
 * 性别枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum Gender {
    /**
     * 男性
     */
    MALE("MALE", "男"),

    /**
     * 女性
     */
    FEMALE("FEMALE", "女");

    private final String code;
    private final String description;

    Gender(String code, String description) {
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
    public static Gender fromCode(String code) {
        for (Gender gender : values()) {
            if (gender.getCode().equals(code)) {
                return gender;
            }
        }
        throw new IllegalArgumentException("Unknown gender code: " + code);
    }
}