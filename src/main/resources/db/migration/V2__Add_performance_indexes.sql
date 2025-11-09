-- 创建优化索引脚本
-- V2__Add_performance_indexes.sql

USE home_school;

-- 用户表优化索引
-- 1. 复合索引：角色和状态组合查询
CREATE INDEX idx_sys_user_role_status ON sys_user(role, status);

-- 2. 复合索引：最后登录时间排序
CREATE INDEX idx_sys_user_last_login ON sys_user(last_login_time DESC);

-- 3. 复合索引：创建时间和状态
CREATE INDEX idx_sys_user_created_status ON sys_user(created_time, status);


-- 班级表优化索引
-- 1. 复合索引：年级和状态组合查询
CREATE INDEX idx_edu_class_grade_status ON edu_class(grade, status);

-- 2. 复合索引：教师ID和状态
CREATE INDEX idx_edu_class_teacher_status ON edu_class(teacher_id, status);

-- 3. 索引：学生数量统计
CREATE INDEX idx_edu_class_student_count ON edu_class(student_count);


-- 学生表优化索引
-- 1. 复合索引：班级ID和状态组合查询
CREATE INDEX idx_edu_student_class_status ON edu_student(class_id, status);

-- 2. 复合索引：姓名和班级ID（常用搜索组合）
CREATE INDEX idx_edu_student_name_class ON edu_student(name, class_id);

-- 3. 复合索引：入学日期和状态
CREATE INDEX idx_edu_student_enrollment_status ON edu_student(enrollment_date, status);

-- 4. 全文索引：姓名搜索优化
CREATE FULLTEXT INDEX idx_edu_student_name_fulltext ON edu_student(name);


-- 家长学生关联表优化索引
-- 1. 复合索引：学生ID和主要联系人标识
CREATE INDEX idx_edu_parent_student_primary ON edu_parent_student(student_id, is_primary);

-- 2. 复合索引：关系类型和家长ID
CREATE INDEX idx_edu_parent_student_relation ON edu_parent_student(parent_id, relation);


-- 学生档案记录表优化索引
-- 1. 复合索引：学生ID和创建时间（按学生查看记录）
CREATE INDEX idx_edu_record_student_time ON edu_student_record(student_id, created_time DESC);

-- 2. 复合索引：教师ID和创建时间（按教师查看记录）
CREATE INDEX idx_edu_record_teacher_time ON edu_student_record(teacher_id, created_time DESC);

-- 3. 复合索引：类型和重要程度
CREATE INDEX idx_edu_record_type_importance ON edu_student_record(type, importance);

-- 4. 复合索引：分类和公开状态
CREATE INDEX idx_edu_record_category_public ON edu_student_record(category, is_public);

-- 5. 复合索引：创建时间范围查询
CREATE INDEX idx_edu_record_time_range ON edu_student_record(created_time DESC, type);

-- 6. 全文索引：标题和内容搜索
CREATE FULLTEXT INDEX idx_edu_record_content_fulltext ON edu_student_record(title, content);


-- 通知消息表优化索引
-- 1. 复合索引：用户ID和已读状态
CREATE INDEX idx_sys_notification_user_read ON sys_notification(user_id, is_read);

-- 2. 复合索引：类型和创建时间
CREATE INDEX idx_sys_notification_type_time ON sys_notification(type, created_time DESC);

-- 3. 复合索引：已读状态和创建时间（未读消息查询）
CREATE INDEX idx_sys_notification_unread_time ON sys_notification(is_read, created_time DESC);


-- 系统日志表优化索引
-- 1. 复合索引：用户ID和操作类型
CREATE INDEX idx_sys_log_user_operation ON sys_log(user_id, operation);

-- 2. 复合索引：执行状态和创建时间
CREATE INDEX idx_sys_log_status_time ON sys_log(status, created_time DESC);

-- 3. 复合索引：操作类型和创建时间
CREATE INDEX idx_sys_log_operation_time ON sys_log(operation, created_time DESC);

-- 4. 复合索引：IP地址和创建时间（安全分析）
CREATE INDEX idx_sys_log_ip_time ON sys_log(ip, created_time DESC);

-- 5. 全文索引：错误信息搜索
CREATE FULLTEXT INDEX idx_sys_log_error_fulltext ON sys_log(error_message);


-- 文件管理表优化索引
-- 1. 复合索引：业务类型和业务ID
CREATE INDEX idx_sys_file_business ON sys_file(business_type, business_id);

-- 2. 复合索引：上传用户和创建时间
CREATE INDEX idx_sys_file_user_time ON sys_file(upload_user_id, created_time DESC);

-- 3. 复合索引：文件类型和创建时间
CREATE INDEX idx_sys_file_type_time ON sys_file(file_type, created_time DESC);

-- 4. 索引：文件大小统计
CREATE INDEX idx_sys_file_size ON sys_file(file_size);


-- 分析表统计信息以优化查询性能
ANALYZE TABLE sys_user;
ANALYZE TABLE edu_class;
ANALYZE TABLE edu_student;
ANALYZE TABLE edu_parent_student;
ANALYZE TABLE edu_student_record;
ANALYZE TABLE sys_notification;
ANALYZE TABLE sys_log;
ANALYZE TABLE sys_file;