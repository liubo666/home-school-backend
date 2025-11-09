# 家校协同教育系统 - 后端技术架构文档

## 项目概述
基于Java Spring Boot的家校协同教育系统后端架构，支持多级权限管理、学生档案管理、Excel导入导出、实时通知等核心功能。

## 技术栈选型

### 核心框架
- **Spring Boot**: 3.2.0 (主框架)
- **Spring Security**: 6.2.0 (安全认证)
- **Spring Data JPA**: 3.2.0 (数据访问层)
- **Spring WebSocket**: 3.2.0 (实时通信)
- **Spring Cache**: 3.2.0 (缓存管理)

### 数据库相关
- **MySQL**: 8.0.35 (主数据库)
- **Redis**: 7.2.0 (缓存/会话存储)
- **HikariCP**: 连接池管理
- **Flyway**: 数据库版本管理

### 工具库
- **MyBatis Plus**: 3.5.4 (ORM增强)
- **Apache POI**: 5.2.4 (Excel处理)
- **EasyExcel**: 3.3.2 (Excel导入导出优化)
- **MinIO**: 8.5.4 (文件存储)
- **JWT**: 0.12.3 (Token认证)
- **Lombok**: 1.18.30 (代码简化)
- **MapStruct**: 1.5.5 (对象映射)

### 开发运维
- **JDK**: 17 LTS (Java运行环境)
- **Maven**: 3.9.5 (项目管理)
- **Docker**: 24.0.7 (容器化)
- **Nginx**: 1.24.0 (反向代理)

## 系统架构设计

### 整体架构图
```
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│   小程序前端    │    │      H5前端     │    │   管理后台      │
└─────────────────┘    └─────────────────┘    └─────────────────┘
         │                       │                       │
         └───────────────────────┼───────────────────────┘
                                 │
                    ┌─────────────────┐
                    │   Nginx网关     │
                    │  (负载均衡/SSL) │
                    └─────────────────┘
                                 │
                    ┌─────────────────┐
                    │  Spring Boot    │
                    │    应用层       │
                    └─────────────────┘
                                 │
         ┌───────────────────────┼───────────────────────┐
         │                       │                       │
┌─────────────────┐    ┌─────────────────┐    ┌─────────────────┐
│     MySQL       │    │     Redis       │    │     MinIO       │
│   (主数据库)    │    │   (缓存/会话)   │    │   (文件存储)    │
└─────────────────┘    └─────────────────┘    └─────────────────┘
```

### 分层架构设计
```
┌─────────────────────────────────────────────────────────────┐
│                      Controller层                          │
│  用户控制器 | 学生控制器 | 档案控制器 | 通知控制器          │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                       Service层                            │
│  业务逻辑处理 | 事务管理 | 业务规则验证 | 权限控制           │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                      Repository层                          │
│  数据访问接口 | CRUD操作 | 复杂查询 | 数据持久化           │
└─────────────────────────────────────────────────────────────┘
┌─────────────────────────────────────────────────────────────┐
│                       Entity层                             │
│  实体模型 | 数据表映射 | 关系映射 | 验证注解               │
└─────────────────────────────────────────────────────────────┘
```

## 项目结构设计

