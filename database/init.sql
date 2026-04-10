-- 创建数据库
CREATE DATABASE IF NOT EXISTS vue2_java_mysql DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- 使用数据库
USE vue2_java_mysql;

-- 创建用户表
CREATE TABLE IF NOT EXISTS users (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    username VARCHAR(20) NOT NULL UNIQUE,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_username (username),
    INDEX idx_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- 插入示例数据
INSERT INTO users (username, email, password, created_at, updated_at) VALUES
('admin', 'admin@example.com', 'password123', NOW(), NOW()),
('user1', 'user1@example.com', 'password123', NOW(), NOW()),
('user2', 'user2@example.com', 'password123', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 创建数据库用户（如果需要）
-- CREATE USER IF NOT EXISTS 'vue2_user'@'localhost' IDENTIFIED BY 'password123';
-- GRANT ALL PRIVILEGES ON vue2_java_mysql.* TO 'vue2_user'@'localhost';
-- FLUSH PRIVILEGES;