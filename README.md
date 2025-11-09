# 家校协同教育系统后端

基于Spring Boot 3.2.x + MySQL 8.0 + Redis 7.x的家校协同教育系统后端服务。

## 技术栈

- **框架**: Spring Boot 3.2.x
- **语言**: Java 17
- **数据库**: MySQL 8.0
- **缓存**: Redis 7.x
- **ORM**: Spring Data JPA + MyBatis Plus
- **安全**: Spring Security + JWT
- **文档**: Knife4j (Swagger)
- **文件存储**: MinIO
- **Excel处理**: Apache POI + EasyExcel
- **工具**: Hutool
- **监控**: Spring Boot Actuator
- **测试**: JUnit 5 + TestContainers

## 项目结构

```
src/main/java/com/school/cooperation/
├── HomeSchoolBackendApplication.java    # 主启动类
├── config/                             # 配置类
│   ├── SecurityConfig.java             # 安全配置
│   ├── RedisConfig.java                # Redis配置
│   ├── MinIOConfig.java                # MinIO配置
│   └── WebSocketConfig.java            # WebSocket配置
├── controller/                         # 控制器层
│   ├── auth/                           # 认证相关
│   ├── admin/                          # 管理员模块
│   ├── teacher/                        # 班主任模块
│   └── parent/                         # 家长模块
├── service/                            # 服务层
│   ├── auth/                           # 认证服务
│   ├── admin/                          # 管理员服务
│   ├── teacher/                        # 班主任服务
│   └── parent/                         # 家长服务
├── repository/                         # 数据访问层
├── entity/                             # 实体类
├── dto/                                # 数据传输对象
│   ├── request/                        # 请求DTO
│   └── response/                       # 响应DTO
├── common/                             # 公共组件
│   ├── constant/                       # 常量类
│   ├── exception/                      # 异常类
│   ├── utils/                          # 工具类
│   └── aspect/                         # 切面类
└── security/                           # 安全相关
```

## 快速开始

### 环境要求

- JDK 17+
- Maven 3.6+
- MySQL 8.0+
- Redis 7.x+
- MinIO (可选，用于文件存储)

### 数据库配置

1. 创建数据库
```sql
CREATE DATABASE home_school CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

2. 修改配置文件
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/home_school?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Shanghai
    username: root
    password: your_password
```

### Redis配置

```yaml
spring:
  data:
    redis:
      host: localhost
      port: 6379
      password: your_password
```

### 运行项目

1. 克隆项目
```bash
git clone <repository-url>
cd home-school-backend
```

2. 安装依赖
```bash
mvn clean install
```

3. 启动应用
```bash
mvn spring-boot:run
```

### 访问地址

- 应用地址: http://localhost:8080/api
- API文档: http://localhost:8080/api/doc.html
- 健康检查: http://localhost:8080/api/health
- 监控端点: http://localhost:8080/api/actuator

## 核心功能

### 1. 用户认证授权
- JWT Token认证
- 基于角色的权限控制(RBAC)
- 四级用户角色：管理员、学校管理员、班主任、家长

### 2. 用户管理
- 用户注册/登录
- 用户信息管理
- 密码修改/重置
- 用户状态管理

### 3. 学生管理
- 学生信息CRUD
- 学生搜索/筛选
- 班级学生管理
- Excel批量导入导出

### 4. 班级管理
- 班级信息管理
- 班主任分配
- 学生班级分配
- 班级统计信息

### 5. 档案记录
- 学生档案创建/管理
- 表扬/批评记录
- 图片附件上传
- 记录分类/标签

### 6. 实时通知
- WebSocket实时推送
- 消息通知管理
- 通知统计查询

### 7. 文件管理
- 文件上传/下载
- 图片处理
- MinIO对象存储

## 开发规范

### 代码规范
- 遵循Alibaba Java Coding Guidelines
- 使用统一的命名规范
- 完善的注释文档
- 合理的方法和类长度

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

## 测试

### 运行单元测试
```bash
mvn test
```

### 运行集成测试
```bash
mvn verify
```

### 生成测试报告
```bash
mvn jacoco:report
```

## 部署

### 构建项目
```bash
mvn clean package -P prod
```

### Docker部署
```bash
docker build -t home-school-backend .
docker run -p 8080:8080 home-school-backend
```

## 配置说明

### 环境配置
- `application.yml`: 主配置文件
- `application-dev.yml`: 开发环境配置
- `application-prod.yml`: 生产环境配置

### 关键配置项
- 数据库连接配置
- Redis连接配置
- JWT密钥配置
- MinIO配置
- 文件上传配置

## 监控

### Actuator端点
- `/actuator/health`: 健康检查
- `/actuator/info`: 应用信息
- `/actuator/metrics`: 性能指标
- `/actuator/prometheus`: Prometheus监控

### 日志配置
- 日志级别可配置
- 支持文件滚动
- 日志格式统一

## 开发进度

- [x] 项目基础搭建
- [x] 数据库设计
- [x] 用户认证授权
- [ ] 用户管理模块
- [ ] 学生管理模块
- [ ] 班级管理模块
- [ ] 档案记录模块
- [ ] 实时通知模块

## 常见问题

### Q: 如何修改JWT密钥？
A: 在`application.yml`中修改`jwt.secret`配置

### Q: 如何配置文件存储？
A: 修改`minio`相关配置，或使用本地存储

### Q: 如何修改数据库连接？
A: 修改`spring.datasource`相关配置

## 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'feat: Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 联系方式

- 项目链接: [https://github.com/your-username/home-school-backend](https://github.com/your-username/home-school-backend)
- 问题反馈: [Issues](https://github.com/your-username/home-school-backend/issues)