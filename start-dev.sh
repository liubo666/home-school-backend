#!/bin/bash

# 开发环境启动脚本

echo "=========================================="
echo "家校协同教育系统后端开发环境启动"
echo "=========================================="

# 检查Docker是否安装
if ! command -v docker &> /dev/null; then
    echo "Docker未安装，请先安装Docker"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo "Docker Compose未安装，请先安装Docker Compose"
    exit 1
fi

# 检查Maven是否安装
if ! command -v mvn &> /dev/null; then
    echo "Maven未安装，请先安装Maven"
    exit 1
fi

echo "1. 启动开发环境依赖服务..."
docker-compose -f docker-compose-dev.yml up -d

echo "2. 等待数据库启动..."
sleep 10

echo "3. 编译项目..."
mvn clean compile -q

if [ $? -ne 0 ]; then
    echo "项目编译失败"
    exit 1
fi

echo "4. 运行数据库迁移..."
mvn flyway:migrate -q

if [ $? -ne 0 ]; then
    echo "数据库迁移失败"
    exit 1
fi

echo "5. 启动应用..."
mvn spring-boot:run -Dspring-boot.run.profiles=dev

echo "开发环境启动完成！"
echo "应用地址: http://localhost:8080/api"
echo "API文档: http://localhost:8080/api/doc.html"
echo "健康检查: http://localhost:8080/api/health"
echo ""
echo "数据库连接信息:"
echo "  - MySQL: localhost:3306"
echo "  - 数据库: home_school"
echo "  - 用户名: root"
echo "  - 密码: 123456"
echo ""
echo "Redis连接信息:"
echo "  - 地址: localhost:6379"
echo "  - 密码: 无"
echo ""
echo "MinIO连接信息:"
echo "  - 控制台: http://localhost:9001"
echo "  - 用户名: minioadmin"
echo "  - 密码: minioadmin"