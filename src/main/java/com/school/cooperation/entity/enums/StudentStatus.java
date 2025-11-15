package com.school.cooperation.entity.enums;

/**
 * 学生状态枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum StudentStatus {
    /**
     * 在读
     */
    ACTIVE("ACTIVE", "在读"),

    /**
     * 已毕业
     */
    GRADUATED("GRADUATED", "已毕业"),

    /**
     * 已转出
     */
    TRANSFERRED("TRANSFERRED", "已转出");

    private final String code;
    private final String description;

    StudentStatus(String code, String description) {
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
    public static StudentStatus fromCode(String code) {
        for (StudentStatus status : values()) {
            if (status.getCode().equals(code)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Unknown student status code: " + code);
    }
}