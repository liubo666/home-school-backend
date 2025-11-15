package com.school.cooperation.service;

import com.school.cooperation.dto.ChangePasswordRequest;
import com.school.cooperation.dto.LoginRequest;
import com.school.cooperation.dto.LoginResponse;
import com.school.cooperation.dto.PasswordResetRequest;
import com.school.cooperation.entity.User;

/**
 * 认证服务接口
 *
 * @author Home School Team
 */
public interface AuthService {

    /**
     * 用户登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse login(LoginRequest loginRequest);

    /**
     * 家长登录
     *
     * @param loginRequest 登录请求
     * @return 登录响应
     */
    LoginResponse parentLogin(LoginRequest loginRequest);

    /**
     * 用户登出
     *
     * @param token 访问Token
     */
    void logout(String token);

    /**
     * 刷新Token
     *
     * @param refreshToken 刷新Token
     * @return 新的登录响应
     */
    LoginResponse refreshToken(String refreshToken);

    /**
     * 修改密码
     *
     * @param username 用户名
     * @param changePasswordRequest 修改密码请求
     */
    void changePassword(String username, ChangePasswordRequest changePasswordRequest);

    /**
     * 用户注册
     *
     * @param user 用户信息
     * @param operatorUsername 操作人用户名
     * @return 创建的用户
     */
    User register(User user, String operatorUsername);

    /**
     * 获取当前用户信息
     *
     * @param username 用户名
     * @return 用户信息
     */
    User getCurrentUser(String username);

    /**
     * 重置密码
     *
     * @param passwordResetRequest 密码重置请求
     */
    void resetPassword(PasswordResetRequest passwordResetRequest);
}