### Maven项目结构
```
home-school-backend/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── school/
│   │   │           └── cooperation/
│   │   │               ├── HomeSchoolApplication.java
│   │   │               ├── config/           # 配置类
│   │   │               │   ├── SecurityConfig.java
│   │   │               │   ├── RedisConfig.java
│   │   │               │   ├── MinIOConfig.java
│   │   │               │   └── WebSocketConfig.java
│   │   │               ├── controller/       # 控制器层
│   │   │               │   ├── auth/
│   │   │               │   │   ├── AuthController.java
│   │   │               │   │   └── UserController.java
│   │   │               │   ├── admin/
│   │   │               │   │   ├── AdminController.java
│   │   │               │   │   └── SystemController.java
│   │   │               │   ├── teacher/
│   │   │               │   │   ├── StudentController.java
│   │   │               │   │   ├── ClassController.java
│   │   │               │   │   └── RecordController.java
│   │   │               │   └── parent/
│   │   │               │       ├── ParentController.java
│   │   │               │       └── NotificationController.java
│   │   │               ├── service/          # 服务层
│   │   │               │   ├── auth/
│   │   │               │   │   ├── AuthService.java
│   │   │               │   │   └── UserService.java
│   │   │               │   ├── admin/
│   │   │               │   │   ├── AdminService.java
│   │   │               │   │   └── SystemService.java
│   │   │               │   ├── teacher/
│   │   │               │   │   ├── StudentService.java
│   │   │               │   │   ├── ClassService.java
│   │   │               │   │   └── RecordService.java
│   │   │               │   └── parent/
│   │   │               │       ├── ParentService.java
│   │   │               │       └── NotificationService.java
│   │   │               ├── repository/       # 数据访问层
│   │   │               │   ├── UserRepository.java
│   │   │               │   ├── StudentRepository.java
│   │   │               │   ├── ClassRepository.java
│   │   │               │   ├── RecordRepository.java
│   │   │               │   └── NotificationRepository.java
│   │   │               ├── entity/           # 实体类
│   │   │               │   ├── User.java
│   │   │               │   ├── Student.java
│   │   │               │   ├── Class.java
│   │   │               │   ├── Record.java
│   │   │               │   └── Notification.java
│   │   │               ├── dto/              # 数据传输对象
│   │   │               │   ├── request/
│   │   │               │   │   ├── LoginRequest.java
│   │   │               │   │   ├── RecordCreateRequest.java
│   │   │               │   │   └── ExcelImportRequest.java
│   │   │               │   └── response/
│   │   │               │       ├── UserResponse.java
│   │   │               │       ├── StudentResponse.java
│   │   │               │       └── RecordResponse.java
│   │   │               ├── common/           # 公共组件
│   │   │               │   ├── exception/
│   │   │               │   │   ├── GlobalExceptionHandler.java
│   │   │               │   │   └── BusinessException.java
│   │   │               │   ├── utils/
│   │   │               │   │   ├── JwtUtil.java
│   │   │               │   │   ├── ExcelUtil.java
│   │   │               │   │   ├── FileUtil.java
│   │   │               │   │   └── PasswordUtil.java
│   │   │               │   ├── constant/
│   │   │               │   │   ├── RoleConstant.java
│   │   │               │   │   ├── ErrorCode.java
│   │   │               │   │   └── CacheConstant.java
│   │   │               │   └── aspect/
│   │   │               │       ├── LogAspect.java
│   │   │               │       └── PermissionAspect.java
│   │   │               └── security/         # 安全相关
│   │   │                   ├── JwtAuthenticationFilter.java
│   │   │                   ├── UserDetailsServiceImpl.java
│   │   │                   └── CustomPermissionEvaluator.java
│   │   └── resources/
│   │       ├── application.yml              # 主配置文件
│   │       ├── application-dev.yml          # 开发环境配置
│   │       ├── application-prod.yml         # 生产环境配置
│   │       ├── db/migration/                # 数据库版本管理
│   │       │   ├── V1__Create_user_table.sql
│   │       │   ├── V2__Create_student_table.sql
│   │       │   └── V3__Create_record_table.sql
│   │       ├── static/                      # 静态资源
│   │       └── templates/                   # 模板文件
│   └── test/                               # 测试代码
│       └── java/
│           └── com/
│               └── school/
│                   └── cooperation/
│                       ├── controller/
│                       ├── service/
│                       └── repository/
├── pom.xml                                  # Maven配置
├── Dockerfile                               # Docker配置
├── docker-compose.yml                       # Docker编排
└── README.md                                # 项目说明
```

## 数据库设计

### 数据库表结构

#### 1. 用户表 (sys_user)
```sql
CREATE TABLE sys_user (
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

    INDEX idx_username (username),
    INDEX idx_phone (phone),
    INDEX idx_role (role),
    INDEX idx_status (status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户表';
```

