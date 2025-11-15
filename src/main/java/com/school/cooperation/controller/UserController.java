package com.school.cooperation.controller;

import com.school.cooperation.common.constant.RoleConstant;
import com.school.cooperation.common.utils.PageResult;
import com.school.cooperation.common.utils.Result;
import com.school.cooperation.dto.UserCreateRequest;
import com.school.cooperation.dto.UserUpdateRequest;
import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserRole;
import com.school.cooperation.entity.enums.UserStatus;
import com.school.cooperation.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户管理控制器
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
@Validated
@Tag(name = "用户管理", description = "用户管理相关接口")
public class UserController {

    private final UserService userService;

    /**
     * 分页获取用户列表
     */
    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "分页获取用户列表", description = "分页获取用户列表，支持关键词搜索和筛选")
    public Result<Page<User>> getUsers(
            @Parameter(description = "页码", example = "1") @RequestParam(defaultValue = "1") int page,
            @Parameter(description = "每页大小", example = "20") @RequestParam(defaultValue = "20") int pageSize,
            @Parameter(description = "搜索关键词") @RequestParam(required = false) String keyword,
            @Parameter(description = "用户角色") @RequestParam(required = false) UserRole role,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatus status) {

        // 创建Pageable对象
        Pageable pageable = PageRequest.of(page - 1, pageSize); // Spring Data的页码从0开始
        Page<User> result = userService.findUsersWithPagination(keyword, role, status, pageable);
        return Result.success(result);
    }

    /**
     * 批量删除用户
     */
    @DeleteMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量删除用户", description = "批量删除多个用户")
    public Result<Void> batchDeleteUsers(
            @Parameter(description = "用户ID列表", required = true)
            @RequestParam List<Long> userIds) {

        userService.batchDeleteUsers(userIds);
        return Result.success();
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/current")
    @Operation(summary = "获取当前用户信息", description = "获取当前登录用户的详细信息")
    public Result<User> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        User user = userService.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return Result.success(user);
    }

    /**
     * 根据ID获取用户信息
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "根据ID获取用户信息", description = "根据用户ID获取用户详细信息")
    public Result<User> getUserById(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {

        User user = userService.findById(id)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        return Result.success(user);
    }

    /**
     * 创建用户
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "创建用户", description = "创建新用户")
    public Result<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setRealName(request.getRealName());
        user.setRole(request.getRole());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());

        User createdUser = userService.createUser(user);
        return Result.success(createdUser);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN') or #id == authentication.principal.id")
    @Operation(summary = "更新用户信息", description = "更新指定用户的信息")
    public Result<User> updateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id,
            @Valid @RequestBody UserUpdateRequest request) {

        User user = new User();
        user.setId(id);
        user.setUsername(request.getUsername());
        user.setRealName(request.getRealName());
        user.setStatus(request.getStatus());
        user.setPhone(request.getPhone());
        user.setEmail(request.getEmail());

        User updatedUser = userService.updateUser(user);
        return Result.success(updatedUser);
    }

    /**
     * 删除用户
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "删除用户", description = "删除指定用户")
    public Result<Void> deleteUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {

        userService.deleteUser(id);
        return Result.success();
    }

    /**
     * 根据角色获取用户列表
     */
    @GetMapping("/role/{role}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "根据角色获取用户列表", description = "根据用户角色获取用户列表")
    public Result<List<User>> getUsersByRole(
            @Parameter(description = "用户角色", required = true)
            @PathVariable UserRole role) {

        List<User> users = userService.findByRole(role);
        return Result.success(users);
    }

    /**
     * 根据状态获取用户列表
     */
    @GetMapping("/status/{status}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "根据状态获取用户列表", description = "根据用户状态获取用户列表")
    public Result<List<User>> getUsersByStatus(
            @Parameter(description = "用户状态", required = true)
            @PathVariable UserStatus status) {

        List<User> users = userService.findByStatus(status);
        return Result.success(users);
    }

    /**
     * 获取所有管理员用户
     */
    @GetMapping("/admins")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取管理员用户列表", description = "获取所有管理员用户")
    public Result<List<User>> getAdminUsers() {
        List<User> users = userService.findByRole(UserRole.ADMIN);
        return Result.success(users);
    }

    /**
     * 获取所有教师用户
     */
    @GetMapping("/teachers")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取教师用户列表", description = "获取所有教师用户")
    public Result<List<User>> getTeacherUsers() {
        List<User> users = userService.findByRole(UserRole.TEACHER);
        return Result.success(users);
    }

    /**
     * 获取所有家长用户
     */
    @GetMapping("/parents")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取家长用户列表", description = "获取所有家长用户")
    public Result<List<User>> getParentUsers() {
        List<User> users = userService.findByRole(UserRole.PARENT);
        return Result.success(users);
    }

    /**
     * 激活用户
     */
    @PutMapping("/{id}/activate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "激活用户", description = "激活指定用户")
    public Result<Void> activateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {

        userService.activateUser(id);
        return Result.success();
    }

    /**
     * 禁用用户
     */
    @PutMapping("/{id}/deactivate")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "禁用用户", description = "禁用指定用户")
    public Result<Void> deactivateUser(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id) {

        userService.deactivateUser(id);
        return Result.success();
    }

    /**
     * 重置用户密码
     */
    @PutMapping("/{id}/reset-password")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "重置用户密码", description = "重置指定用户的密码")
    public Result<Void> resetPassword(
            @Parameter(description = "用户ID", required = true)
            @PathVariable Long id,
            @RequestParam String newPassword) {

        userService.resetPassword(id, newPassword);
        return Result.success();
    }

    /**
     * 搜索用户
     */
    @GetMapping("/search")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "搜索用户", description = "根据条件搜索用户")
    public Result<List<User>> searchUsers(
            @Parameter(description = "用户名") @RequestParam(required = false) String username,
            @Parameter(description = "真实姓名") @RequestParam(required = false) String realName,
            @Parameter(description = "手机号") @RequestParam(required = false) String phone,
            @Parameter(description = "用户角色") @RequestParam(required = false) UserRole role,
            @Parameter(description = "用户状态") @RequestParam(required = false) UserStatus status) {

        List<User> users = userService.findByConditions(username, realName, phone, role, status);
        return Result.success(users);
    }

    /**
     * 根据真实姓名搜索用户
     */
    @GetMapping("/search/by-name")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "根据真实姓名搜索用户", description = "根据真实姓名模糊搜索用户")
    public Result<List<User>> searchUsersByName(
            @Parameter(description = "真实姓名", required = true)
            @RequestParam String realName) {

        List<User> users = userService.findByRealNameContaining(realName);
        return Result.success(users);
    }

    /**
     * 批量更新用户状态
     */
    @PutMapping("/batch-status")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "批量更新用户状态", description = "批量更新多个用户的状态")
    public Result<Void> batchUpdateUserStatus(
            @Parameter(description = "用户ID列表", required = true)
            @RequestParam List<Long> userIds,
            @Parameter(description = "用户状态", required = true)
            @RequestParam UserStatus status) {

        userService.batchUpdateUserStatus(userIds, status);
        return Result.success();
    }

    /**
     * 获取用户统计信息
     */
    @GetMapping("/statistics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户统计信息", description = "获取按角色分组的用户统计")
    public Result<List<Object[]>> getUserStatisticsByRole() {
        List<Object[]> statistics = userService.countUsersByRole();
        return Result.success(statistics);
    }

    /**
     * 获取用户状态统计
     */
    @GetMapping("/status-statistics")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取用户状态统计", description = "获取按状态分组的用户统计")
    public Result<List<Object[]>> getUserStatisticsByStatus() {
        List<Object[]> statistics = userService.countUsersByStatus();
        return Result.success(statistics);
    }

    /**
     * 获取长期未登录用户
     */
    @GetMapping("/inactive-users")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "获取长期未登录用户", description = "获取超过指定时间未登录的用户")
    public Result<List<User>> getInactiveUsers(
            @Parameter(description = "天数阈值", required = true)
            @RequestParam Integer days) {

        LocalDateTime threshold = LocalDateTime.now().minusDays(days);
        List<User> users = userService.findInactiveUsers(threshold);
        return Result.success(users);
    }
}