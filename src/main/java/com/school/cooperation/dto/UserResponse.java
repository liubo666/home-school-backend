package com.school.cooperation.dto;

import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 用户响应DTO
 *
 * @author homeschool
 * @since 1.0.0
 */
@Data
public class UserResponse {

    /**
     * 用户ID
     */
    private Long id;

    /**
     * 用户名
     */
    private String username;

    /**
     * 真实姓名
     */
    private String realName;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像URL
     */
    private String avatar;

    /**
     * 用户角色
     */
    private UserRole role;

    /**
     * 用户状态
     */
    private UserStatus status;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    private LocalDateTime createdTime;

    /**
     * 更新时间
     */
    private LocalDateTime updatedTime;

    /**
     * 角色显示名称
     */
    public String getRoleDisplayName() {
        if (role == null) return null;
        return switch (role) {
            case ADMIN -> "系统管理员";
            case SCHOOL_ADMIN -> "学校管理员";
            case TEACHER -> "教师";
            case PARENT -> "家长";
        };
    }

    /**
     * 状态显示名称
     */
    public String getStatusDisplayName() {
        if (status == null) return null;
        return switch (status) {
            case ACTIVE -> "正常";
            case INACTIVE -> "未激活";
            case SUSPENDED -> "已暂停";
            case LOCKED -> "已锁定";
        };
    }

    /**
     * 从User实体转换为DTO
     */
    public static UserResponse from(com.school.cooperation.entity.User user) {
        if (user == null) return null;

        UserResponse response = new UserResponse();
        response.setId(user.getId());
        response.setUsername(user.getUsername());
        response.setRealName(user.getRealName());
        response.setPhone(user.getPhone());
        response.setEmail(user.getEmail());
        response.setAvatar(user.getAvatar());
        response.setRole(user.getRole());
        response.setStatus(user.getStatus());
        response.setLastLoginTime(user.getLastLoginTime());
        response.setCreatedTime(user.getCreatedTime());
        response.setUpdatedTime(user.getUpdatedTime());
        return response;
    }
}