#### 2. 学生表 (edu_student)
```sql
CREATE TABLE edu_student (
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

    INDEX idx_student_id (student_id),
    INDEX idx_class_id (class_id),
    INDEX idx_id_card (id_card),
    INDEX idx_name (name),
    INDEX idx_status (status),

    FOREIGN KEY (class_id) REFERENCES edu_class(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生表';
```

#### 3. 班级表 (edu_class)
```sql
CREATE TABLE edu_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '班级ID',
    name VARCHAR(50) NOT NULL COMMENT '班级名称',
    grade VARCHAR(20) NOT NULL COMMENT '年级',
    teacher_id BIGINT NOT NULL COMMENT '班主任ID',
    student_count INT DEFAULT 0 COMMENT '学生人数',
    description TEXT COMMENT '班级描述',
    status ENUM('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE' COMMENT '状态',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',

    INDEX idx_grade (grade),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_status (status),

    FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='班级表';
```

#### 4. 家长学生关联表 (edu_parent_student)
```sql
CREATE TABLE edu_parent_student (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '关联ID',
    parent_id BIGINT NOT NULL COMMENT '家长ID',
    student_id BIGINT NOT NULL COMMENT '学生ID',
    relation ENUM('FATHER', 'MOTHER', 'GRANDFATHER', 'GRANDMOTHER', 'OTHER') NOT NULL COMMENT '关系',
    is_primary BOOLEAN DEFAULT FALSE COMMENT '是否主要联系人',
    created_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',

    UNIQUE KEY uk_parent_student (parent_id, student_id),
    INDEX idx_parent_id (parent_id),
    INDEX idx_student_id (student_id),

    FOREIGN KEY (parent_id) REFERENCES sys_user(id),
    FOREIGN KEY (student_id) REFERENCES edu_student(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='家长学生关联表';
```

#### 5. 学生档案记录表 (edu_student_record)
```sql
CREATE TABLE edu_student_record (
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

    INDEX idx_student_id (student_id),
    INDEX idx_teacher_id (teacher_id),
    INDEX idx_type (type),
    INDEX idx_category (category),
    INDEX idx_created_time (created_time),
    INDEX idx_is_public (is_public),

    FOREIGN KEY (student_id) REFERENCES edu_student(id),
    FOREIGN KEY (teacher_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='学生档案记录表';
```

#### 6. 通知消息表 (sys_notification)
```sql
CREATE TABLE sys_notification (
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
    INDEX idx_created_time (created_time),

    FOREIGN KEY (user_id) REFERENCES sys_user(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='通知消息表';
```

#### 7. 系统日志表 (sys_log)
```sql
CREATE TABLE sys_log (
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
```

## 核心业务实现

### 1. 认证授权模块

#### JWT工具类
```java
@Component
public class JwtUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("role", getUserRole(userDetails));
        claims.put("userId", getUserId(userDetails));
        return createToken(claims, userDetails.getUsername());
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public String getUsernameFromToken(String token) {
        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        return getClaimFromToken(token, claims -> claims.get("role", String.class));
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }
}
```

#### 用户认证服务
```java
@Service
@Transactional
public class AuthService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public LoginResponse login(LoginRequest request) {
        // 1. 认证用户
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                request.getUsername(),
                request.getPassword()
            )
        );

        // 2. 获取用户信息
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername())
            .orElseThrow(() -> new BusinessException("用户不存在"));

        // 3. 生成JWT Token
        String token = jwtUtil.generateToken(userDetails);

        // 4. 更新最后登录时间
        user.setLastLoginTime(new Date());
        userRepository.save(user);

        // 5. 存储Token到Redis
        String redisKey = "auth:token:" + user.getId();
        redisTemplate.opsForValue().set(redisKey, token, Duration.ofHours(24));

        // 6. 构建返回数据
        return LoginResponse.builder()
            .token(token)
            .tokenType("Bearer")
            .expiresIn(86400) // 24小时
            .userInfo(UserResponse.from(user))
            .build();
    }

    public void logout(String token) {
        String username = jwtUtil.getUsernameFromToken(token);
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("用户不存在"));

        // 从Redis中删除Token
        String redisKey = "auth:token:" + user.getId();
        redisTemplate.delete(redisKey);

        // 将Token加入黑名单
        String blacklistKey = "auth:blacklist:" + token;
        redisTemplate.opsForValue().set(blacklistKey, "1", Duration.ofHours(24));
    }

    public void changePassword(ChangePasswordRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
            .orElseThrow(() -> new BusinessException("用户不存在"));

        // 验证原密码
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new BusinessException("原密码错误");
        }

        // 更新密码
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        // 清除用户所有Token
        String pattern = "auth:token:" + user.getId() + "*";
        redisTemplate.delete(redisTemplate.keys(pattern));
    }
}
```

