package com.school.cooperation.service.impl;

import com.school.cooperation.common.exception.BusinessException;
import com.school.cooperation.common.utils.PageResult;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import com.school.cooperation.repository.UserRepository;
import com.school.cooperation.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务实现类
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByPhone(String phone) {
        return userRepository.findByPhone(phone);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    @Transactional
    public User createUser(User user) {
        // 检查用户名是否已存在
        if (existsByUsername(user.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 检查手机号是否已存在
        if (user.getPhone() != null && existsByPhone(user.getPhone())) {
            throw new BusinessException(400, "手机号已存在");
        }

        // 检查邮箱是否已存在
        if (user.getEmail() != null && existsByEmail(user.getEmail())) {
            throw new BusinessException(400, "邮箱已存在");
        }

        // 加密密码
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // 设置默认值
        user.setStatus(UserStatus.ACTIVE);
        // createdTime和updatedTime会通过JPA生命周期回调自动设置

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public User updateUser(User user) {
        // 检查用户是否存在
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        // 如果修改了用户名，检查是否重复
        if (!existingUser.getUsername().equals(user.getUsername()) && existsByUsername(user.getUsername())) {
            throw new BusinessException(400, "用户名已存在");
        }

        // 如果修改了手机号，检查是否重复
        if (user.getPhone() != null && !existingUser.getPhone().equals(user.getPhone()) && existsByPhone(user.getPhone())) {
            throw new BusinessException(400, "手机号已存在");
        }

        // 如果修改了邮箱，检查是否重复
        if (user.getEmail() != null && !existingUser.getEmail().equals(user.getEmail()) && existsByEmail(user.getEmail())) {
            throw new BusinessException(400, "邮箱已存在");
        }

        // updatedTime会通过JPA生命周期回调自动设置

        return userRepository.save(user);
    }

    @Override
    @Transactional
    public void updateUserStatus(Long userId, UserStatus status) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        user.setStatus(status);
        // updatedTime会通过JPA生命周期回调自动设置

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(Long userId, String oldPassword, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        // 验证旧密码
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(400, "旧密码不正确");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        // updatedTime会通过JPA生命周期回调自动设置

        userRepository.save(user);
    }

    @Override
    @Transactional
    public void resetPassword(Long userId, String newPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        // 更新密码
        user.setPassword(passwordEncoder.encode(newPassword));
        // updatedTime会通过JPA生命周期回调自动设置

        userRepository.save(user);
    }

    @Override
    public List<User> findByRole(UserRole role) {
        return userRepository.findByRole(role);
    }

    @Override
    public List<User> findByStatus(UserStatus status) {
        return userRepository.findByStatus(status);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    @Transactional
    public void activateUser(Long userId) {
        updateUserStatus(userId, UserStatus.ACTIVE);
    }

    @Override
    @Transactional
    public void deactivateUser(Long userId) {
        updateUserStatus(userId, UserStatus.INACTIVE);
    }

    @Override
    @Transactional
    public void deleteUser(Long userId) {
        // 检查用户是否存在
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(404, "用户不存在"));

        // 软删除：设置删除标记
        user.setDeleted(true);
        user.setUpdatedTime(LocalDateTime.now());
        userRepository.save(user);

        log.info("用户删除成功: userId={}, username={}", userId, user.getUsername());
    }

    @Override
    public List<User> findByConditions(String username, String realName, String phone, UserRole role, UserStatus status) {
        return userRepository.findByConditions(username, realName, phone, role, status);
    }

    @Override
    public List<User> getActiveTeachers() {
        return userRepository.findActiveTeachers();
    }

    @Override
    public List<User> getActiveParents() {
        return userRepository.findActiveParents();
    }

    @Override
    public List<User> findByRealNameContaining(String realName) {
        return userRepository.findByRealNameContaining(realName);
    }

    @Override
    @Transactional
    public void updateLastLoginTime(Long userId, LocalDateTime loginTime) {
        userRepository.updateLastLoginTime(userId, loginTime);
    }

    @Override
    @Transactional
    public void batchUpdateUserStatus(List<Long> userIds, UserStatus status) {
        List<User> users = userRepository.findAllById(userIds);

        for (User user : users) {
            user.setStatus(status);
            user.setUpdatedTime(LocalDateTime.now());
        }

        userRepository.saveAll(users);
        log.info("批量更新用户状态成功: userIds={}, status={}", userIds, status);
    }

    @Override
    public List<Object[]> countUsersByRole() {
        return userRepository.countUsersByRole();
    }

    @Override
    public List<Object[]> countUsersByStatus() {
        return userRepository.countUsersByStatus();
    }

    @Override
    public List<User> findInactiveUsers(LocalDateTime threshold) {
        return userRepository.findInactiveUsers(threshold);
    }

    @Override
    public Page<User> findUsersWithPagination(String keyword, UserRole role, UserStatus status, Pageable pageable) {
        return userRepository.findUsersWithPagination(keyword, role, status, pageable);
    }

    @Override
    @Transactional
    public void batchDeleteUsers(List<Long> userIds) {
        List<User> users = userRepository.findAllById(userIds);

        // 记录删除日志
        for (User user : users) {
            log.info("删除用户: userId={}, username={}, role={}", user.getId(), user.getUsername(), user.getRole());
        }

        userRepository.deleteAllById(userIds);
        log.info("批量删除用户成功: userIds={}", userIds);
    }
}