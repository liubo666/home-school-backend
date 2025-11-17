package com.school.cooperation.config;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.engine.jdbc.internal.FormatStyle;
import org.springframework.stereotype.Component;

import java.util.regex.Pattern;

/**
 * Hibernate SQL日志格式化工具
 * 提供类似MyBatis Plus的SQL日志输出格式
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@Component
public class HibernateSqlLogger {

    private static final Pattern SQL_PATTERN = Pattern.compile("^\\s*(create|alter|drop|truncate|insert|update|delete|select|with)\\b.*$", Pattern.CASE_INSENSITIVE);


    /**
     * 格式化SQL日志输出
     */
    public static void logSql(String sql, Object... parameters) {
        try {
            String formattedSql = formatSql(sql);
            String parametersStr = formatParameters(parameters);

            log.info("SQL: {}", formattedSql);
            if (!parametersStr.isEmpty()) {
                log.info("Parameters: {}", parametersStr);
            }
            log.info("----------------------------------------");
        } catch (Exception e) {
            log.error("格式化SQL日志时出错: {}", e.getMessage());
            log.info("Original SQL: {}", sql);
        }
    }

    /**
     * 格式化SQL语句
     */
    public static String formatSql(String sql) {
        if (sql == null || sql.trim().isEmpty()) {
            return "";
        }

        // 检测SQL类型并格式化
        sql = sql.trim().replaceAll("\\s+", " ");

        if (isDdlStatement(sql)) {
            // DDL语句格式化
            return FormatStyle.DDL.getFormatter().format(sql);
        } else if (isDmlStatement(sql)) {
            // DML语句格式化
            return FormatStyle.BASIC.getFormatter().format(sql);
        } else {
            // 其他语句简单格式化
            return sql;
        }
    }

    /**
     * 格式化参数
     */
    private static String formatParameters(Object[] parameters) {
        if (parameters == null || parameters.length == 0) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parameters.length; i++) {
            if (i > 0) {
                sb.append(", ");
            }
            sb.append("[").append(i + 1).append("] ");
            sb.append(formatParameter(parameters[i]));
        }
        return sb.toString();
    }

    /**
     * 格式化单个参数
     */
    private static String formatParameter(Object param) {
        if (param == null) {
            return "NULL";
        } else if (param instanceof String) {
            return "'" + param + "'";
        } else if (param instanceof java.util.Date) {
            return "TIMESTAMP '" + param + "'";
        } else {
            return param.toString();
        }
    }

    /**
     * 检查是否为DDL语句
     */
    private static boolean isDdlStatement(String sql) {
        String upperSql = sql.toUpperCase();
        return upperSql.startsWith("CREATE") || upperSql.startsWith("ALTER") ||
               upperSql.startsWith("DROP") || upperSql.startsWith("TRUNCATE");
    }

    /**
     * 检查是否为DML语句
     */
    private static boolean isDmlStatement(String sql) {
        return SQL_PATTERN.matcher(sql).matches();
    }

    /**
     * 记录慢SQL
     */
    public static void logSlowSql(String sql, long executionTime, Object... parameters) {
        String formattedSql = formatSql(sql);
        String parametersStr = formatParameters(parameters);

        log.warn("!!! SLOW SQL DETECTED ({}ms) !!!", executionTime);
        log.warn("SQL: {}", formattedSql);
        if (!parametersStr.isEmpty()) {
            log.warn("Parameters: {}", parametersStr);
        }
        log.warn("----------------------------------------");
    }
}