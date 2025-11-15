-- 插入基础测试数据脚本
-- V4__Insert_basic_test_data.sql

USE home_school;

-- 禁用外键检查
SET FOREIGN_KEY_CHECKS = 0;

-- 1. 插入测试用户数据
INSERT INTO sys_user (username, password, real_name, phone, email, school_id, avatar, role, status, last_login_time, created_time, updated_time) VALUES
-- 管理员用户
('admin', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '系统管理员', '13800000001', 'admin@homeschool.com', 1, 'https://example.com/avatar/admin.jpg', 'ADMIN', 'ACTIVE', NOW(), NOW(), NOW()),

-- 学校管理员
('school_admin', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '学校管理员', '13800000002', 'school_admin@homeschool.com', 1, 'https://example.com/avatar/school_admin.jpg', 'SCHOOL_ADMIN', 'ACTIVE', NOW(), NOW(), NOW()),

-- 教师用户
('teacher001', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '张老师', '13800000101', 'zhang@homeschool.com', 1, 'https://example.com/avatar/teacher1.jpg', 'TEACHER', 'ACTIVE', NOW(), NOW(), NOW()),
('teacher002', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '李老师', '13800000102', 'li@homeschool.com', 1, 'https://example.com/avatar/teacher2.jpg', 'TEACHER', 'ACTIVE', NOW(), NOW(), NOW()),
('teacher003', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '王老师', '13800000103', 'wang@homeschool.com', 1, 'https://example.com/avatar/teacher3.jpg', 'TEACHER', 'ACTIVE', NOW(), NOW(), NOW()),

-- 家长用户
('parent001', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '张爸爸', '13800000201', 'zhangbaba@homeschool.com', 1, 'https://example.com/avatar/parent1.jpg', 'PARENT', 'ACTIVE', NOW(), NOW(), NOW()),
('parent002', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '张妈妈', '13800000202', 'zhangmama@homeschool.com', 1, 'https://example.com/avatar/parent2.jpg', 'PARENT', 'ACTIVE', NOW(), NOW(), NOW()),
('parent003', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '李爸爸', '13800000203', 'libaba@homeschool.com', 1, 'https://example.com/avatar/parent3.jpg', 'PARENT', 'ACTIVE', NOW(), NOW(), NOW()),
('parent004', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '李妈妈', '13800000204', 'limama@homeschool.com', 1, 'https://example.com/avatar/parent4.jpg', 'PARENT', 'ACTIVE', NOW(), NOW(), NOW()),
('parent005', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '王爸爸', '13800000205', 'wangbaba@homeschool.com', 1, 'https://example.com/avatar/parent5.jpg', 'PARENT', 'ACTIVE', NOW(), NOW(), NOW());

-- 2. 插入班级数据
INSERT INTO edu_class (name, grade, teacher_id, student_count, description, status, created_time, updated_time) VALUES
('一年级1班', '一年级', 4, 25, '一年级优秀班集体', 'ACTIVE', NOW(), NOW()),
('一年级2班', '一年级', 5, 24, '一年级实验班', 'ACTIVE', NOW(), NOW()),
('二年级1班', '二年级', 6, 26, '二年级重点班', 'ACTIVE', NOW(), NOW());

