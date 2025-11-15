package com.school.cooperation.service;

import com.school.cooperation.common.utils.PageResult;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户服务接口
 *
 * @author homeschool
 * @since 1.0.0
 */
public interface UserService {

    /**
     * 根据ID获取用户
     *
     * @param id 用户ID
     * @return 用户信息
     */
    Optional<User> findById(Long id);

    /**
     * 根据用户名获取用户
     *
     * @param username 用户名
     * @return 用户信息
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据手机号获取用户
     *
     * @param phone 手机号
     * @return 用户信息
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据邮箱获取用户
     *
     * @param email 邮箱
     * @return 用户信息
     */
    Optional<User> findByEmail(String email);

    /**
     * 创建用户
     *
     * @param user 用户信息
     * @return 创建后的用户信息
     */
    User createUser(User user);

    /**
     * 更新用户信息
     *
     * @param user 用户信息
     * @return 更新后的用户信息
     */
    User updateUser(User user);

    /**
     * 修改用户状态
     *
     * @param userId 用户ID
     * @param status 新状态
     */
    void updateUserStatus(Long userId, UserStatus status);

    /**
     * 修改密码
     *
     * @param userId 用户ID
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     */
    void changePassword(Long userId, String oldPassword, String newPassword);

    /**
     * 重置密码
     *
     * @param userId 用户ID
     * @param newPassword 新密码
     */
    void resetPassword(Long userId, String newPassword);

    /**
     * 根据角色获取用户列表
     *
     * @param role 角色
     * @return 用户列表
     */
    List<User> findByRole(UserRole role);

    /**
     * 根据状态获取用户列表
     *
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByStatus(UserStatus status);

    /**
     * 检查用户名是否存在
     *
     * @param username 用户名
     * @return 是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     *
     * @param phone 手机号
     * @return 是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 检查邮箱是否存在
     *
     * @param email 邮箱
     * @return 是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 激活用户
     *
     * @param userId 用户ID
     */
    void activateUser(Long userId);

    /**
     * 禁用用户
     *
     * @param userId 用户ID
     */
    void deactivateUser(Long userId);

    /**
     * 删除用户
     *
     * @param userId 用户ID
     */
    void deleteUser(Long userId);

    /**
     * 根据条件查询用户列表
     *
     * @param username 用户名（模糊查询）
     * @param realName 真实姓名（模糊查询）
     * @param phone 手机号（模糊查询）
     * @param role 角色
     * @param status 状态
     * @return 用户列表
     */
    List<User> findByConditions(String username, String realName, String phone, UserRole role, UserStatus status);

    /**
     * 获取教师用户列表
     *
     * @return 教师用户列表
     */
    List<User> getActiveTeachers();

    /**
     * 获取家长用户列表
     *
     * @return 家长用户列表
     */
    List<User> getActiveParents();

    /**
     * 根据真实姓名模糊查询用户
     *
     * @param realName 真实姓名
     * @return 用户列表
     */
    List<User> findByRealNameContaining(String realName);

    /**
     * 更新用户最后登录时间
     *
     * @param userId 用户ID
     * @param loginTime 登录时间
     */
    void updateLastLoginTime(Long userId, LocalDateTime loginTime);

    /**
     * 批量更新用户状态
     *
     * @param userIds 用户ID列表
     * @param status 新状态
     */
    void batchUpdateUserStatus(List<Long> userIds, UserStatus status);

    /**
     * 获取用户统计信息
     *
     * @return 用户统计信息（按角色分组）
     */
    List<Object[]> countUsersByRole();

    /**
     * 获取用户状态统计
     *
     * @return 用户状态统计
     */
    List<Object[]> countUsersByStatus();

    /**
     * 查询长期未登录的用户
     *
     * @param threshold 时间阈值
     * @return 长期未登录用户列表
     */
    List<User> findInactiveUsers(LocalDateTime threshold);

    /**
     * 分页查询用户
     *
     * @param keyword  搜索关键词
     * @param role     用户角色
     * @param status   用户状态
     * @param pageable 分页参数
     * @return 分页结果
     */
    Page<User> findUsersWithPagination(String keyword, UserRole role, UserStatus status, Pageable pageable);

    /**
     * 批量删除用户
     *
     * @param userIds 用户ID列表
     */
    void batchDeleteUsers(List<Long> userIds);
}