### 2. 学生管理模块

#### 学生服务实现
```java
@Service
@Transactional
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private ClassRepository classRepository;

    @Autowired
    private ParentStudentRepository parentStudentRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ExcelUtil excelUtil;

    @Autowired
    private MinIOService minIOService;

    public Page<StudentResponse> getStudentList(StudentQueryRequest request) {
        // 1. 构建查询条件
        Specification<Student> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (StringUtils.hasText(request.getKeyword())) {
                String keyword = "%" + request.getKeyword() + "%";
                predicates.add(cb.or(
                    cb.like(root.get("name"), keyword),
                    cb.like(root.get("studentId"), keyword)
                ));
            }

            if (request.getClassId() != null) {
                predicates.add(cb.equal(root.get("classId"), request.getClassId()));
            }

            if (request.getStatus() != null) {
                predicates.add(cb.equal(root.get("status"), request.getStatus()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 2. 分页查询
        Pageable pageable = PageRequest.of(
            request.getPage() - 1,
            request.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdTime")
        );

        Page<Student> studentPage = studentRepository.findAll(spec, pageable);

        // 3. 转换为响应对象
        return studentPage.map(StudentResponse::from);
    }

    @Transactional
    public ExcelImportResponse importStudentExcel(MultipartFile file, Long classId) {
        try {
            // 1. 验证班级是否存在
            Class clazz = classRepository.findById(classId)
                .orElseThrow(() -> new BusinessException("班级不存在"));

            // 2. 解析Excel文件
            List<StudentExcelData> excelDataList = excelUtil.parseStudentExcel(file);

            // 3. 验证数据
            validateStudentExcelData(excelDataList);

            // 4. 保存学生数据
            List<Student> savedStudents = new ArrayList<>();
            List<String> errors = new ArrayList<>();

            for (StudentExcelData excelData : excelDataList) {
                try {
                    Student student = convertExcelDataToStudent(excelData, classId);
                    Student savedStudent = studentRepository.save(student);
                    savedStudents.add(savedStudent);

                    // 如果Excel中包含家长信息，同时创建家长账户
                    if (excelData.getParentName() != null && excelData.getParentPhone() != null) {
                        createParentAccount(excelData, savedStudent);
                    }

                } catch (Exception e) {
                    errors.add(String.format("第%d行: %s", excelData.getRowNum(), e.getMessage()));
                }
            }

            // 5. 更新班级学生数量
            clazz.setStudentCount((int) studentRepository.countByClassId(classId));
            classRepository.save(clazz);

            // 6. 存储Excel文件
            String fileUrl = minIOService.uploadFile(file, "excel/student");

            return ExcelImportResponse.builder()
                .totalCount(excelDataList.size())
                .successCount(savedStudents.size())
                .failCount(errors.size())
                .errors(errors)
                .fileUrl(fileUrl)
                .build();

        } catch (Exception e) {
            throw new BusinessException("Excel导入失败: " + e.getMessage());
        }
    }

    private void validateStudentExcelData(List<StudentExcelData> dataList) {
        Set<String> studentIds = new HashSet<>();
        Set<String> idCards = new HashSet<>();

        for (StudentExcelData data : dataList) {
            // 验证学号唯一性
            if (studentIds.contains(data.getStudentId())) {
                throw new BusinessException(String.format("学号 %s 重复", data.getStudentId()));
            }
            studentIds.add(data.getStudentId());

            // 验证身份证唯一性
            if (idCards.contains(data.getIdCard())) {
                throw new BusinessException(String.format("身份证号 %s 重复", data.getIdCard()));
            }
            idCards.add(data.getIdCard());

            // 验证身份证格式
            if (!isValidIdCard(data.getIdCard())) {
                throw new BusinessException(String.format("身份证号 %s 格式错误", data.getIdCard()));
            }
        }

        // 检查与数据库中的重复
        List<String> existingStudentIds = studentRepository.findExistingStudentIds(studentIds);
        if (!existingStudentIds.isEmpty()) {
            throw new BusinessException("学号已存在: " + String.join(", ", existingStudentIds));
        }

        List<String> existingIdCards = studentRepository.findExistingIdCards(idCards);
        if (!existingIdCards.isEmpty()) {
            throw new BusinessException("身份证号已存在: " + String.join(", ", existingIdCards));
        }
    }

    private void createParentAccount(StudentExcelData excelData, Student student) {
        // 检查家长是否已存在
        Optional<User> existingParent = userRepository.findByPhone(excelData.getParentPhone());
        if (existingParent.isPresent()) {
            // 关联现有家长
            ParentStudent relation = new ParentStudent();
            relation.setParentId(existingParent.get().getId());
            relation.setStudentId(student.getId());
            relation.setRelation(determineRelation(excelData.getParentRelation()));
            relation.setPrimary(true);
            parentStudentRepository.save(relation);
        } else {
            // 创建新家长账户
            User parent = new User();
            parent.setUsername(student.getIdCard().substring(6)); // 身份证后6位作为初始用户名
            parent.setPassword(passwordEncoder.encode("123456")); // 初始密码
            parent.setRealName(excelData.getParentName());
            parent.setPhone(excelData.getParentPhone());
            parent.setRole(User.Role.PARENT);
            parent.setStatus(User.Status.ACTIVE);

            User savedParent = userRepository.save(parent);

            // 关联学生
            ParentStudent relation = new ParentStudent();
            relation.setParentId(savedParent.getId());
            relation.setStudentId(student.getId());
            relation.setRelation(determineRelation(excelData.getParentRelation()));
            relation.setPrimary(true);
            parentStudentRepository.save(relation);
        }
    }
}
```

