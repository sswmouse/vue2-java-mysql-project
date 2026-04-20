-- 角色附魔配置表
-- V2026-04-20__add_character_enchant.sql

CREATE TABLE IF NOT EXISTS character_enchant (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    character_id BIGINT NOT NULL COMMENT '角色ID',
    equipment_part VARCHAR(20) NOT NULL COMMENT '装备部位：武器/头肩/上衣/下装/鞋/腰带/项链/手镯/戒指/辅助装备/魔法石/耳环',
    enchant_option_id BIGINT COMMENT '附魔选项ID',
    enchant_name VARCHAR(100) COMMENT '附魔名称',
    attribute_value VARCHAR(200) COMMENT '属性值（如 +30, +25, +8% 等，支持多条，用逗号分隔）',
    package_type VARCHAR(50) COMMENT '礼包类型：春节宝珠/国庆宝珠/其他附魔',
    fame INT COMMENT '名望值',
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY uk_character_part (character_id, equipment_part),
    INDEX idx_character_id (character_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色附魔配置表';
