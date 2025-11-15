package com.school.cooperation.repository;

import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * 用户Repository接口
 *
 * @author system
 * @since 2025-11-15
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long>, JpaSpecificationExecutor<User> {

    /**
     * 根据用户名查询用户
     */
    Optional<User> findByUsername(String username);

    /**
     * 根据用户名和未删除状态查询用户
     */
    Optional<User> findByUsernameAndDeletedFalse(String username);

    /**
     * 根据手机号查询用户
     */
    Optional<User> findByPhone(String phone);

    /**
     * 根据邮箱查询用户
     */
    Optional<User> findByEmail(String email);

    /**
     * 检查用户名是否存在
     */
    boolean existsByUsername(String username);

    /**
     * 检查手机号是否存在
     */
    boolean existsByPhone(String phone);

    /**
     * 检查邮箱是否存在
     */
    boolean existsByEmail(String email);

    /**
     * 检查用户名是否存在（未删除）
     */
    boolean existsByUsernameAndDeletedFalse(String username);

    /**
     * 检查手机号是否存在（未删除）
     */
    boolean existsByPhoneAndDeletedFalse(String phone);

    /**
     * 检查邮箱是否存在（未删除）
     */
    boolean existsByEmailAndDeletedFalse(String email);

    /**
     * 根据角色查询用户列表
     */
    List<User> findByRoleAndDeletedFalse(UserRole role);

    /**
     * 根据角色查询用户列表（不检查删除状态）
     */
    List<User> findByRole(UserRole role);

    /**
     * 根据状态查询用户列表
     */
    List<User> findByStatusAndDeletedFalse(UserStatus status);

    /**
     * 根据状态查询用户列表（不检查删除状态）
     */
    List<User> findByStatus(UserStatus status);

    /**
     * 根据角色和状态查询用户列表
     */
    List<User> findByRoleAndStatusAndDeletedFalse(UserRole role, UserStatus status);

    /**
     * 查询班主任列表
     */
    @Query("SELECT u FROM User u WHERE u.role = 'TEACHER' AND u.status = 'ACTIVE' AND u.deleted = false")
    List<User> findActiveTeachers();

    /**
     * 查询家长列表
     */
    @Query("SELECT u FROM User u WHERE u.role = 'PARENT' AND u.status = 'ACTIVE' AND u.deleted = false")
    List<User> findActiveParents();

    /**
     * 根据真实姓名模糊查询用户
     */
    @Query("SELECT u FROM User u WHERE u.realName LIKE %:realName% AND u.deleted = false")
    List<User> findByRealNameContaining(@Param("realName") String realName);

    /**
     * 根据多个条件查询用户
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:username IS NULL OR u.username LIKE %:username%) AND " +
           "(:realName IS NULL OR u.realName LIKE %:realName%) AND " +
           "(:phone IS NULL OR u.phone LIKE %:phone%) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "u.deleted = false")
    List<User> findByConditions(@Param("username") String username,
                                @Param("realName") String realName,
                                @Param("phone") String phone,
                                @Param("role") UserRole role,
                                @Param("status") UserStatus status);

    /**
     * 更新用户最后登录时间
     */
    @Query("UPDATE User u SET u.lastLoginTime = :loginTime WHERE u.id = :userId")
    void updateLastLoginTime(@Param("userId") Long userId, @Param("loginTime") LocalDateTime loginTime);

    /**
     * 统计各角色用户数量
     */
    @Query("SELECT u.role, COUNT(u) FROM User u WHERE u.deleted = false GROUP BY u.role")
    List<Object[]> countUsersByRole();

    /**
     * 统计各状态用户数量
     */
    @Query("SELECT u.status, COUNT(u) FROM User u WHERE u.deleted = false GROUP BY u.status")
    List<Object[]> countUsersByStatus();

    /**
     * 查询指定时间后注册的用户
     */
    @Query("SELECT u FROM User u WHERE u.createdTime >= :startTime AND u.deleted = false")
    List<User> findUsersRegisteredAfter(@Param("startTime") LocalDateTime startTime);

    /**
     * 查询长期未登录的用户
     */
    @Query("SELECT u FROM User u WHERE (u.lastLoginTime IS NULL OR u.lastLoginTime < :lastLoginThreshold) AND u.status = 'ACTIVE' AND u.deleted = false")
    List<User> findInactiveUsers(@Param("lastLoginThreshold") LocalDateTime lastLoginThreshold);

    /**
     * 分页查询用户
     */
    @Query("SELECT u FROM User u WHERE " +
           "(:keyword IS NULL OR u.username LIKE %:keyword% OR u.realName LIKE %:keyword% OR u.phone LIKE %:keyword%) AND " +
           "(:role IS NULL OR u.role = :role) AND " +
           "(:status IS NULL OR u.status = :status) AND " +
           "u.deleted = false ORDER BY u.createdTime DESC")
    Page<User> findUsersWithPagination(@Param("keyword") String keyword,
                                     @Param("role") UserRole role,
                                     @Param("status") UserStatus status,
                                     Pageable pageable);
}