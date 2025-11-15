package com.school.cooperation.dto;

import com.school.cooperation.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 登录响应DTO
 *
 * @author Home School Team
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "登录响应数据")
public class LoginResponse {

    @Schema(description = "访问Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "刷新Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String refreshToken;

    @Schema(description = "Token过期时间（秒）", example = "7200")
    private Integer expiresIn;

    @Schema(description = "用户信息")
    private UserInfo userInfo;

    /**
     * 用户信息内部类
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    @Schema(description = "用户信息")
    public static class UserInfo {

        @Schema(description = "用户ID", example = "1")
        private Long id;

        @Schema(description = "用户名", example = "admin")
        private String username;

        @Schema(description = "真实姓名", example = "管理员")
        private String realName;

        @Schema(description = "用户角色", example = "ADMIN")
        private String role;

        @Schema(description = "头像URL", example = "http://example.com/avatar.jpg")
        private String avatar;

        @Schema(description = "手机号", example = "13800138000")
        private String phone;

        @Schema(description = "邮箱", example = "admin@example.com")
        private String email;

        @Schema(description = "用户状态", example = "ACTIVE")
        private String status;

        @Schema(description = "最后登录时间", example = "2025-11-15T10:00:00")
        private String lastLoginTime;

        @Schema(description = "创建时间", example = "2025-11-01T10:00:00")
        private String createdTime;

        @Schema(description = "学校ID", example = "school001")
        private String schoolId;

        @Schema(description = "班级ID", example = "class001")
        private String classId;
    }

    /**
     * 从User实体创建LoginResponse
     */
    public static LoginResponse from(String token, String refreshToken, Integer expiresIn, User user) {
        UserInfo userInfo = UserInfo.builder()
                .id(user.getId())
                .username(user.getUsername())
                .realName(user.getRealName())
                .role(user.getRole().name())
                .avatar(user.getAvatar())
                .phone(user.getPhone())
                .email(user.getEmail())
                .status(user.getStatus().name())
                .lastLoginTime(user.getLastLoginTime() != null ? user.getLastLoginTime().toString() : null)
                .createdTime(user.getCreatedTime() != null ? user.getCreatedTime().toString() : null)
                .build();

        return LoginResponse.builder()
                .token(token)
                .refreshToken(refreshToken)
                .expiresIn(expiresIn)
                .userInfo(userInfo)
                .build();
    }
}