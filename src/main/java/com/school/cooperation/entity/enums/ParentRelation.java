package com.school.cooperation.entity.enums;

/**
 * 家长与学生的关系枚举
 *
 * @author system
 * @since 2025-11-15
 */
public enum ParentRelation {
    /**
     * 父亲
     */
    FATHER("FATHER", "父亲"),

    /**
     * 母亲
     */
    MOTHER("MOTHER", "母亲"),

    /**
     * 祖父
     */
    GRANDFATHER("GRANDFATHER", "祖父"),

    /**
     * 祖母
     */
    GRANDMOTHER("GRANDMOTHER", "祖母"),

    /**
     * 其他
     */
    OTHER("OTHER", "其他");

    private final String code;
    private final String description;

    ParentRelation(String code, String description) {
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
    public static ParentRelation fromCode(String code) {
        for (ParentRelation relation : values()) {
            if (relation.getCode().equals(code)) {
                return relation;
            }
        }
        throw new IllegalArgumentException("Unknown parent relation code: " + code);
    }
}