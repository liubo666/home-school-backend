package com.school.cooperation.security;

import com.school.cooperation.entity.User;
import com.school.cooperation.entity.enums.UserStatus;
import com.school.cooperation.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.stream.Collectors;

/**
 * 自定义用户详情服务
 * 负责从数据库加载用户信息并转换为Spring Security的UserDetails对象
 *
 * @author system
 * @since 2025-11-15
 */
@Slf4j
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.debug("正在加载用户信息: {}", username);

        User user = userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> {
                    log.warn("用户不存在: {}", username);
                    return new UsernameNotFoundException("用户不存在: " + username);
                });

        log.debug("找到用户: {}, 角色: {}, 状态: {}", user.getUsername(), user.getRole(), user.getStatus());

        // 检查用户状态
        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            log.warn("用户 {} 状态异常: {}", username, user.getStatus());
            throw new UsernameNotFoundException("用户账户已被停用或暂停");
        }

        // 构建权限列表
        var authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        // 创建用户详情对象
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false) // 账户未过期
                .accountLocked(!user.isAccountNonLocked()) // 账户锁定状态
                .credentialsExpired(false) // 凭证未过期
                .disabled(!user.isEnabled()) // 账户禁用状态
                .build();
    }

    /**
     * 根据用户ID加载用户信息
     */
    @Transactional(readOnly = true)
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        log.debug("正在根据ID加载用户信息: {}", userId);

        User user = userRepository.findById(userId)
                .filter(u -> !u.getDeleted())
                .orElseThrow(() -> {
                    log.warn("用户ID不存在: {}", userId);
                    return new UsernameNotFoundException("用户ID不存在: " + userId);
                });

        // 检查用户状态
        if (!UserStatus.ACTIVE.equals(user.getStatus())) {
            log.warn("用户ID {} 状态异常: {}", userId, user.getStatus());
            throw new UsernameNotFoundException("用户账户已被停用或暂停");
        }

        // 构建权限列表
        var authorities = Collections.singletonList(
                new SimpleGrantedAuthority("ROLE_" + user.getRole().name())
        );

        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getUsername())
                .password(user.getPassword())
                .authorities(authorities)
                .accountExpired(false)
                .accountLocked(!user.isAccountNonLocked())
                .credentialsExpired(false)
                .disabled(!user.isEnabled())
                .build();
    }

    /**
     * 获取用户实体（用于Service层）
     */
    @Transactional(readOnly = true)
    public User getUserEntity(String username) {
        return userRepository.findByUsernameAndDeletedFalse(username)
                .orElseThrow(() -> new UsernameNotFoundException("用户不存在: " + username));
    }

    /**
     * 检查用户是否存在
     */
//    @Transactional(readOnly = true)
//    public boolean userExists(String username) {
//        return userRepository.existsByUsernameAndDeletedFalse(username);
//    }

    /**
     * 检查用户ID是否存在
     */
    @Transactional(readOnly = true)
    public boolean userExists(Long userId) {
        return userRepository.findById(userId)
                .map(user -> !user.getDeleted())
                .orElse(false);
    }
}