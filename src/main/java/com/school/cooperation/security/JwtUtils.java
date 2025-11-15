package com.school.cooperation.security;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import jakarta.servlet.http.HttpServletRequest;

/**
 * JWT工具类
 * 提供便捷的JWT操作方法
 *
 * @author system
 * @since 2025-11-15
 */
@Slf4j
@Component
public class JwtUtils {

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    /**
     * 获取当前认证用户的用户名
     */
    public static String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            return authentication.getName();
        }
        return null;
    }

    /**
     * 获取当前认证用户ID（需要从数据库查询）
     * 注意：这个方法需要调用Repository，所以不能是静态的
     */
    public Long getCurrentUserId() {
        String username = getCurrentUsername();
        if (username == null) {
            return null;
        }

        // 这里应该通过Service层查询用户ID
        // 为了简化，暂时返回null，实际使用时需要注入UserService
        log.warn("getCurrentUserId方法需要通过UserService实现");
        return null;
    }

    /**
     * 检查当前用户是否已认证
     */
    public static boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() &&
               !"anonymousUser".equals(authentication.getName());
    }

    /**
     * 检查当前用户是否具有指定角色
     */
    public static boolean hasRole(String role) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            return false;
        }

        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_" + role));
    }

    /**
     * 检查当前用户是否为管理员
     */
    public static boolean isAdmin() {
        return hasRole("ADMIN");
    }

    /**
     * 检查当前用户是否为学校管理员
     */
    public static boolean isSchoolAdmin() {
        return hasRole("SCHOOL_ADMIN");
    }

    /**
     * 检查当前用户是否为教师
     */
    public static boolean isTeacher() {
        return hasRole("TEACHER");
    }

    /**
     * 检查当前用户是否为家长
     */
    public static boolean isParent() {
        return hasRole("PARENT");
    }

    /**
     * 生成访问token
     */
    public String generateAccessToken(String username) {
        return jwtTokenProvider.generateToken(username, null);
    }

    /**
     * 生成访问token（带额外声明）
     */
    public String generateAccessToken(String username, java.util.Map<String, Object> extraClaims) {
        return jwtTokenProvider.generateToken(username, extraClaims);
    }

    /**
     * 生成Token（带角色）
     */
    public String generateToken(String username, String role) {
        java.util.Map<String, Object> extraClaims = new java.util.HashMap<>();
        extraClaims.put("role", role);
        return jwtTokenProvider.generateToken(username, extraClaims);
    }

    /**
     * 获取Token过期时间（秒）
     */
    public Integer getExpirationTime() {
        return jwtTokenProvider.getExpirationTimeInSeconds();
    }

    /**
     * 从请求中获取Token
     */
    public String getTokenFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        return extractTokenFromHeader(authHeader);
    }

    /**
     * 生成刷新token
     */
    public String generateRefreshToken(String username) {
        return jwtTokenProvider.generateRefreshToken(username);
    }

    /**
     * 验证token
     */
    public boolean validateToken(String token) {
        try {
            // 这里需要UserDetails，简化处理
            return jwtTokenProvider.validateTokenFormat(token);
        } catch (Exception e) {
            log.error("Token验证失败", e);
            return false;
        }
    }

    /**
     * 从token中获取用户名
     */
    public String getUsernameFromToken(String token) {
        try {
            return jwtTokenProvider.getUsernameFromToken(token);
        } catch (Exception e) {
            log.error("从Token获取用户名失败", e);
            return null;
        }
    }

    /**
     * 检查token是否过期
     */
    public boolean isTokenExpired(String token) {
        return jwtTokenProvider.isTokenExpired(token);
    }

    /**
     * 刷新token
     */
    public String refreshToken(String token) {
        try {
            return jwtTokenProvider.refreshToken(token);
        } catch (Exception e) {
            log.error("Token刷新失败", e);
            return null;
        }
    }

    /**
     * 获取token剩余有效时间（毫秒）
     */
    public Long getTokenRemainingTime(String token) {
        return jwtTokenProvider.getTokenRemainingTime(token);
    }

    /**
     * 从请求头中提取token
     */
    public String extractTokenFromHeader(String authHeader) {
        return jwtTokenProvider.extractTokenFromHeader(authHeader);
    }

    /**
     * 清除当前安全上下文
     */
    public static void clearSecurityContext() {
        SecurityContextHolder.clearContext();
    }

    /**
     * 获取当前认证对象
     */
    public static Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * 设置认证信息到安全上下文
     */
    public static void setAuthentication(Authentication authentication) {
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}