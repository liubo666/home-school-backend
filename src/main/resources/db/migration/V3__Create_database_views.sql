-- 创建数据库视图脚本
-- V3__Create_database_views.sql

USE home_school;

-- 1. 学生详细信息视图（包含班级信息）
CREATE OR REPLACE VIEW v_student_detail AS
SELECT
    s.id AS student_id,
    s.student_id AS student_number,
    s.name AS student_name,
    s.gender,
    s.birth_date,
    s.id_card,
    s.address,
    s.emergency_contact_name,
    s.emergency_contact_phone,
    s.emergency_contact_relation,
    s.enrollment_date,
    s.status AS student_status,
    s.created_time AS student_created_time,

    c.id AS class_id,
    c.name AS class_name,
    c.grade,
    c.description AS class_description,
    c.status AS class_status,

    u.id AS teacher_id,
    u.real_name AS teacher_name,
    u.phone AS teacher_phone,
    u.email AS teacher_email

FROM edu_student s
INNER JOIN edu_class c ON s.class_id = c.id
INNER JOIN sys_user u ON c.teacher_id = u.id
WHERE s.deleted = 0 AND c.deleted = 0 AND u.deleted = 0;

-- 2. 家长学生关联详情视图
CREATE OR REPLACE VIEW v_parent_student_relation AS
SELECT
    ps.id AS relation_id,
    ps.relation,
    ps.is_primary,
    ps.created_time AS relation_created_time,

    p.id AS parent_id,
    p.username AS parent_username,
    p.real_name AS parent_name,
    p.phone AS parent_phone,
    p.email AS parent_email,
    p.avatar AS parent_avatar,
    p.status AS parent_status,

    s.id AS student_id,
    s.student_id AS student_number,
    s.name AS student_name,
    s.gender,
    s.birth_date,
    s.class_id,
    s.status AS student_status,

    c.name AS class_name,
    c.grade

FROM edu_parent_student ps
INNER JOIN sys_user p ON ps.parent_id = p.id
INNER JOIN edu_student s ON ps.student_id = s.id
INNER JOIN edu_class c ON s.class_id = c.id
WHERE p.deleted = 0 AND s.deleted = 0 AND c.deleted = 0;

-- 3. 学生档案统计视图
CREATE OR REPLACE VIEW v_student_record_statistics AS
SELECT
    s.id AS student_id,
    s.name AS student_name,
    c.name AS class_name,
    c.grade,

    COUNT(CASE WHEN r.type = 'PRAISE' THEN 1 END) AS praise_count,
    COUNT(CASE WHEN r.type = 'CRITICISM' THEN 1 END) AS criticism_count,
    COUNT(CASE WHEN r.type = 'NEUTRAL' THEN 1 END) AS neutral_count,
    COUNT(r.id) AS total_record_count,

    COUNT(CASE WHEN r.importance = 'HIGH' THEN 1 END) AS high_importance_count,
    COUNT(CASE WHEN r.importance = 'MEDIUM' THEN 1 END) AS medium_importance_count,
    COUNT(CASE WHEN r.importance = 'LOW' THEN 1 END) AS low_importance_count,

    MAX(r.created_time) AS last_record_time,

    COUNT(CASE WHEN r.is_public = 1 THEN 1 END) AS public_record_count,
    COUNT(CASE WHEN r.parent_notified = 1 THEN 1 END) AS notified_record_count

FROM edu_student s
INNER JOIN edu_class c ON s.class_id = c.id
LEFT JOIN edu_student_record r ON s.id = r.student_id AND r.deleted = 0
WHERE s.deleted = 0 AND c.deleted = 0
GROUP BY s.id, s.name, c.name, c.grade;

