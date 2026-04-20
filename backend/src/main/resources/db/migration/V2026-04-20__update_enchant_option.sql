-- 更新附魔选项表：移除属性类型和是否新赛季，添加名望字段，扩大属性值长度
-- V2026-04-20__update_enchant_option.sql

-- 添加名望字段
ALTER TABLE enchant_options ADD COLUMN IF NOT EXISTS fame INT COMMENT '名望值' AFTER sort_order;

-- 移除不再使用的字段
ALTER TABLE enchant_options DROP COLUMN IF EXISTS attribute_type;
ALTER TABLE enchant_options DROP COLUMN IF EXISTS is_latest;

-- 扩大属性值字段长度以支持多条属性值
ALTER TABLE enchant_options MODIFY COLUMN attribute_value VARCHAR(200) COMMENT '属性值（如 +30, +25, +8% 等，支持多条，用逗号分隔）';

-- 更新package_type注释
ALTER TABLE enchant_options MODIFY COLUMN package_type VARCHAR(50) COMMENT '礼包类型：春节宝珠/国庆宝珠/其他附魔';
