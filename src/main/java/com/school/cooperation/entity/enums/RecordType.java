package com.school.cooperation.entity.enums;

/**
 * 学生档案记录类型枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum RecordType {
    /**
     * 表扬
     */
    PRAISE("PRAISE", "表扬"),

    /**
     * 批评
     */
    CRITICISM("CRITICISM", "批评"),

    /**
     * 中性记录
     */
    NEUTRAL("NEUTRAL", "中性记录");

    private final String code;
    private final String description;

    RecordType(String code, String description) {
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
    public static RecordType fromCode(String code) {
        for (RecordType type : values()) {
            if (type.getCode().equals(code)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Unknown record type code: " + code);
    }
}