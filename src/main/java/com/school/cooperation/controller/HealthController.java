package com.school.cooperation.controller;

import com.school.cooperation.common.utils.Result;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

/**
 * 健康检查控制器
 *
 * @author Home School Team
 */
@Slf4j
@RestController
@RequestMapping("/health")
@Tag(name = "健康检查", description = "系统健康检查相关接口")
public class HealthController {

    @Autowired
    private DataSource dataSource;

    /**
     * 系统健康检查
     */
    @GetMapping
    @Operation(summary = "系统健康检查", description = "检查系统各组件运行状态")
    public Result<Map<String, Object>> health() {
        Map<String, Object> health = new HashMap<>();

        try {
            // 检查数据库连接
            try (Connection connection = dataSource.getConnection()) {
                health.put("database", "UP");
                // 不暴露完整的数据库URL，只显示必要信息
                String dbUrl = connection.getMetaData().getURL();
                String maskedUrl = dbUrl.replaceAll("://.*@", "://***@***");
                health.put("database_info", maskedUrl);
            }
        } catch (SQLException e) {
            log.error("数据库连接检查失败", e);
            health.put("database", "DOWN");
            health.put("database_error", "数据库连接异常"); // 不暴露具体错误信息
        } catch (Exception e) {
            log.error("数据库连接检查失败", e);
            health.put("database", "DOWN");
            health.put("database_error", "系统异常"); // 通用错误信息
        }

        // 应用信息
        health.put("application", "UP");
        health.put("timestamp", LocalDateTime.now());
        health.put("version", "1.0.0");
        health.put("environment", System.getProperty("spring.profiles.active", "default"));

        return Result.success("系统运行正常", health);
    }

    /**
     * 简单的ping接口
     */
    @GetMapping("/ping")
    @Operation(summary = "Ping接口", description = "简单的ping接口，用于检查服务是否可用")
    public Result<String> ping() {
        return Result.success("pong");
    }
}