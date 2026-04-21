-- 角色增幅配置表
-- 用于存储每个角色选择的装备增幅/强化配置
CREATE TABLE IF NOT EXISTS character_amplify (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    character_id BIGINT NOT NULL COMMENT '角色ID',
    equipment_part VARCHAR(20) NOT NULL COMMENT '装备部位',
    amplify_type VARCHAR(10) NOT NULL DEFAULT '增幅' COMMENT '增幅类型：增幅/强化',
    amplify_level INT NOT NULL DEFAULT 0 COMMENT '增幅等级(1-15)',
    fame INT DEFAULT 0 COMMENT '名望值',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_character_part (character_id, equipment_part)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色增幅配置表';

-- 添加索引
CREATE INDEX idx_character_id ON character_amplify(character_id);