-- 4. 班级统计视图
CREATE OR REPLACE VIEW v_class_statistics AS
SELECT
    c.id AS class_id,
    c.name AS class_name,
    c.grade,
    c.description,
    c.status AS class_status,
    c.created_time AS class_created_time,

    u.real_name AS teacher_name,
    u.phone AS teacher_phone,

    COUNT(s.id) AS total_student_count,
    COUNT(CASE WHEN s.status = 'ACTIVE' THEN 1 END) AS active_student_count,
    COUNT(CASE WHEN s.status = 'GRADUATED' THEN 1 END) AS graduated_student_count,
    COUNT(CASE WHEN s.status = 'TRANSFERRED' THEN 1 END) AS transferred_student_count,

    COUNT(CASE WHEN s.gender = 'MALE' THEN 1 END) AS male_student_count,
    COUNT(CASE WHEN s.gender = 'FEMALE' THEN 1 END) AS female_student_count,

    COUNT(r.id) AS total_record_count,
    COUNT(CASE WHEN r.type = 'PRAISE' THEN 1 END) AS praise_count,
    COUNT(CASE WHEN r.type = 'CRITICISM' THEN 1 END) AS criticism_count,

    MAX(r.created_time) AS last_record_time,

    c.student_count AS configured_student_count

FROM edu_class c
INNER JOIN sys_user u ON c.teacher_id = u.id
LEFT JOIN edu_student s ON c.id = s.class_id AND s.deleted = 0
LEFT JOIN edu_student_record r ON s.id = r.student_id AND r.deleted = 0
WHERE c.deleted = 0 AND u.deleted = 0
GROUP BY c.id, c.name, c.grade, c.description, c.status, c.created_time,
         u.real_name, u.phone, c.student_count;

-- 5. 用户活动统计视图
CREATE OR REPLACE VIEW v_user_activity_statistics AS
SELECT
    u.id AS user_id,
    u.username,
    u.real_name,
    u.role,
    u.status,
    u.last_login_time,
    u.created_time,

    COUNT(CASE WHEN l.id IS NOT NULL THEN 1 END) AS login_count,
    COUNT(CASE WHEN l.status = 'SUCCESS' THEN 1 END) AS success_operation_count,
    COUNT(CASE WHEN l.status = 'FAILURE' THEN 1 END) AS failure_operation_count,

    MAX(l.created_time) AS last_activity_time,

    COUNT(CASE WHEN n.id IS NOT NULL THEN 1 END) AS notification_count,
    COUNT(CASE WHEN n.is_read = 0 THEN 1 END) AS unread_notification_count,

    -- 教师相关统计
    COUNT(DISTINCT CASE WHEN u.role = 'TEACHER' THEN cls.id END) AS managed_class_count,
    COUNT(DISTINCT CASE WHEN u.role = 'TEACHER' THEN stu.id END) AS managed_student_count,
    COUNT(CASE WHEN u.role = 'TEACHER' THEN r.id END) AS created_record_count,

    -- 家长相关统计
    COUNT(DISTINCT CASE WHEN u.role = 'PARENT' THEN ps.student_id END) AS children_count

FROM sys_user u
LEFT JOIN sys_log l ON u.id = l.user_id
LEFT JOIN sys_notification n ON u.id = n.user_id
LEFT JOIN edu_class cls ON u.id = cls.teacher_id AND cls.deleted = 0
LEFT JOIN edu_student stu ON cls.id = stu.class_id AND stu.deleted = 0
LEFT JOIN edu_student_record r ON u.id = r.teacher_id AND r.deleted = 0
LEFT JOIN edu_parent_student ps ON u.id = ps.parent_id
WHERE u.deleted = 0
GROUP BY u.id, u.username, u.real_name, u.role, u.status,
         u.last_login_time, u.created_time;

