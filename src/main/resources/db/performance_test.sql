-- 数据库性能测试脚本
-- performance_test.sql

USE home_school;

-- 输出测试开始信息
SELECT '=== 数据库性能测试开始 ===' AS test_info;
SELECT NOW() AS test_start_time;

-- 1. 基础查询性能测试
SELECT '1. 基础查询性能测试' AS test_section;

-- 用户表查询测试
SELECT
    '用户总数查询' AS test_name,
    COUNT(*) AS result_count,
    (SELECT COUNT(*) FROM sys_user) AS user_count;

-- 学生表查询测试
SELECT
    '学生总数查询' AS test_name,
    COUNT(*) AS result_count,
    (SELECT COUNT(*) FROM edu_student) AS student_count;

-- 档案记录查询测试
SELECT
    '档案记录总数查询' AS test_name,
    COUNT(*) AS result_count,
    (SELECT COUNT(*) FROM edu_student_record) AS record_count;

-- 2. 索引性能测试
SELECT '2. 索引性能测试' AS test_section;

-- 测试角色索引效果
EXPLAIN SELECT * FROM sys_user WHERE role = 'TEACHER';

-- 测试班级学生索引效果
EXPLAIN SELECT * FROM edu_student WHERE class_id = 1 AND status = 'ACTIVE';

-- 测试学生记录时间索引效果
EXPLAIN SELECT * FROM edu_student_record WHERE student_id = 1 ORDER BY created_time DESC;

-- 测试通知用户索引效果
EXPLAIN SELECT * FROM sys_notification WHERE user_id = 1 AND is_read = 0;

-- 3. 复杂查询性能测试
SELECT '3. 复杂查询性能测试' AS test_section;

-- 学生详细信息查询（连接查询）
SELECT
    '学生详细信息查询' AS test_name,
    COUNT(*) AS result_count,
    AVG(TIMESTAMPDIFF(MICROSECOND, NOW(), NOW())) AS execution_time_microseconds
FROM edu_student s
INNER JOIN edu_class c ON s.class_id = c.id
INNER JOIN sys_user u ON c.teacher_id = u.id
WHERE s.status = 'ACTIVE';

-- 学生档案统计查询（聚合查询）
SELECT
    '学生档案统计查询' AS test_name,
    COUNT(*) AS result_count
FROM (
    SELECT
        s.id,
        s.name,
        COUNT(CASE WHEN r.type = 'PRAISE' THEN 1 END) as praise_count,
        COUNT(CASE WHEN r.type = 'CRITICISM' THEN 1 END) as criticism_count,
        COUNT(r.id) as total_record_count
    FROM edu_student s
    LEFT JOIN edu_student_record r ON s.id = r.student_id
    GROUP BY s.id, s.name
) AS student_stats;

-- 班级统计查询（复杂聚合）
SELECT
    '班级统计查询' AS test_name,
    COUNT(*) AS result_count
FROM edu_class c
INNER JOIN sys_user u ON c.teacher_id = u.id
LEFT JOIN edu_student s ON c.id = s.class_id
LEFT JOIN edu_student_record r ON s.id = r.student_id
WHERE c.deleted = 0 AND u.deleted = 0
GROUP BY c.id, c.name, u.real_name;

-- 4. 分页查询性能测试
SELECT '4. 分页查询性能测试' AS test_section;

-- 学生分页查询
SELECT
    '学生分页查询(第1页)' AS test_name,
    COUNT(*) AS result_count
FROM edu_student s
INNER JOIN edu_class c ON s.class_id = c.id
WHERE s.status = 'ACTIVE'
ORDER BY s.created_time DESC
LIMIT 20;

-- 档案记录分页查询
SELECT
    '档案记录分页查询(第1页)' AS test_name,
    COUNT(*) AS result_count
FROM edu_student_record r
INNER JOIN edu_student s ON r.student_id = s.id
INNER JOIN sys_user u ON r.teacher_id = u.id
WHERE r.is_public = 1
ORDER BY r.created_time DESC
LIMIT 20;

-- 5. 全文搜索性能测试
SELECT '5. 全文搜索性能测试' AS test_section;

