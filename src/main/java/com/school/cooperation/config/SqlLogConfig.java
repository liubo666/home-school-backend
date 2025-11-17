package com.school.cooperation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

/**
 * SQL日志配置类
 *
 * @author homeschool
 * @since 1.0.0
 */
@Configuration
@EnableJpaAuditing(auditorAwareRef = "auditorProvider")
public class SqlLogConfig {

    /**
     * 审计功能配置
     */
    @Bean
    public AuditorAware<String> auditorProvider() {
        return new AuditorAwareImpl();
    }

    /**
     * 审计信息提供者实现
     */
    public static class AuditorAwareImpl implements AuditorAware<String> {
        @Override
        public Optional<String> getCurrentAuditor() {
            try {
                // 尝试从Spring Security上下文获取当前用户
                Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
                if (authentication != null && authentication.isAuthenticated()) {
                    String username = authentication.getName();
                    return Optional.ofNullable(username);
                }
            } catch (Exception e) {
                // 如果获取失败，返回系统用户
            }
            return Optional.of("system");
        }
    }
}