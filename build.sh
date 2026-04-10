#!/bin/bash

echo "======================================="
echo "构建 Vue2 + Java + MySQL 全栈项目"
echo "======================================="

# 构建后端
echo "1. 构建后端Java项目..."
cd backend
if command -v mvn &> /dev/null; then
    mvn clean package
    if [ $? -eq 0 ]; then
        echo "✓ 后端构建成功"
    else
        echo "✗ 后端构建失败"
        exit 1
    fi
else
    echo "✗ Maven未安装，跳过后端构建"
    echo "   macOS: brew install maven"
    echo "   Ubuntu: sudo apt install maven"
fi
cd ..

# 构建前端
echo "2. 构建前端Vue项目..."
cd frontend
if [ -d "node_modules" ]; then
    echo "✓ 依赖已存在"
else
    echo "安装前端依赖..."
    npm install
fi

echo "构建前端..."
npm run build
if [ $? -eq 0 ]; then
    echo "✓ 前端构建成功"
    echo "  构建结果在: frontend/dist/"
else
    echo "✗ 前端构建失败"
    exit 1
fi
cd ..

echo ""
echo "======================================="
echo "项目构建完成!"
echo ""
echo "后端JAR文件: backend/target/vue2-java-mysql-backend-1.0.0.jar"
echo "前端静态文件: frontend/dist/"
echo ""
echo "运行项目: ./start.sh"
echo "======================================="