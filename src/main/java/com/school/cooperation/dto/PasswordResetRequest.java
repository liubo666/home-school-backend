package com.school.cooperation.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 密码重置请求DTO
 *
 * @author homeschool
 * @since 1.0.0
 */
@Data
public class PasswordResetRequest {

    /**
     * 用户名或手机号或邮箱
     */
    @NotBlank(message = "用户名不能为空")
    private String username;

    /**
     * 验证码（如果有）
     */
    private String verificationCode;

    /**
     * 新密码
     */
    @NotBlank(message = "新密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20个字符之间")
    private String newPassword;

    /**
     * 确认密码
     */
    @NotBlank(message = "确认密码不能为空")
    private String confirmPassword;
}