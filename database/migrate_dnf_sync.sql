-- DNF角色信息同步功能 - 数据库迁移脚本
-- 执行: source database/migrate_dnf_sync.sql

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;

USE vue2_java_mysql;

-- 新增角色表字段：DNF同步相关
ALTER TABLE characters
    ADD COLUMN server_name VARCHAR(50) DEFAULT '' COMMENT '区服名称：跨一/跨二/跨三等' AFTER character_name,
    ADD COLUMN fame_value INT DEFAULT 0 COMMENT '名望值（未转职/1~99/100/110/115）' AFTER dark_element,
    ADD COLUMN level INT DEFAULT 1 COMMENT '角色等级' AFTER server_name,
    ADD COLUMN adventure_name VARCHAR(50) DEFAULT '' COMMENT '冒险团名称' AFTER level,
    ADD COLUMN equipment_summary TEXT COMMENT '装备概要JSON：装备名称/部位/品级/强化/增幅/附魔' AFTER avatar_url,
    ADD COLUMN last_sync_time DATETIME COMMENT '最后同步时间' AFTER equipment_summary,
    ADD COLUMN sync_status VARCHAR(20) DEFAULT 'idle' COMMENT '同步状态：idle/syncing/error' AFTER last_sync_time,
    ADD COLUMN sync_error_msg TEXT COMMENT '同步错误信息' AFTER sync_status;

-- 创建同步日志表
CREATE TABLE IF NOT EXISTS character_sync_logs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    character_id BIGINT NOT NULL COMMENT '角色ID',
    sync_type VARCHAR(20) NOT NULL COMMENT '同步类型：manual/daily',
    sync_status VARCHAR(20) NOT NULL COMMENT '同步结果：success/failed',
    request_params TEXT COMMENT '请求参数',
    response_data TEXT COMMENT '响应数据（截取）',
    error_msg TEXT COMMENT '错误信息',
    duration_ms INT COMMENT '耗时（毫秒）',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    INDEX idx_character_id (character_id),
    INDEX idx_created_at (created_at)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色同步日志表';
