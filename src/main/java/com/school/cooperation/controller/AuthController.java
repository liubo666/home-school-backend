package com.school.cooperation.controller;

import com.school.cooperation.common.constant.ApiConstants;
import com.school.cooperation.common.utils.Result;
import com.school.cooperation.dto.ChangePasswordRequest;
import com.school.cooperation.dto.LoginRequest;
import com.school.cooperation.dto.LoginResponse;
import com.school.cooperation.dto.RefreshTokenRequest;
import com.school.cooperation.entity.User;
import com.school.cooperation.security.JwtUtils;
import com.school.cooperation.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 认证控制器
 *
 * @author Home School Team
 */
@Slf4j
@RestController
@RequestMapping(ApiConstants.API_V1_PREFIX)
@RequiredArgsConstructor
@Tag(name = "认证管理", description = "用户认证相关接口")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    private final AuthService authService;
    private final JwtUtils jwtUtils;

    /**
     * 用户登录
     */
    @PostMapping(ApiConstants.Auth.LOGIN)
    @Operation(summary = "用户登录", description = "通过用户名和密码进行身份认证")
    public Result<LoginResponse> login(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            log.info("用户登录请求: username={}, ip={}",
                    loginRequest.getUsername(), getClientIpAddress(request));

            // 调用认证服务进行登录
            LoginResponse loginResponse = authService.login(loginRequest);

            log.info("用户登录成功: username={}, role={}",
                    loginRequest.getUsername(), loginResponse.getUserInfo().getRole());

            return Result.success("登录成功", loginResponse);

        } catch (Exception e) {
            log.error("用户登录失败: username={}, error={}",
                    loginRequest.getUsername(), e.getMessage(), e);

            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 家长登录
     */
    @PostMapping(ApiConstants.Auth.PARENT_LOGIN)
    @Operation(summary = "家长登录", description = "家长通过账号密码和验证学生信息登录")
    public Result<LoginResponse> parentLogin(
            @Valid @RequestBody LoginRequest loginRequest,
            HttpServletRequest request) {
        try {
            log.info("家长登录请求: username={}, ip={}",
                    loginRequest.getUsername(), getClientIpAddress(request));

            // 调用认证服务进行家长登录
            LoginResponse loginResponse = authService.parentLogin(loginRequest);

            log.info("家长登录成功: username={}", loginRequest.getUsername());

            return Result.success("登录成功", loginResponse);

        } catch (Exception e) {
            log.error("家长登录失败: username={}, error={}",
                    loginRequest.getUsername(), e.getMessage(), e);

            return Result.error("登录失败: " + e.getMessage());
        }
    }

    /**
     * 用户登出
     */
    @PostMapping(ApiConstants.Auth.LOGOUT)
    @Operation(summary = "用户登出", description = "用户退出登录，清除Token")
    public Result<String> logout(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);

            log.info("用户登出: username={}", username);

            // 调用认证服务进行登出处理
            authService.logout(token);

            return Result.success("登出成功");

        } catch (Exception e) {
            log.error("用户登出失败: error={}", e.getMessage(), e);
            return Result.error("登出失败");
        }
    }

    /**
     * 刷新Token
     */
    @PostMapping(ApiConstants.Auth.REFRESH_TOKEN)
    @Operation(summary = "刷新Token", description = "使用刷新Token获取新的访问Token")
    public Result<LoginResponse> refreshToken(
            @Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
        try {
            log.info("Token刷新请求");

            // 调用认证服务刷新Token
            LoginResponse loginResponse = authService.refreshToken(refreshTokenRequest.getRefreshToken());

            log.info("Token刷新成功");

            return Result.success("Token刷新成功", loginResponse);

        } catch (Exception e) {
            log.error("Token刷新失败: error={}", e.getMessage(), e);
            return Result.error("Token刷新失败: " + e.getMessage());
        }
    }

    /**
     * 修改密码
     */
    @PostMapping(ApiConstants.Auth.CHANGE_PASSWORD)
    @Operation(summary = "修改密码", description = "用户修改登录密码")
    public Result<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest changePasswordRequest,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);

            log.info("用户修改密码: username={}", username);

            // 调用认证服务修改密码
            authService.changePassword(username, changePasswordRequest);

            log.info("用户修改密码成功: username={}", username);

            return Result.success("密码修改成功");

        } catch (Exception e) {
            log.error("用户修改密码失败: error={}", e.getMessage(), e);
            return Result.error("密码修改失败: " + e.getMessage());
        }
    }

    /**
     * 用户注册（管理员功能）
     */
    @PostMapping(ApiConstants.Auth.REGISTER)
    @Operation(summary = "用户注册", description = "管理员创建新用户账号")
    public Result<User> register(
            @Valid @RequestBody User user,
            HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String operatorUsername = jwtUtils.getUsernameFromToken(token);

            log.info("管理员创建用户: operator={}, username={}, role={}",
                    operatorUsername, user.getUsername(), user.getRole());

            // 调用认证服务创建用户
            User createdUser = authService.register(user, operatorUsername);

            log.info("用户创建成功: username={}, role={}",
                    createdUser.getUsername(), createdUser.getRole());

            return Result.success("用户创建成功", createdUser);

        } catch (Exception e) {
            log.error("用户创建失败: error={}", e.getMessage(), e);
            return Result.error("用户创建失败: " + e.getMessage());
        }
    }

    /**
     * 验证Token有效性
     */
    @GetMapping(ApiConstants.Auth.PREFIX + "/validate")
    @Operation(summary = "验证Token", description = "验证当前Token是否有效")
    public Result<Boolean> validateToken(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            boolean isValid = jwtUtils.validateToken(token);

            return Result.success("Token验证完成", isValid);

        } catch (Exception e) {
            log.error("Token验证失败: error={}", e.getMessage(), e);
            return Result.success("Token验证完成", false);
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping(ApiConstants.Auth.PREFIX + "/me")
    @Operation(summary = "获取当前用户信息", description = "根据Token获取当前登录用户的信息")
    public Result<User> getCurrentUser(HttpServletRequest request) {
        try {
            String token = jwtUtils.getTokenFromRequest(request);
            String username = jwtUtils.getUsernameFromToken(token);

            // 获取用户信息
            User user = authService.getCurrentUser(username);

            return Result.success("获取用户信息成功", user);

        } catch (Exception e) {
            log.error("获取用户信息失败: error={}", e.getMessage(), e);
            return Result.error("获取用户信息失败: " + e.getMessage());
        }
    }

    /**
     * 获取客户端IP地址
     */
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty() && !"unknown".equalsIgnoreCase(xForwardedFor)) {
            return xForwardedFor.split(",")[0].trim();
        }

        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty() && !"unknown".equalsIgnoreCase(xRealIp)) {
            return xRealIp;
        }

        return request.getRemoteAddr();
    }
}