### 3. 档案记录模块

#### 档案服务实现
```java
@Service
@Transactional
public class RecordService {

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private NotificationService notificationService;

    @Autowired
    private MinIOService minIOService;

    @Autowired
    private WebSocketService webSocketService;

    public RecordResponse createRecord(RecordCreateRequest request) {
        // 1. 验证学生是否存在
        Student student = studentRepository.findById(request.getStudentId())
            .orElseThrow(() -> new BusinessException("学生不存在"));

        // 2. 获取当前教师信息
        User currentUser = getCurrentUser();
        if (currentUser.getRole() != User.Role.TEACHER) {
            throw new BusinessException("只有教师可以创建档案记录");
        }

        // 3. 处理图片上传
        List<String> imageUrls = new ArrayList<>();
        if (request.getImages() != null && !request.getImages().isEmpty()) {
            for (MultipartFile image : request.getImages()) {
                String imageUrl = minIOService.uploadFile(image, "record/images");
                imageUrls.add(imageUrl);
            }
        }

        // 4. 创建档案记录
        StudentRecord record = StudentRecord.builder()
            .studentId(request.getStudentId())
            .teacherId(currentUser.getId())
            .type(request.getType())
            .category(request.getCategory())
            .title(request.getTitle())
            .content(request.getContent())
            .images(imageUrls)
            .tags(request.getTags())
            .importance(request.getImportance())
            .isPublic(request.getIsPublic() != null ? request.getIsPublic() : true)
            .parentNotified(false)
            .build();

        StudentRecord savedRecord = recordRepository.save(record);

        // 5. 如果记录公开，发送通知给家长
        if (savedRecord.getIsPublic()) {
            notifyParents(savedRecord, student);
        }

        // 6. 记录操作日志
        logService.logOperation("CREATE_RECORD",
            String.format("创建学生档案记录: %s - %s", student.getName(), savedRecord.getTitle()));

        return RecordResponse.from(savedRecord);
    }

    private void notifyParents(StudentRecord record, Student student) {
        // 1. 获取学生家长列表
        List<User> parents = parentStudentRepository.findParentsByStudentId(student.getId());

        // 2. 创建通知消息
        for (User parent : parents) {
            Notification notification = Notification.builder()
                .userId(parent.getId())
                .title(String.format("%s的%s记录", student.getName(), record.getCategory()))
                .content(String.format("%s老师记录了一条关于%s的%s信息: %s",
                    getCurrentUser().getRealName(),
                    student.getName(),
                    record.getCategory(),
                    record.getContent().length() > 50 ?
                        record.getContent().substring(0, 50) + "..." : record.getContent()))
                .type(Notification.Type.RECORD)
                .relatedId(record.getId())
                .isRead(false)
                .build();

            notificationService.saveNotification(notification);

            // 3. 实时推送通知
            try {
                webSocketService.sendToUser(parent.getId(),
                    WebSocketMessage.builder()
                        .type("NEW_RECORD")
                        .data(Map.of(
                            "recordId", record.getId(),
                            "studentName", student.getName(),
                            "title", record.getTitle(),
                            "content", record.getContent(),
                            "images", record.getImages()
                        ))
                        .build());
            } catch (Exception e) {
                log.warn("实时通知推送失败: {}", e.getMessage());
            }
        }

        // 4. 更新记录通知状态
        record.setNotified(true);
        recordRepository.save(record);
    }

    public Page<RecordResponse> getRecordList(RecordQueryRequest request) {
        // 1. 获取当前用户信息
        User currentUser = getCurrentUser();

        // 2. 构建查询条件
        Specification<StudentRecord> spec = (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            // 根据用户角色过滤数据
            switch (currentUser.getRole()) {
                case PARENT:
                    // 家长只能查看自己孩子的记录
                    List<Long> studentIds = parentStudentRepository.findStudentIdsByParentId(currentUser.getId());
                    if (studentIds.isEmpty()) {
                        predicates.add(cb.disjunction()); // 返回空结果
                    } else {
                        predicates.add(root.get("studentId").in(studentIds));
                    }
                    predicates.add(cb.equal(root.get("isPublic"), true));
                    break;

                case TEACHER:
                    // 教师只能查看自己班级学生的记录
                    if (request.getStudentId() != null) {
                        // 如果指定了学生，验证是否是自己班级的学生
                        Student student = studentRepository.findById(request.getStudentId())
                            .orElse(null);
                        if (student == null || !isTeacherOfClass(currentUser.getId(), student.getClassId())) {
                            predicates.add(cb.disjunction());
                        }
                    } else {
                        // 查询自己班级所有学生的记录
                        List<Long> classStudentIds = studentRepository.findStudentIdsByTeacherId(currentUser.getId());
                        if (!classStudentIds.isEmpty()) {
                            predicates.add(root.get("studentId").in(classStudentIds));
                        }
                    }
                    break;

                case SCHOOL_ADMIN:
                case ADMIN:
                    // 管理员可以查看所有记录
                    break;
            }

            // 其他查询条件
            if (request.getStudentId() != null) {
                predicates.add(cb.equal(root.get("studentId"), request.getStudentId()));
            }

            if (request.getType() != null) {
                predicates.add(cb.equal(root.get("type"), request.getType()));
            }

            if (StringUtils.hasText(request.getCategory())) {
                predicates.add(cb.equal(root.get("category"), request.getCategory()));
            }

            if (request.getStartDate() != null) {
                predicates.add(cb.greaterThanOrEqualTo(root.get("createdTime"), request.getStartDate()));
            }

            if (request.getEndDate() != null) {
                predicates.add(cb.lessThanOrEqualTo(root.get("createdTime"), request.getEndDate()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };

        // 3. 分页查询
        Pageable pageable = PageRequest.of(
            request.getPage() - 1,
            request.getPageSize(),
            Sort.by(Sort.Direction.DESC, "createdTime")
        );

        Page<StudentRecord> recordPage = recordRepository.findAll(spec, pageable);

        // 4. 转换为响应对象
        return recordPage.map(RecordResponse::from);
    }
}
```

