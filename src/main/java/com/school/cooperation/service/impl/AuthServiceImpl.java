package com.school.cooperation.service.impl;

import com.school.cooperation.common.constant.ErrorCode;
import com.school.cooperation.common.exception.BusinessException;
import com.school.cooperation.dto.ChangePasswordRequest;
import com.school.cooperation.dto.LoginRequest;
import com.school.cooperation.dto.LoginResponse;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import com.school.cooperation.repository.UserRepository;
import com.school.cooperation.security.JwtUtils;
import com.school.cooperation.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * 认证服务实现类
 *
 * @author Home School Team
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Override
    @Transactional
    public LoginResponse login(LoginRequest loginRequest) {
        // 查找用户
        User user = userRepository.findByUsernameAndDeletedFalse(loginRequest.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账户已被禁用，请联系管理员");
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 生成Token
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = generateRefreshToken();
        Integer expiresIn = jwtUtils.getExpirationTime();

        return LoginResponse.from(token, refreshToken, expiresIn, user);
    }

    @Override
    @Transactional
    public LoginResponse parentLogin(LoginRequest loginRequest) {
        // 查找家长用户
        User user = userRepository.findByUsernameAndDeletedFalse(loginRequest.getUsername())
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户名或密码错误"));

        // 验证是否为家长角色
        if (user.getRole() != UserRole.PARENT) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "该账户不是家长角色");
        }

        // 验证密码
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR, "用户名或密码错误");
        }

        // 检查用户状态
        if (user.getStatus() != UserStatus.ACTIVE) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "账户已被禁用，请联系管理员");
        }

        // TODO: 验证学生信息（如果需要）
        if (loginRequest.getStudentName() != null || loginRequest.getStudentClass() != null) {
            // 这里可以添加学生信息验证逻辑
            log.info("家长登录验证学生信息: studentName={}, studentClass={}",
                    loginRequest.getStudentName(), loginRequest.getStudentClass());
        }

        // 更新最后登录时间
        user.setLastLoginTime(LocalDateTime.now());
        userRepository.save(user);

        // 生成Token
        String token = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
        String refreshToken = generateRefreshToken();
        Integer expiresIn = jwtUtils.getExpirationTime();

        return LoginResponse.from(token, refreshToken, expiresIn, user);
    }

    @Override
    public void logout(String token) {
        try {
            String username = jwtUtils.getUsernameFromToken(token);
            log.info("用户登出: username={}", username);

            // TODO: 将Token加入黑名单（如果需要）
            // 可以使用Redis存储已登出的Token

        } catch (Exception e) {
            log.warn("登出时解析Token失败: {}", e.getMessage());
        }
    }

    @Override
    @Transactional
    public LoginResponse refreshToken(String refreshToken) {
        try {
            // TODO: 验证刷新Token的有效性
            // 这里可以实现刷新Token的验证逻辑，比如从Redis中验证

            // 临时方案：生成新的Token（实际应该验证refreshToken）
            // 可以从refreshToken中解析出用户信息
            String username = "mock_user"; // 临时实现，应该从refreshToken解析
            User user = userRepository.findByUsernameAndDeletedFalse(username)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在"));

            // 生成新的Token
            String newToken = jwtUtils.generateToken(user.getUsername(), user.getRole().name());
            String newRefreshToken = generateRefreshToken();
            Integer expiresIn = jwtUtils.getExpirationTime();

            return LoginResponse.from(newToken, newRefreshToken, expiresIn, user);

        } catch (Exception e) {
            log.error("刷新Token失败: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.TOKEN_INVALID, "刷新Token失败");
        }
    }

    @Override
    @Transactional
    public void changePassword(String username, ChangePasswordRequest changePasswordRequest) {
        // 验证新密码和确认密码是否一致
        if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "新密码和确认密码不一致");
        }

        // 查找用户
        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在"));

        // 验证原密码
        if (!passwordEncoder.matches(changePasswordRequest.getOldPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PASSWORD_ERROR, "原密码错误");
        }

        // 检查新密码是否与原密码相同
        if (passwordEncoder.matches(changePasswordRequest.getNewPassword(), user.getPassword())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "新密码不能与原密码相同");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);

        log.info("用户修改密码成功: username={}", username);
    }

    @Override
    @Transactional
    public User register(User user, String operatorUsername) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsernameAndDeletedFalse(user.getUsername())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "用户名已存在");
        }

        // 检查手机号是否已存在
        if (user.getPhone() != null && userRepository.existsByPhoneAndDeletedFalse(user.getPhone())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "手机号已存在");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && userRepository.existsByEmailAndDeletedFalse(user.getEmail())) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "邮箱已存在");
        }

        // 设置默认值
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setStatus(UserStatus.ACTIVE);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());
        user.setDeleted(false);

        // 保存用户
        User savedUser = userRepository.save(user);

        log.info("用户注册成功: username={}, role={}, operator={}",
                savedUser.getUsername(), savedUser.getRole(), operatorUsername);

        return savedUser;
    }

    @Override
    public User getCurrentUser(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "用户不存在"));
    }

    /**
     * 生成刷新Token
     */
    private String generateRefreshToken() {
        return UUID.randomUUID().toString().replace("-", "") +
               System.currentTimeMillis();
    }
}