-- 3. 插入学生数据
INSERT INTO edu_student (student_id, name, gender, birth_date, id_card, class_id, address, emergency_contact_name, emergency_contact_phone, emergency_contact_relation, enrollment_date, status, created_time, updated_time) VALUES
-- 一年级1班学生
('2024001', '张小明', 'MALE', '2017-03-15', '110101201703150001', 1, '北京市朝阳区某某街道123号', '张爸爸', '13800000201', '父亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024002', '李小红', 'FEMALE', '2017-05-20', '110101201705200002', 1, '北京市朝阳区某某街道124号', '李妈妈', '13800000204', '母亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024003', '王小强', 'MALE', '2017-08-10', '110101201708100003', 1, '北京市朝阳区某某街道125号', '王爸爸', '13800000205', '父亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024004', '刘小美', 'FEMALE', '2017-02-28', '110101201702280004', 1, '北京市朝阳区某某街道126号', '刘妈妈', '13800000206', '母亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024005', '陈小军', 'MALE', '2017-06-12', '110101201706120005', 1, '北京市朝阳区某某街道127号', '陈爸爸', '13800000207', '父亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),

-- 一年级2班学生
('2024006', '赵小丽', 'FEMALE', '2017-04-08', '110101201704080006', 2, '北京市海淀区某某街道234号', '赵妈妈', '13800000208', '母亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024007', '孙小华', 'MALE', '2017-07-22', '110101201707220007', 2, '北京市海淀区某某街道235号', '孙爸爸', '13800000209', '父亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),
('2024008', '周小芳', 'FEMALE', '2017-03-30', '110101201703300008', 2, '北京市海淀区某某街道236号', '周妈妈', '13800000210', '母亲', '2024-09-01', 'ACTIVE', NOW(), NOW()),

-- 二年级1班学生
('2023009', '吴小刚', 'MALE', '2016-09-15', '110101201609150009', 3, '北京市西城区某某街道345号', '吴爸爸', '13800000211', '父亲', '2023-09-01', 'ACTIVE', NOW(), NOW()),
('2024010', '郑小婷', 'FEMALE', '2016-11-20', '110101201611200010', 3, '北京市西城区某某街道346号', '郑妈妈', '13800000212', '母亲', '2024-09-01', 'ACTIVE', NOW(), NOW());

-- 4. 插入家长学生关联数据
INSERT INTO edu_parent_student (parent_id, student_id, relation, is_primary, created_time) VALUES
-- 张小明的家长
(7, 1, 'FATHER', TRUE, NOW()),
(8, 1, 'MOTHER', FALSE, NOW()),

-- 李小红的家长
(9, 2, 'FATHER', FALSE, NOW()),
(10, 2, 'MOTHER', TRUE, NOW()),

-- 王小强的家长
(11, 3, 'FATHER', TRUE, NOW()),

-- 刘小美的家长（模拟单亲家庭）
(12, 4, 'MOTHER', TRUE, NOW()),

-- 陈小军的家长
(13, 5, 'FATHER', TRUE, NOW()),

-- 赵小丽的家长
(14, 6, 'MOTHER', TRUE, NOW()),

-- 孙小华的家长
(15, 7, 'FATHER', TRUE, NOW()),

-- 周小芳的家长
(16, 8, 'MOTHER', TRUE, NOW()),

-- 吴小刚的家长
(17, 9, 'FATHER', TRUE, NOW()),

-- 郑小婷的家长
(18, 10, 'MOTHER', TRUE, NOW());

-- 5. 插入学生档案记录数据
INSERT INTO edu_student_record (student_id, teacher_id, type, category, title, content, images, tags, importance, is_public, parent_notified, created_time, updated_time) VALUES
-- 张小明的记录
(1, 4, 'PRAISE', '学习表现', '数学考试成绩优异', '张小明同学在本月数学考试中取得满分成绩，表现出色',
'["https://example.com/images/math_exam.jpg"]', '["数学", "考试", "满分"]', 'HIGH', TRUE, TRUE, NOW(), NOW()),
(1, 4, 'NEUTRAL', '日常表现', '课堂表现良好', '张小明同学今天课堂上积极发言，认真听讲',
NULL, '["课堂", "表现"]', 'MEDIUM', TRUE, FALSE, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),

-- 李小红的记录
(2, 4, 'PRAISE', '文艺活动', '演讲比赛获奖', '李小红同学在学校演讲比赛中获得一等奖，展现了优秀的表达能力',
'["https://example.com/images/speech_contest.jpg"]', '["演讲", "比赛", "获奖"]', 'HIGH', TRUE, TRUE, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),
(2, 4, 'CRITICISM', '纪律问题', '上课迟到', '李小红同学今天上午第一节课迟到，需要加强时间管理',
NULL, '["迟到", "纪律"]', 'LOW', TRUE, TRUE, NOW() - INTERVAL 3 DAY, NOW() - INTERVAL 3 DAY),

-- 王小强的记录
(3, 4, 'PRAISE', '体育活动', '运动会表现突出', '王小强同学在运动会100米跑比赛中获得第二名，为班级争光',
'["https://example.com/images/sports_meet.jpg"]', '["体育", "运动会", "跑步"]', 'MEDIUM', TRUE, TRUE, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY),

-- 刘小美的记录
(4, 4, 'NEUTRAL', '学习表现', '语文作业完成情况', '刘小美同学本周语文作业完成质量良好，字迹工整',
NULL, '["语文", "作业"]', 'MEDIUM', TRUE, FALSE, NOW() - INTERVAL 4 DAY, NOW() - INTERVAL 4 DAY),

-- 陈小军的记录
(5, 4, 'CRITICISM', '行为规范', '与同学发生争执', '陈小军同学在课间与同学发生争执，需要学会与人友好相处',
NULL, '["行为", "争执"]', 'MEDIUM', TRUE, TRUE, NOW() - INTERVAL 2 DAY, NOW() - INTERVAL 2 DAY),

-- 吴小刚的记录（二年级）
(9, 6, 'PRAISE', '学习表现', '科学实验兴趣浓厚', '吴小刚同学在科学课上表现积极，对实验操作很感兴趣',
'["https://example.com/images/science_lab.jpg"]', '["科学", "实验", "兴趣"]', 'MEDIUM', TRUE, TRUE, NOW() - INTERVAL 1 DAY, NOW() - INTERVAL 1 DAY);

-- 6. 插入通知消息数据
INSERT INTO sys_notification (user_id, title, content, type, related_id, is_read, read_time, created_time) VALUES
-- 张小明的家长通知
(7, '张小明获得数学考试满分', '您的孩子张小明在本月数学考试中取得满分成绩，表现优异，请继续鼓励！', 'RECORD', 1, TRUE, NOW() - INTERVAL 1 HOUR, NOW() - INTERVAL 1 DAY),
(8, '张小明获得数学考试满分', '您的孩子张小明在本月数学考试中取得满分成绩，表现优异，请继续鼓励！', 'RECORD', 1, FALSE, NULL, NOW() - INTERVAL 1 DAY),

-- 李小红的家长通知
(9, '李小红演讲比赛获奖', '您的孩子李小红在学校演讲比赛中获得一等奖，展现了优秀的表达能力', 'RECORD', 3, TRUE, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 2 DAY),
(10, '李小红演讲比赛获奖', '您的孩子李小红在学校演讲比赛中获得一等奖，展现了优秀的表达能力', 'RECORD', 3, TRUE, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 2 DAY),
(10, '李小红上课迟到提醒', '您的孩子李小红今天上午第一节课迟到，请帮助孩子加强时间管理', 'RECORD', 4, FALSE, NULL, NOW() - INTERVAL 3 DAY),

-- 王小强的家长通知
(11, '王小强运动会获奖', '您的孩子王小强在运动会100米跑比赛中获得第二名，为班级争光', 'RECORD', 6, TRUE, NOW() - INTERVAL 4 HOUR, NOW() - INTERVAL 1 DAY),

-- 陈小军的家长通知
(13, '陈小军与同学争执情况', '您的孩子陈小军今天在课间与同学发生争执，请在家引导孩子学会友好相处', 'RECORD', 8, FALSE, NULL, NOW() - INTERVAL 2 DAY),

-- 系统通知
(7, '系统维护通知', '系统将于本周六凌晨2点进行维护升级，期间可能无法正常使用', 'SYSTEM', NULL, FALSE, NULL, NOW() - INTERVAL 1 HOUR),
(8, '系统维护通知', '系统将于本周六凌晨2点进行维护升级，期间可能无法正常使用', 'SYSTEM', NULL, FALSE, NULL, NOW() - INTERVAL 1 HOUR),

-- 班级通知
(7, '家长会通知', '一年级1班定于本周五下午2点召开家长会，请准时参加', 'CLASS', 1, TRUE, NOW() - INTERVAL 3 HOUR, NOW() - INTERVAL 5 HOUR),
(8, '家长会通知', '一年级1班定于本周五下午2点召开家长会，请准时参加', 'CLASS', 1, TRUE, NOW() - INTERVAL 2 HOUR, NOW() - INTERVAL 5 HOUR);

-- 7. 插入系统日志数据
INSERT INTO sys_log (user_id, username, operation, method, params, ip, user_agent, execute_time, status, error_message, created_time) VALUES
-- 登录日志
(1, 'admin', '用户登录', 'POST /api/auth/login', '{"username":"admin","password":"******"}', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 120, 'SUCCESS', NULL, NOW() - INTERVAL 2 HOUR),
(4, 'teacher001', '用户登录', 'POST /api/auth/login', '{"username":"teacher001","password":"******"}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 95, 'SUCCESS', NULL, NOW() - INTERVAL 3 HOUR),
(7, 'parent001', '用户登录', 'POST /api/auth/login', '{"username":"parent001","password":"******"}', '192.168.1.200', 'Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15', 150, 'SUCCESS', NULL, NOW() - INTERVAL 1 HOUR),

-- 业务操作日志
(4, 'teacher001', '创建学生档案记录', 'POST /api/teacher/record/create', '{"studentId":1,"type":"PRAISE","title":"数学考试成绩优异"}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 200, 'SUCCESS', NULL, NOW() - INTERVAL 1 DAY),
(4, 'teacher001', '查询学生列表', 'GET /api/teacher/student/list', '{"page":1,"pageSize":20}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 80, 'SUCCESS', NULL, NOW() - INTERVAL 1 HOUR),
(7, 'parent001', '查看孩子档案', 'GET /api/parent/record/list', '{"studentId":1,"page":1}', '192.168.1.200', 'Mozilla/5.0 (iPhone; CPU iPhone OS 14_0 like Mac OS X) AppleWebKit/605.1.15', 110, 'SUCCESS', NULL, NOW() - INTERVAL 30 MINUTE),

-- 系统操作日志
(1, 'admin', '系统配置更新', 'PUT /api/admin/system/config', '{"key":"system.title","value":"家校协同系统"}', '127.0.0.1', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 150, 'SUCCESS', NULL, NOW() - INTERVAL 4 HOUR),

-- 错误日志示例
(4, 'teacher001', '文件上传失败', 'POST /api/teacher/file/upload', '{"file":"student_list.xlsx"}', '192.168.1.100', 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36', 5000, 'FAILURE', '文件格式不正确，仅支持.xlsx格式', NOW() - INTERVAL 2 HOUR);

-- 8. 插入文件管理数据
INSERT INTO sys_file (original_name, file_name, file_path, file_size, file_type, mime_type, upload_user_id, business_type, business_id, created_time) VALUES
('数学考试试卷.jpg', 'math_exam_20241208_001.jpg', 'records/images/2024/12/math_exam_20241208_001.jpg', 1024000, 'jpg', 'image/jpeg', 4, 'RECORD_IMAGE', 1, NOW() - INTERVAL 1 DAY),
('演讲比赛照片.jpg', 'speech_contest_20241207_001.jpg', 'records/images/2024/12/speech_contest_20241207_001.jpg', 2048000, 'jpg', 'image/jpeg', 4, 'RECORD_IMAGE', 3, NOW() - INTERVAL 2 DAY),
('运动会照片.jpg', 'sports_meet_20241208_001.jpg', 'records/images/2024/12/sports_meet_20241208_001.jpg', 1536000, 'jpg', 'image/jpeg', 4, 'RECORD_IMAGE', 6, NOW() - INTERVAL 1 DAY),
('科学实验照片.jpg', 'science_lab_20241207_001.jpg', 'records/images/2024/12/science_lab_20241207_001.jpg', 2560000, 'jpg', 'image/jpeg', 6, 'RECORD_IMAGE', 9, NOW() - INTERVAL 1 DAY),
('学生名单导入.xlsx', 'student_import_20241208_001.xlsx', 'excel/import/2024/12/student_import_20241208_001.xlsx', 51200, 'xlsx', 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet', 4, 'STUDENT_IMPORT', NULL, NOW() - INTERVAL 2 HOUR);

-- 重新启用外键检查
SET FOREIGN_KEY_CHECKS = 1;

-- 更新班级学生数量（实际数量）
UPDATE edu_class SET student_count = 5 WHERE id = 1;  -- 一年级1班实际5名学生
UPDATE edu_class SET student_count = 3 WHERE id = 2;  -- 一年级2班实际3名学生
UPDATE edu_class SET student_count = 2 WHERE id = 3;  -- 二年级1班实际2名学生

-- 输出统计信息
SELECT '基础测试数据插入完成' AS message;
SELECT COUNT(*) AS user_count FROM sys_user;
SELECT COUNT(*) AS class_count FROM edu_class;
SELECT COUNT(*) AS student_count FROM edu_student;
SELECT COUNT(*) AS parent_student_count FROM edu_parent_student;
SELECT COUNT(*) AS record_count FROM edu_student_record;
SELECT COUNT(*) AS notification_count FROM sys_notification;
SELECT COUNT(*) AS log_count FROM sys_log;
SELECT COUNT(*) AS file_count FROM sys_file;