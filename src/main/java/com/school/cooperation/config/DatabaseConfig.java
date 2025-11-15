package com.school.cooperation.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;

/**
 * 数据库配置类（优化版）
 * 核心：精简配置、避免冲突、适配MySQL、增强可维护性
 */
@Slf4j
@Configuration
@EnableJpaRepositories(basePackages = "com.school.cooperation.repository")
@EntityScan(basePackages = "com.school.cooperation.entity")
@EnableTransactionManagement
public class DatabaseConfig {

    // 从配置文件注入核心参数（其他非核心参数通过默认值或代码内合理设置）
    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    // 驱动类名通过URL自动推断（MySQL 8.0+无需显式指定）
    @Value("${spring.datasource.driver-class-name:com.mysql.cj.jdbc.Driver}")
    private String driverClassName;

    // SSL配置（从环境变量读取，支持灵活配置）
    @Value("${spring.datasource.useSSL:false}")
    private String useSSL;

    /**
     * 主数据源配置（HikariCP）
     * 仅在没有其他DataSource Bean时生效（避免冲突）
     */
    @Bean
    @Primary
    @ConditionalOnMissingBean(DataSource.class)
    public DataSource primaryDataSource() {
        HikariConfig config = new HikariConfig();

        // 1. 基础连接配置
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setPoolName("HomeSchool-Main-Pool"); // 连接池名称（便于监控）

        // 2. 连接池核心参数（根据业务并发量调整）
        // 中等并发场景：最小空闲5，最大连接20（避免资源浪费）
        config.setMinimumIdle(5);
        config.setMaximumPoolSize(20);
        config.setAutoCommit(true); // 默认自动提交（符合多数业务场景）

        // 3. 连接生命周期管理（避免连接失效）
        config.setIdleTimeout(600000); // 空闲10分钟后回收
        config.setMaxLifetime(1800000); // 连接最长存活30分钟（小于MySQL wait_timeout）
        config.setConnectionTimeout(30000); // 获取连接超时30秒（避免长期阻塞）

        // 4. 连接校验与初始化（确保连接可用）
        config.setConnectionTestQuery("SELECT 1"); // 心跳检测（简单高效）
        config.setValidationTimeout(5000); // 校验超时5秒
        // 字符集初始化（统一用utf8mb4，支持emoji等特殊字符）
        config.setConnectionInitSql("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");

        // 5. MySQL性能优化参数（针对性调优）
        config.addDataSourceProperty("cachePrepStmts", "true"); // 缓存预处理语句
        config.addDataSourceProperty("prepStmtCacheSize", "250"); // 缓存大小
        config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048"); // 最大SQL长度
        config.addDataSourceProperty("useServerPrepStmts", "true"); // 使用服务器端预处理
        config.addDataSourceProperty("rewriteBatchedStatements", "true"); // 批量插入优化
        config.addDataSourceProperty("useLocalSessionState", "true"); // 本地会话状态
        config.addDataSourceProperty("elideSetAutoCommits", "true"); // 减少自动提交语句

        // 6. 安全与兼容性配置
        config.addDataSourceProperty("useSSL", useSSL); // 根据环境配置SSL
        config.addDataSourceProperty("allowPublicKeyRetrieval", "true"); // 兼容旧版本MySQL
        config.addDataSourceProperty("serverTimezone", "Asia/Shanghai"); // 时区明确指定

        // 7. 监控与诊断（按需开启）
        config.setLeakDetectionThreshold(60000); // 1分钟连接泄漏检测（便于排查长事务）
        config.setRegisterMbeans(false); // 非监控环境关闭JMX（减少资源占用）

        // 初始化日志（打印关键信息，避免敏感信息泄露）
        log.info("主数据源初始化完成 | 连接池: {} | 最大连接数: {} | 数据库: {}",
                config.getPoolName(), config.getMaximumPoolSize(),
                jdbcUrl.replaceAll("://.*@", "://***@***")); // 隐藏用户名密码

        return new HikariDataSource(config);
    }

    /**
     * 健康检查数据源（复用主数据源配置，减少资源浪费）
     * 仅在需要独立健康检查时保留（多数场景可删除）
     */
    @Bean
    public DataSource healthCheckDataSource() {
        HikariConfig config = new HikariConfig();
        // 复用主数据源的核心配置
        config.setJdbcUrl(jdbcUrl);
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        // 健康检查连接池参数（最小化资源占用）
        config.setPoolName("HomeSchool-HealthCheck-Pool");
        config.setMinimumIdle(1);
        config.setMaximumPoolSize(2);
        config.setConnectionTimeout(5000); // 健康检查超时5秒（快速失败）
        config.setConnectionTestQuery("SELECT 1"); // 简单心跳检测
        // 复用字符集和时区配置
        config.addDataSourceProperty("serverTimezone", "Asia/Shanghai");
        config.setConnectionInitSql("SET NAMES utf8mb4 COLLATE utf8mb4_unicode_ci");

        return new HikariDataSource(config);
    }
}