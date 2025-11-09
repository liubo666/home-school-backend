package com.school.cooperation.common.constant;

/**
 * API常量类
 *
 * @author Home School Team
 */
public interface ApiConstants {

    /**
     * API版本前缀
     */
    String API_V1_PREFIX = "/api/v1";

    /**
     * 认证相关路径
     */
    interface Auth {
        String PREFIX = "/auth";
        String LOGIN = PREFIX + "/login";
        String LOGOUT = PREFIX + "/logout";
        String REGISTER = PREFIX + "/register";
        String REFRESH_TOKEN = PREFIX + "/refresh";
        String CHANGE_PASSWORD = PREFIX + "/change-password";
        String PARENT_LOGIN = PREFIX + "/parent-login";
    }

    /**
     * 用户管理路径
     */
    interface User {
        String PREFIX = "/users";
        String PROFILE = PREFIX + "/profile";
        String UPDATE_PROFILE = PREFIX + "/profile";
        String LIST = PREFIX + "/list";
        String CREATE = PREFIX;
        String UPDATE = PREFIX + "/{id}";
        String DELETE = PREFIX + "/{id}";
        String STATUS = PREFIX + "/{id}/status";
    }

    /**
     * 学生管理路径
     */
    interface Student {
        String PREFIX = "/students";
        String LIST = PREFIX + "/list";
        String CREATE = PREFIX;
        String UPDATE = PREFIX + "/{id}";
        String DELETE = PREFIX + "/{id}";
        String IMPORT = PREFIX + "/import";
        String EXPORT = PREFIX + "/export";
        String RECORDS = PREFIX + "/{id}/records";
    }

    /**
     * 班级管理路径
     */
    interface Class {
        String PREFIX = "/classes";
        String LIST = PREFIX + "/list";
        String CREATE = PREFIX;
        String UPDATE = PREFIX + "/{id}";
        String DELETE = PREFIX + "/{id}";
        String STUDENTS = PREFIX + "/{id}/students";
        String TEACHER = PREFIX + "/{id}/teacher";
    }

    /**
     * 档案记录路径
     */
    interface Record {
        String PREFIX = "/records";
        String LIST = PREFIX + "/list";
        String CREATE = PREFIX;
        String UPDATE = PREFIX + "/{id}";
        String DELETE = PREFIX + "/{id}";
        String IMAGES = PREFIX + "/{id}/images";
    }

    /**
     * 通知管理路径
     */
    interface Notification {
        String PREFIX = "/notifications";
        String LIST = PREFIX + "/list";
        String READ = PREFIX + "/{id}/read";
        String UNREAD_COUNT = PREFIX + "/unread-count";
    }

    /**
     * 文件管理路径
     */
    interface File {
        String PREFIX = "/files";
        String UPLOAD = PREFIX + "/upload";
        String DOWNLOAD = PREFIX + "/{filename}";
        String DELETE = PREFIX + "/{filename}";
    }

    /**
     * 系统管理路径
     */
    interface System {
        String PREFIX = "/system";
        String CONFIG = PREFIX + "/config";
        String LOGS = PREFIX + "/logs";
        String STATISTICS = PREFIX + "/statistics";
        String HEALTH = PREFIX + "/health";
    }
}