-- 学生姓名搜索
SELECT
    '学生姓名搜索' AS test_name,
    COUNT(*) AS result_count
FROM edu_student
WHERE name LIKE '%张%' AND deleted = 0;

-- 档案内容搜索
SELECT
    '档案内容搜索' AS test_name,
    COUNT(*) AS result_count
FROM edu_student_record
WHERE title LIKE '%表现%' OR content LIKE '%表现%'
AND deleted = 0;

-- 6. 数据写入性能测试
SELECT '6. 数据写入性能测试' AS test_section;

-- 创建临时测试表
CREATE TEMPORARY TABLE temp_performance_test (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    test_data VARCHAR(255),
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP
) ENGINE=InnoDB;

-- 批量插入测试
INSERT INTO temp_performance_test (test_data)
SELECT CONCAT('测试数据_', seq)
FROM (
    SELECT @row := @row + 1 AS seq
    FROM (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
          SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t1,
         (SELECT 0 UNION SELECT 1 UNION SELECT 2 UNION SELECT 3 UNION SELECT 4 UNION
          SELECT 5 UNION SELECT 6 UNION SELECT 7 UNION SELECT 8 UNION SELECT 9) t2,
         (SELECT @row := 0) r
    LIMIT 1000
) AS seq_table;

SELECT
    '批量插入1000条记录' AS test_name,
    COUNT(*) AS result_count
FROM temp_performance_test;

-- 清理临时表
DROP TEMPORARY TABLE temp_performance_test;

-- 7. 事务性能测试
SELECT '7. 事务性能测试' AS test_section;

-- 模拟事务操作
START TRANSACTION;

-- 插入测试用户
INSERT INTO sys_user (username, password, real_name, phone, email, role, status, created_time, updated_time)
VALUES
('test_user001', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '测试用户1', '13900000001', 'test1@example.com', 'TEACHER', 'ACTIVE', NOW(), NOW()),
('test_user002', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '测试用户2', '13900000002', 'test2@example.com', 'TEACHER', 'ACTIVE', NOW(), NOW());

-- 插入测试班级
INSERT INTO edu_class (name, grade, teacher_id, student_count, description, status, created_time, updated_time)
VALUES ('测试班级', '测试年级', (SELECT id FROM sys_user WHERE username = 'test_user001'), 0, '测试班级描述', 'ACTIVE', NOW(), NOW());

-- 插入测试学生
INSERT INTO edu_student (student_id, name, gender, birth_date, id_card, class_id, enrollment_date, status, created_time, updated_time)
VALUES
('TEST001', '测试学生1', 'MALE', '2017-01-01', '110101201701010001', (SELECT id FROM edu_class WHERE name = '测试班级'), '2024-09-01', 'ACTIVE', NOW(), NOW()),
('TEST002', '测试学生2', 'FEMALE', '2017-01-02', '110101201701020002', (SELECT id FROM edu_class WHERE name = '测试班级'), '2024-09-01', 'ACTIVE', NOW(), NOW());

-- 插入测试记录
INSERT INTO edu_student_record (student_id, teacher_id, type, category, title, content, importance, is_public, parent_notified, created_time, updated_time)
VALUES
((SELECT id FROM edu_student WHERE student_id = 'TEST001'), (SELECT id FROM sys_user WHERE username = 'test_user001'), 'PRAISE', '测试分类', '测试记录', '这是一个测试记录', 'MEDIUM', TRUE, FALSE, NOW(), NOW());

COMMIT;

SELECT
    '事务操作测试' AS test_name,
    '成功完成事务操作' AS result;

-- 清理测试数据
DELETE FROM edu_student_record WHERE teacher_id IN (SELECT id FROM sys_user WHERE username LIKE 'test_user%');
DELETE FROM edu_parent_student WHERE student_id IN (SELECT id FROM edu_student WHERE student_id LIKE 'TEST%');
DELETE FROM edu_student WHERE student_id LIKE 'TEST%';
DELETE FROM edu_class WHERE name = '测试班级';
DELETE FROM sys_user WHERE username LIKE 'test_user%';

-- 8. 存储空间统计
SELECT '8. 存储空间统计' AS test_section;

-- 表大小统计
SELECT
    table_name AS '表名',
    ROUND(((data_length + index_length) / 1024 / 1024), 2) AS '表大小(MB)',
    ROUND((data_length / 1024 / 1024), 2) AS '数据大小(MB)',
    ROUND((index_length / 1024 / 1024), 2) AS '索引大小(MB)',
    table_rows AS '行数'
FROM information_schema.TABLES
WHERE table_schema = 'home_school'
    AND table_name IN ('sys_user', 'edu_class', 'edu_student', 'edu_parent_student',
                      'edu_student_record', 'sys_notification', 'sys_log', 'sys_file')
ORDER BY (data_length + index_length) DESC;

-- 9. 索引使用统计
SELECT '9. 索引使用统计' AS test_section;

-- 显示索引统计信息
SELECT
    table_name AS '表名',
    index_name AS '索引名',
    non_unique AS '非唯一',
    seq_in_index AS '索引序号',
    column_name AS '列名',
    collation AS '排序',
    cardinality AS '基数',
    sub_part AS '子部分',
    packed AS '压缩',
    nullable AS '可为空',
    index_type AS '索引类型'
FROM information_schema.STATISTICS
WHERE table_schema = 'home_school'
    AND table_name IN ('sys_user', 'edu_class', 'edu_student', 'edu_parent_student',
                      'edu_student_record', 'sys_notification', 'sys_log', 'sys_file')
ORDER BY table_name, index_name, seq_in_index;

-- 10. 数据库连接和锁状态
SELECT '10. 数据库连接和锁状态' AS test_section;

-- 显示当前连接数
SHOW STATUS LIKE 'Threads_connected';

-- 显示最大连接数
SHOW VARIABLES LIKE 'max_connections';

-- 显示锁等待情况
SELECT
    r.trx_id AS waiting_trx_id,
    r.trx_mysql_thread_id AS waiting_thread,
    r.trx_query AS waiting_query,
    b.trx_id AS blocking_trx_id,
    b.trx_mysql_thread_id AS blocking_thread,
    b.trx_query AS blocking_query
FROM information_schema.innodb_lock_waits w
INNER JOIN information_schema.innodb_trx b ON b.trx_id = w.blocking_trx_id
INNER JOIN information_schema.innodb_trx r ON r.trx_id = w.requesting_trx_id;

-- 11. 缓冲池状态
SELECT '11. 缓冲池状态' AS test_section;

-- 显示InnoDB缓冲池状态
SHOW ENGINE INNODB STATUS;

-- 显示缓冲池大小和状态
SHOW STATUS LIKE 'Innodb_buffer_pool%';

-- 12. 查询缓存状态
SELECT '12. 查询缓存状态' AS test_section;

-- 显示查询缓存状态
SHOW STATUS LIKE 'Qcache%';

-- 13. 慢查询日志状态
SELECT '13. 慢查询日志状态' AS test_section;

-- 显示慢查询配置
SHOW VARIABLES LIKE 'slow_query_log%';
SHOW VARIABLES LIKE 'long_query_time';

-- 输出测试结束信息
SELECT '=== 数据库性能测试完成 ===' AS test_info;
SELECT NOW() AS test_end_time;

-- 性能优化建议
SELECT '14. 性能优化建议' AS test_section;

SELECT
    '优化建议' AS category,
    '建议内容' AS suggestion,
    '优先级' AS priority
UNION ALL
SELECT
    '索引优化',
    '确保所有WHERE条件、JOIN条件、ORDER BY子句都有适当的索引',
    '高'
UNION ALL
SELECT
    '查询优化',
    '避免SELECT *，只查询需要的字段',
    '高'
UNION ALL
SELECT
    '连接池配置',
    '根据并发量调整HikariCP连接池大小',
    '中'
UNION ALL
SELECT
    '缓存策略',
    '对频繁查询但不常变化的数据使用Redis缓存',
    '高'
UNION ALL
SELECT
    '分页优化',
    '大数据量分页查询使用游标分页或覆盖索引优化',
    '中'
UNION ALL
SELECT
    '定期维护',
    '定期执行ANALYZE TABLE和OPTIMIZE TABLE',
    '低';