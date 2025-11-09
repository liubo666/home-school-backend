package com.school.cooperation.common.constant;

/**
 * 角色常量
 *
 * @author Home School Team
 */
public interface RoleConstant {

    /**
     * 角色前缀
     */
    String ROLE_PREFIX = "ROLE_";

    /**
     * 系统管理员
     */
    String ADMIN = "ADMIN";
    String ROLE_ADMIN = ROLE_PREFIX + ADMIN;

    /**
     * 学校管理员
     */
    String SCHOOL_ADMIN = "SCHOOL_ADMIN";
    String ROLE_SCHOOL_ADMIN = ROLE_PREFIX + SCHOOL_ADMIN;

    /**
     * 教师/班主任
     */
    String TEACHER = "TEACHER";
    String ROLE_TEACHER = ROLE_PREFIX + TEACHER;

    /**
     * 家长
     */
    String PARENT = "PARENT";
    String ROLE_PARENT = ROLE_PREFIX + PARENT;

    /**
     * 系统内置角色列表
     */
    String[] ALL_ROLES = {
        ADMIN,
        SCHOOL_ADMIN,
        TEACHER,
        PARENT
    };

    /**
     * 管理员角色列表
     */
    String[] ADMIN_ROLES = {
        ADMIN,
        SCHOOL_ADMIN
    };

    /**
     * 学校工作人员角色列表
     */
    String[] SCHOOL_STAFF_ROLES = {
        SCHOOL_ADMIN,
        TEACHER
    };

    /**
     * 用户角色列表
     */
    String[] USER_ROLES = {
        TEACHER,
        PARENT
    };
}