-- 称号表
CREATE TABLE IF NOT EXISTS `title` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `name` VARCHAR(100) NOT NULL COMMENT '称号名称',
    `year` INT NOT NULL COMMENT '年份',
    `type` VARCHAR(50) NOT NULL COMMENT '称号类型：普通/至尊/耕耘/金秋/春节',
    `sub_type` VARCHAR(50) DEFAULT NULL COMMENT '子类型（如：时空旅人/纵横次元等）',
    `four_stats` INT DEFAULT 0 COMMENT '四维',
    `three_attack` INT DEFAULT 0 COMMENT '三攻',
    `crit_rate` DECIMAL(5,2) DEFAULT 0 COMMENT '暴击率(%)',
    `elemental_bonus` INT DEFAULT 0 COMMENT '属强',
    `attack_speed` DECIMAL(5,2) DEFAULT 0 COMMENT '三速(%)',
    `attack_strength` DECIMAL(5,2) DEFAULT 0 COMMENT '攻击强化(%)',
    `skill_bonus` VARCHAR(200) DEFAULT NULL COMMENT '技能加成描述',
    `skill_bonus_level` INT DEFAULT NULL COMMENT '技能加成等级',
    `fame` INT DEFAULT 0 COMMENT '名望',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '额外描述',
    `is_active` TINYINT(1) DEFAULT 1 COMMENT '是否启用',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    PRIMARY KEY (`id`),
    INDEX `idx_year` (`year`),
    INDEX `idx_type` (`type`),
    INDEX `idx_fame` (`fame`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='称号表';

-- 角色称号关联表
CREATE TABLE IF NOT EXISTS `character_title` (
    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `character_id` BIGINT NOT NULL COMMENT '角色ID',
    `title_id` BIGINT NOT NULL COMMENT '称号ID',
    `is_equipped` TINYINT(1) DEFAULT 1 COMMENT '是否已穿戴',
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    PRIMARY KEY (`id`),
    INDEX `idx_character` (`character_id`),
    INDEX `idx_title` (`title_id`),
    UNIQUE KEY `uk_character_title` (`character_id`, `title_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色称号关联表';