### 4. 实时通信模块

#### WebSocket配置
```java
@Configuration
@EnableWebSocket
public class WebSocketConfig implements WebSocketConfigurer {

    @Autowired
    private JwtHandshakeInterceptor jwtHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(new WebSocketHandler(), "/ws")
                .addInterceptors(jwtHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}

@Component
public class JwtHandshakeInterceptor implements HandshakeInterceptor {

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                 ServerHttpResponse response,
                                 WebSocketHandler wsHandler,
                                 Map<String, Object> attributes) throws Exception {

        // 从请求参数中获取token
        String query = request.getURI().getQuery();
        if (StringUtils.hasText(query)) {
            String[] params = query.split("&");
            for (String param : params) {
                if (param.startsWith("token=")) {
                    String token = param.substring(6);

                    // 验证token
                    try {
                        String username = jwtUtil.getUsernameFromToken(token);
                        attributes.put("username", username);
                        return true;
                    } catch (Exception e) {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                             ServerHttpResponse response,
                             WebSocketHandler wsHandler,
                             Exception exception) {
        // 握手后处理
    }
}
```

#### WebSocket处理器
```java
@Component
public class WebSocketHandler extends TextWebSocketHandler {

    private static final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();
    private static final Map<String, String> userSessions = new ConcurrentHashMap<>();

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        String username = (String) session.getAttributes().get("username");
        if (username != null) {
            sessions.put(session.getId(), session);
            userSessions.put(username, session.getId());

            // 发送连接成功消息
            sendMessage(session, WebSocketMessage.builder()
                .type("CONNECTED")
                .data(Map.of("message", "连接成功"))
                .build());
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        sessions.remove(session.getId());

        // 移除用户会话映射
        userSessions.entrySet().removeIf(entry -> entry.getValue().equals(session.getId()));
    }

    public void sendToUser(Long userId, WebSocketMessage message) {
        User user = userRepository.findById(userId).orElse(null);
        if (user != null) {
            sendToUser(user.getUsername(), message);
        }
    }

    public void sendToUser(String username, WebSocketMessage message) {
        String sessionId = userSessions.get(username);
        if (sessionId != null) {
            WebSocketSession session = sessions.get(sessionId);
            if (session != null && session.isOpen()) {
                sendMessage(session, message);
            }
        }
    }

    private void sendMessage(WebSocketSession session, WebSocketMessage message) {
        try {
            String jsonMessage = objectMapper.writeValueAsString(message);
            session.sendMessage(new TextMessage(jsonMessage));
        } catch (Exception e) {
            log.error("发送WebSocket消息失败", e);
        }
    }
}
```

