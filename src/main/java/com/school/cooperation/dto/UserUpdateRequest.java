package com.school.cooperation.dto;

import com.school.cooperation.entity.enums.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * 用户更新请求DTO
 *
 * @author homeschool
 * @since 1.0.0
 */
@Data
public class UserUpdateRequest {

    /**
     * 用户名
     */
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    private String username;

    /**
     * 姓名
     */
    @Size(max = 50, message = "姓名长度不能超过50个字符")
    private String realName;

    /**
     * 状态
     */
    private UserStatus status;

    /**
     * 手机号
     */
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    private String email;
}