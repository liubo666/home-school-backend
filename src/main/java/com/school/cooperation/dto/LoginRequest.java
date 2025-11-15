package com.school.cooperation.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * 登录请求DTO
 *
 * @author Home School Team
 */
@Data
@Schema(description = "登录请求参数")
public class LoginRequest {

    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 50, message = "用户名长度必须在3-50个字符之间")
    @Schema(description = "用户名", example = "admin", required = true)
    private String username;

    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 100, message = "密码长度必须在6-100个字符之间")
    @Schema(description = "密码", example = "123456", required = true)
    private String password;

    @Schema(description = "学生姓名（家长登录时使用）", example = "张三")
    private String studentName;

    @Schema(description = "学生班级（家长登录时使用）", example = "三年级一班")
    private String studentClass;
}