## 系统配置

### 应用配置文件 (application.yml)
```yaml
server:
  port: 8080
  servlet:
    context-path: /api

spring:
  profiles:
    active: dev

  application:
    name: home-school-backend

  datasource:
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://localhost:3306/home_school?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:123456}
    hikari:
      maximum-pool-size: 20
      minimum-idle: 5
      idle-timeout: 300000
      connection-timeout: 20000

  jpa:
    hibernate:
      ddl-auto: validate
    show-sql: false
    properties:
      hibernate:
        dialect: org.hibernate.dialect.MySQL8Dialect
        format_sql: true

  redis:
    host: ${REDIS_HOST:localhost}
    port: ${REDIS_PORT:6379}
    password: ${REDIS_PASSWORD:}
    database: 0
    timeout: 3000ms
    lettuce:
      pool:
        max-active: 20
        max-idle: 10
        min-idle: 5
        max-wait: 3000ms

  servlet:
    multipart:
      max-file-size: 10MB
      max-request-size: 10MB

  cache:
    type: redis
    redis:
      time-to-live: 3600000

# JWT配置
jwt:
  secret: ${JWT_SECRET:mySecretKey123456789012345678901234567890}
  expiration: 86400000  # 24小时

# MinIO配置
minio:
  endpoint: ${MINIO_ENDPOINT:http://localhost:9000}
  access-key: ${MINIO_ACCESS_KEY:minioadmin}
  secret-key: ${MINIO_SECRET_KEY:minioadmin}
  bucket-name: home-school

# 日志配置
logging:
  level:
    com.school.cooperation: INFO
    org.springframework.security: DEBUG
  pattern:
    console: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
    file: "%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n"
  file:
    name: logs/home-school-backend.log
    max-size: 100MB
    max-history: 30

# 监控配置
management:
  endpoints:
    web:
      exposure:
        include: health,info,metrics,prometheus
  endpoint:
    health:
      show-details: when-authorized
```