-- 6. 系统性能统计视图
CREATE OR REPLACE VIEW v_system_performance_statistics AS
SELECT
    -- 用户统计
    COUNT(DISTINCT u.id) AS total_user_count,
    COUNT(CASE WHEN u.role = 'ADMIN' THEN 1 END) AS admin_count,
    COUNT(CASE WHEN u.role = 'SCHOOL_ADMIN' THEN 1 END) AS school_admin_count,
    COUNT(CASE WHEN u.role = 'TEACHER' THEN 1 END) AS teacher_count,
    COUNT(CASE WHEN u.role = 'PARENT' THEN 1 END) AS parent_count,

    -- 班级统计
    COUNT(DISTINCT c.id) AS total_class_count,
    COUNT(DISTINCT c.grade) AS total_grade_count,

    -- 学生统计
    COUNT(DISTINCT s.id) AS total_student_count,
    COUNT(CASE WHEN s.status = 'ACTIVE' THEN 1 END) AS active_student_count,
    COUNT(CASE WHEN s.gender = 'MALE' THEN 1 END) AS male_student_count,
    COUNT(CASE WHEN s.gender = 'FEMALE' THEN 1 END) AS female_student_count,

    -- 档案记录统计
    COUNT(DISTINCT r.id) AS total_record_count,
    COUNT(CASE WHEN r.type = 'PRAISE' THEN 1 END) AS praise_record_count,
    COUNT(CASE WHEN r.type = 'CRITICISM' THEN 1 END) AS criticism_record_count,
    COUNT(CASE WHEN r.type = 'NEUTRAL' THEN 1 END) AS neutral_record_count,

    -- 通知统计
    COUNT(DISTINCT n.id) AS total_notification_count,
    COUNT(CASE WHEN n.is_read = 0 THEN 1 END) AS unread_notification_count,

    -- 日志统计
    COUNT(DISTINCT l.id) AS total_log_count,
    COUNT(CASE WHEN l.status = 'SUCCESS' THEN 1 END) AS success_log_count,
    COUNT(CASE WHEN l.status = 'FAILURE' THEN 1 END) AS failure_log_count,

    -- 文件统计
    COUNT(DISTINCT f.id) AS total_file_count,
    SUM(f.file_size) AS total_file_size,

    -- 时间统计
    MIN(u.created_time) AS system_start_time,
    MAX(l.created_time) AS last_activity_time,

    -- 今日统计
    COUNT(CASE WHEN DATE(l.created_time) = CURDATE() THEN 1 END) AS today_log_count,
    COUNT(CASE WHEN DATE(r.created_time) = CURDATE() THEN 1 END) AS today_record_count,
    COUNT(CASE WHEN DATE(n.created_time) = CURDATE() THEN 1 END) AS today_notification_count

FROM sys_user u
LEFT JOIN edu_class c ON 1=1 AND c.deleted = 0
LEFT JOIN edu_student s ON 1=1 AND s.deleted = 0
LEFT JOIN edu_student_record r ON 1=1 AND r.deleted = 0
LEFT JOIN sys_notification n ON 1=1
LEFT JOIN sys_log l ON 1=1
LEFT JOIN sys_file f ON 1=1
WHERE u.deleted = 0;

-- 7. 实时活动监控视图
CREATE OR REPLACE VIEW v_recent_activities AS
SELECT
    'LOGIN' AS activity_type,
    u.real_name AS user_name,
    u.role,
    l.operation,
    l.ip,
    l.created_time
FROM sys_log l
INNER JOIN sys_user u ON l.user_id = u.id
WHERE l.created_time >= DATE_SUB(NOW(), INTERVAL 24 HOUR)
  AND l.operation LIKE '%LOGIN%'

UNION ALL

SELECT
    'RECORD_CREATED' AS activity_type,
    u.real_name AS user_name,
    u.role,
    CONCAT('创建记录: ', r.title) AS operation,
    NULL AS ip,
    r.created_time
FROM edu_student_record r
INNER JOIN sys_user u ON r.teacher_id = u.id
WHERE r.created_time >= DATE_SUB(NOW(), INTERVAL 24 HOUR)

UNION ALL

SELECT
    'NOTIFICATION' AS activity_type,
    u.real_name AS user_name,
    u.role,
    CONCAT('收到通知: ', n.title) AS operation,
    NULL AS ip,
    n.created_time
FROM sys_notification n
INNER JOIN sys_user u ON n.user_id = u.id
WHERE n.created_time >= DATE_SUB(NOW(), INTERVAL 24 HOUR)

ORDER BY created_time DESC
LIMIT 100;