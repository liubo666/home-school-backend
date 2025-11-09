-- 创建数据库
CREATE DATABASE IF NOT EXISTS home_school CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE home_school;

-- 用户表
CREATE TABLE IF NOT EXISTS sys_user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '用户ID',
    username VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
    password VARCHAR(100) NOT NULL COMMENT '密码',
    real_name VARCHAR(50) NOT NULL COMMENT '真实姓名',
    phone VARCHAR(20) UNIQUE COMMENT '手机号',
    email VARCHAR(100) COMMENT '邮箱',
    avatar VARCHAR(255) COMMENT '头像URL',
    role ENUM('ADMIN', 'SCHOOL_ADMIN', 'TEACHER', 'PARENT') NOT NULL COMMENT '角色',
    status ENUM('ACTIVE', 'INACTIVE', 'SUSPENDED') DEFAULT 'ACTIVE' COMMENT '状态',
    last_login_time DATETIME COMMENT '最后登录时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',

    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_role (role),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';

-- 班级表
CREATE TABLE IF NOT EXISTS edu_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    name VARCHAR(50) NOT NULL COMMENT '班级名称',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    teacher_id BIGINT NOT NULL COMMENT '班主任ID',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    description TEXT COMMENT '班级描述',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',

    INDEX idx_grade (grade),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';

-- 学生表
CREATE TABLE IF NOT EXISTS edu_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '学生ID',
    student_id VARCHAR(20) NOT NULL UNIQUE COMMENT '学号',
    name VARCHAR(50) NOT NULL COMMENT '姓名',
    gender ENUM('MALE', 'FEMALE') NOT NULL COMMENT '性别',
    birth_date DATE NOT NULL COMMENT '出生日期',
    id_card VARCHAR(18) NOT NULL UNIQUE COMMENT '身份证号',
    class_id BIGINT NOT NULL COMMENT '班级ID',
    address VARCHAR(255) COMMENT '家庭住址',
    emergency_contact_name VARCHAR(50) COMMENT '紧急联系人姓名',
    emergency_contact_phone VARCHAR(20) COMMENT '紧急联系人电话',
    emergency_contact_relation VARCHAR(20) COMMENT '紧急联系人关系',
    enrollment_date DATE NOT NULL COMMENT '入学日期',
    status ENUM('ACTIVE', 'GRADUATED', 'TRANSFERRED') DEFAULT 'ACTIVE' COMMENT '状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',

    INDEX idx_student_id (student_id),
    INDEX idx_class_id (class_id),
    INDEX idx_id_card (id_card),
    INDEX idx_name (name),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';

-- 家长学生关联表
CREATE TABLE IF NOT EXISTS edu_parent_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    parent_id BIGINT NOT NULL COMMENT '家长ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    relation ENUM('FATHER', 'MOTHER', 'GRANDFATHER', 'GRANDMOTHER', 'OTHER') NOT NULL COMMENT '关系',
    is_primary BOOLEAN DEFAULT FALSE COMMENT '是否主要联系人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    UNIQUE KEY uk_parent_student (parent_id, student_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_student_id (student_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长学生关联表';

-- 学生档案记录表
CREATE TABLE IF NOT EXISTS edu_student_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '记录ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    teacher_id BIGINT NOT NULL COMMENT '教师ID',
    type ENUM('PRAISE', 'CRITICISM', 'NEUTRAL') NOT NULL COMMENT '记录类型',
    category VARCHAR(50) NOT NULL COMMENT '记录分类',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    images JSON COMMENT '图片URL列表',
    tags JSON COMMENT '标签列表',
    importance ENUM('HIGH', 'MEDIUM', 'LOW') DEFAULT 'MEDIUM' COMMENT '重要程度',
    is_public BOOLEAN DEFAULT TRUE COMMENT '是否对家长公开',
    parent_notified BOOLEAN DEFAULT FALSE COMMENT '是否已通知家长',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    created_by BIGINT COMMENT '创建人ID',
    updated_by BIGINT COMMENT '更新人ID',
    deleted TINYINT(1) DEFAULT 0 COMMENT '逻辑删除标识',

    INDEX idx_student_id (student_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_type (type),
    INDEX idx_category (category),
    INDEX idx_created_time (created_time),
    INDEX idx_is_public (is_public)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生档案记录表';

-- 通知消息表
CREATE TABLE IF NOT EXISTS sys_notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '通知ID',
    user_id BIGINT NOT NULL COMMENT '用户ID',
    title VARCHAR(100) NOT NULL COMMENT '标题',
    content TEXT NOT NULL COMMENT '内容',
    type ENUM('SYSTEM', 'RECORD', 'CLASS', 'STUDENT') NOT NULL COMMENT '通知类型',
    related_id BIGINT COMMENT '关联ID(记录/班级/学生ID)',
    is_read BOOLEAN DEFAULT FALSE COMMENT '是否已读',
    read_time DATETIME COMMENT '阅读时间',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user_id (user_id),
    INDEX idx_type (type),
    INDEX idx_is_read (is_read),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';

-- 系统日志表
CREATE TABLE IF NOT EXISTS sys_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
    user_id BIGINT COMMENT '用户ID',
    username VARCHAR(50) COMMENT '用户名',
    operation VARCHAR(100) NOT NULL COMMENT '操作类型',
    method VARCHAR(200) NOT NULL COMMENT '请求方法',
    params TEXT COMMENT '请求参数',
    result TEXT COMMENT '返回结果',
    ip VARCHAR(50) COMMENT 'IP地址',
    user_agent VARCHAR(500) COMMENT '用户代理',
    execute_time BIGINT COMMENT '执行时间(毫秒)',
    status ENUM('SUCCESS', 'FAILURE') NOT NULL COMMENT '执行状态',
    error_message TEXT COMMENT '错误信息',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_user_id (user_id),
    INDEX idx_operation (operation),
    INDEX idx_status (status),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统日志表';

-- 文件管理表
CREATE TABLE IF NOT EXISTS sys_file (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '文件ID',
    original_name VARCHAR(255) NOT NULL COMMENT '原始文件名',
    file_name VARCHAR(255) NOT NULL UNIQUE COMMENT '存储文件名',
    file_path VARCHAR(500) NOT NULL COMMENT '文件路径',
    file_size BIGINT NOT NULL COMMENT '文件大小(字节)',
    file_type VARCHAR(50) NOT NULL COMMENT '文件类型',
    mime_type VARCHAR(100) COMMENT 'MIME类型',
    upload_user_id BIGINT COMMENT '上传用户ID',
    business_type VARCHAR(50) COMMENT '业务类型',
    business_id BIGINT COMMENT '业务ID',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    INDEX idx_file_name (file_name),
    INDEX idx_upload_user_id (upload_user_id),
    INDEX idx_business_type (business_type),
    INDEX idx_created_time (created_time)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='文件管理表';

-- 创建初始管理员用户
INSERT INTO sys_user (username, password, real_name, phone, email, role, status)
VALUES ('admin', '$2a$10$7JB720yubVSOfvVWbfXCOOxjTOQcQjmrJF1ZM4nAVccp/.rkCqE5S', '系统管理员', '13800000001', 'admin@homeschool.com', 'ADMIN', 'ACTIVE')
ON DUPLICATE KEY UPDATE username = username;