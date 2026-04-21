-- 宠物表
CREATE TABLE IF NOT EXISTS `pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '宠物名称',
    `year` INT NOT NULL COMMENT '年份',
    `type` VARCHAR(50) NOT NULL COMMENT '宠物类型：普通/至尊/耕耘',
    `sub_type` VARCHAR(50) DEFAULT NULL COMMENT '子类型',
    `four_stats` INT DEFAULT 0 COMMENT '四维',
    `four_stats_alt` VARCHAR(50) DEFAULT NULL COMMENT '四维备选（如力智/体精分开）',
    `crit_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '暴击率(%)',
    `elemental_bonus` INT DEFAULT 0 COMMENT '属强',
    `attack_speed` DECIMAL(5,2) DEFAULT 0 COMMENT '三速(%)',
    `mp_bonus` DECIMAL(5,2) DEFAULT 0 COMMENT 'MP加成(%)',
    `skill_level_range` VARCHAR(50) DEFAULT NULL COMMENT '技能等级范围（如1~95+1）',
    `gain_bonus` DECIMAL(5,2) DEFAULT 0 COMMENT '增益量(%)',
    `attack_strength` DECIMAL(5,2) DEFAULT 0 COMMENT '攻击强化(%)',
    `white_damage` DECIMAL(5,2) DEFAULT 0 COMMENT '白字伤害(%)',
    `extra_yellow` DECIMAL(5,2) DEFAULT 0 COMMENT '额外黄字(%)',
    `three_attack` DECIMAL(5,2) DEFAULT 0 COMMENT '三攻(%)',
    `cd_reduction` DECIMAL(5,2) DEFAULT 0 COMMENT 'CD缩减(%)',
    `fame` INT DEFAULT 0 COMMENT '名望',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '额外描述',
    `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_year` (`year`),
    INDEX `idx_type` (`type`),
    INDEX `idx_fame` (`fame`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='宠物表';

-- 角色宠物关联表
CREATE TABLE IF NOT EXISTS `character_pet` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `character_id` BIGINT NOT NULL COMMENT '角色ID',
    `pet_id` BIGINT NOT NULL COMMENT '宠物ID',
    `is_equipped` TINYINT(1) DEFAULT 1 COMMENT '是否已穿戴',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_character` (`character_id`),
    INDEX `idx_pet` (`pet_id`),
    UNIQUE KEY `uk_character_pet` (`character_id`, `pet_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色宠物关联表';