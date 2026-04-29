#!/bin/bash

echo "======================================="
echo "启动 Vue2 + Java + MySQL 全栈项目"
echo "======================================="

# 检查数据库
echo "1. 检查数据库连接..."
mysql --version > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ MySQL 已安装"
else
    echo "✗ MySQL 未安装，请先安装MySQL"
    echo "   macOS: brew install mysql"
    echo "   Ubuntu: sudo apt install mysql-server"
    exit 1
fi

# 检查Java
echo "2. 检查Java环境..."
java --version > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ Java 已安装"
else
    echo "✗ Java 未安装，请先安装Java 17+"
    echo "   macOS: brew install openjdk@17"
    echo "   Ubuntu: sudo apt install openjdk-17-jdk"
    exit 1
fi

# 检查Node.js
echo "3. 检查Node.js环境..."
node --version > /dev/null 2>&1
if [ $? -eq 0 ]; then
    echo "✓ Node.js 已安装"
else
    echo "✗ Node.js 未安装，请先安装Node.js"
    echo "   macOS: brew install node"
    echo "   Ubuntu: sudo apt install nodejs npm"
    exit 1
fi

echo ""
echo "4. 启动后端服务..."
cd backend
if [ -f "target/vue2-java-mysql-backend-1.0.0.jar" ]; then
    echo "使用已编译的JAR文件启动..."
    java -jar target/vue2-java-mysql-backend-1.0.0.jar &
else
    echo "请先编译后端项目:"
    echo "  cd backend && mvn clean package"
    echo "然后重新运行此脚本"
    exit 1
fi
BACKEND_PID=$!
cd ..

echo "5. 启动前端服务..."
cd frontend
if [ -d "node_modules" ]; then
    echo "✓ 依赖已安装"
else
    echo "安装前端依赖..."
    npm install
fi

echo "启动前端开发服务器..."
npm run serve &
FRONTEND_PID=$!
cd ..

echo ""
echo "======================================="
echo "服务启动完成!"
echo ""
echo "前端: http://localhost:8099"
echo "后端API: http://localhost:3000"
echo ""
echo "按 Ctrl+C 停止所有服务"
echo "======================================="

# 捕获Ctrl+C信号
trap 'kill $BACKEND_PID $FRONTEND_PID; echo "服务已停止"; exit' INT

# 等待
wait