### 安全配置
```java
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true)
public class SecurityConfig {

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(authz -> authz
                // 公开接口
                .requestMatchers("/auth/login", "/auth/parent-login").permitAll()
                .requestMatchers("/ws/**").permitAll()
                .requestMatchers("/actuator/health").permitAll()
                // 管理员接口
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/school-admin/**").hasRole("SCHOOL_ADMIN")
                // 教师接口
                .requestMatchers("/teacher/**").hasRole("TEACHER")
                // 家长接口
                .requestMatchers("/parent/**").hasRole("PARENT")
                // 其他接口需要认证
                .anyRequest().authenticated())
            .exceptionHandling(exceptions -> exceptions
                .authenticationEntryPoint(customAuthenticationEntryPoint))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
```

## 部署架构

### Docker配置 (Dockerfile)
```dockerfile
FROM openjdk:17-jre-slim

WORKDIR /app

COPY target/home-school-backend-*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### Docker编排 (docker-compose.yml)
```yaml
version: '3.8'

services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - SPRING_PROFILES_ACTIVE=prod
      - DB_HOST=mysql
      - DB_USERNAME=root
      - DB_PASSWORD=123456
      - REDIS_HOST=redis
      - MINIO_ENDPOINT=http://minio:9000
    depends_on:
      - mysql
      - redis
      - minio
    volumes:
      - ./logs:/app/logs
    restart: unless-stopped

  mysql:
    image: mysql:8.0
    environment:
      - MYSQL_ROOT_PASSWORD=123456
      - MYSQL_DATABASE=home_school
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql/init.sql:/docker-entrypoint-initdb.d/init.sql
    ports:
      - "3306:3306"
    restart: unless-stopped

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"
    volumes:
      - redis_data:/data
    restart: unless-stopped

  minio:
    image: minio/minio:latest
    command: server /data --console-address ":9001"
    environment:
      - MINIO_ROOT_USER=minioadmin
      - MINIO_ROOT_PASSWORD=minioadmin
    volumes:
      - minio_data:/data
    ports:
      - "9000:9000"
      - "9001:9001"
    restart: unless-stopped

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
      - "443:443"
    volumes:
      - ./nginx/nginx.conf:/etc/nginx/nginx.conf
      - ./nginx/ssl:/etc/nginx/ssl
    depends_on:
      - app
    restart: unless-stopped

volumes:
  mysql_data:
  redis_data:
  minio_data:
```

## 开发规范

### 代码规范
- 使用Alibaba Java Coding Guidelines
- 统一的命名规范（驼峰命名法）
- 完善的JavaDoc注释
- 合理的方法和类长度控制

### Git规范
```
feat: 新功能
fix: 修复bug
docs: 文档更新
style: 代码格式调整
refactor: 代码重构
test: 测试相关
chore: 构建或工具相关
```

### API设计规范
- RESTful API设计
- 统一的响应格式
- 完善的错误处理
- 接口版本管理

这个Java后端技术架构为您的家校协同教育系统提供了完整的解决方案，包含了所有核心功能的实现细节，确保系统的稳定性、安全性和可扩展性。