-- 迁移: 添加用户主题偏好字段
-- 时间: 2026-04-14

-- 添加theme字段到users表
ALTER TABLE users ADD COLUMN theme VARCHAR(50) DEFAULT 'dnf' COMMENT '用户主题偏好';

-- 将现有用户的主题设置为默认的dnf
UPDATE users SET theme = 'dnf' WHERE theme IS NULL;
