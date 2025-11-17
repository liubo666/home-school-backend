package com.school.cooperation.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * SQL性能监控组件
 * 类似于MyBatis Plus的性能分析插件
 *
 * @author homeschool
 * @since 1.0.0
 */
@Slf4j
@Component
public class SqlPerformanceInterceptor {

    // 慢SQL阈值（毫秒）
    private static final long SLOW_SQL_THRESHOLD = 1000L;

    // SQL执行统计
    private final ConcurrentHashMap<String, SqlStats> sqlStatsMap = new ConcurrentHashMap<>();

    /**
     * 记录SQL执行开始
     */
    public SqlExecution startExecution(String sql) {
        return new SqlExecution(sql, System.currentTimeMillis());
    }

    /**
     * 记录SQL执行结束
     */
    public void endExecution(SqlExecution execution, Object... parameters) {
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - execution.getStartTime();

        // 格式化SQL
        String formattedSql = HibernateSqlLogger.formatSql(execution.getSql());

        // 记录普通日志
        if (log.isInfoEnabled()) {
            String paramsStr = formatParametersForLog(parameters);
            log.info("SQL Executed ({}ms): {}", executionTime, formattedSql);
            if (!paramsStr.isEmpty()) {
                log.info("Parameters: {}", paramsStr);
            }
        }

        // 检查是否为慢SQL
        if (executionTime > SLOW_SQL_THRESHOLD) {
            log.warn("!!! SLOW SQL DETECTED ({}ms) !!!", executionTime);
            log.warn("SQL: {}", formattedSql);
            log.warn("Parameters: {}", formatParametersForLog(parameters));
            log.warn("----------------------------------------");
        }

        // 更新统计信息
        updateStats(formattedSql, executionTime);
    }

    /**
     * 获取SQL统计信息
     */
    public SqlStats getStats(String sql) {
        return sqlStatsMap.get(sql);
    }

    /**
     * 获取所有SQL统计信息
     */
    public ConcurrentHashMap<String, SqlStats> getAllStats() {
        return new ConcurrentHashMap<>(sqlStatsMap);
    }

    /**
     * 清空统计信息
     */
    public void clearStats() {
        sqlStatsMap.clear();
        log.info("SQL统计信息已清空");
    }

    /**
     * 打印SQL统计报告
     */
    public void printStatsReport() {
        if (sqlStatsMap.isEmpty()) {
            log.info("没有SQL执行统计数据");
            return;
        }

        log.info("========== SQL执行统计报告 ==========");
//        sqlStatsMap.entrySet().stream()
//                .sorted((e1, e2) -> e2.getValue().getTotalTime()
//                        .compareTo(e1.getValue().getTotalTime()))
//                .forEach(entry -> {
//                    SqlStats stats = entry.getValue();
//                    log.info("SQL: {}", entry.getKey());
//                    log.info("  执行次数: {}, 总耗时: {}ms, 平均耗时: {}ms, 最大耗时: {}ms",
//                            stats.getCount(),
//                            stats.getTotalTime(),
//                            stats.getAverageTime(),
//                            stats.getMaxTime());
//                });
        log.info("=======================================");
    }

    /**
     * 更新统计信息
     */
    private void updateStats(String sql, long executionTime) {
        sqlStatsMap.compute(sql, (key, stats) -> {
            if (stats == null) {
                stats = new SqlStats();
            }
            stats.addExecution(executionTime);
            return stats;
        });
    }

    /**
     * 格式化参数用于日志输出
     */
    private String formatParametersForLog(Object[] parameters) {
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
    private String formatParameter(Object param) {
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
     * SQL执行记录
     */
    public static class SqlExecution {
        private final String sql;
        private final long startTime;

        public SqlExecution(String sql, long startTime) {
            this.sql = sql;
            this.startTime = startTime;
        }

        public String getSql() {
            return sql;
        }

        public long getStartTime() {
            return startTime;
        }
    }

    /**
     * SQL统计信息
     */
    public static class SqlStats {
        private final AtomicLong count = new AtomicLong(0);
        private final AtomicLong totalTime = new AtomicLong(0);
        private volatile long maxTime = 0;

        public void addExecution(long executionTime) {
            count.incrementAndGet();
            totalTime.addAndGet(executionTime);
            if (executionTime > maxTime) {
                maxTime = executionTime;
            }
        }

        public long getCount() {
            return count.get();
        }

        public long getTotalTime() {
            return totalTime.get();
        }

        public double getAverageTime() {
            long count = this.count.get();
            return count > 0 ? totalTime.get() / (double) count : 0.0;
        }

        public long getMaxTime() {
            return maxTime;
        }
    }
}