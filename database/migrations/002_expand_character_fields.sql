-- DNF角色系统字段扩展迁移脚本
-- 版本：v1.0
-- 日期：2026-04-15
-- 说明：根据 DNF_Character_Fields.md 设计文档扩展字段

SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET CHARACTER_SET_RESULTS=utf8mb4;
SET COLLATION_CONNECTION=utf8mb4_unicode_ci;

USE vue2_java_mysql;

-- ============================================
-- 1. 扩展characters表 - 核心战斗属性
-- ============================================

-- 添加核心输出属性（技攻/攻击强化）
ALTER TABLE characters
    ADD COLUMN technique_attack FLOAT DEFAULT 0 COMMENT '技能攻击力（技攻%）',
    ADD COLUMN attack_bonus INT DEFAULT 0 COMMENT '攻击强化',
    ADD COLUMN buff_bonus INT DEFAULT 0 COMMENT '增益量（奶系BUFF三攻加成）';

-- 添加模拟伤害和站街三攻
ALTER TABLE characters
    ADD COLUMN simulate_damage INT DEFAULT 0 COMMENT '模拟伤害（修炼场沙袋）',
    ADD COLUMN stat_attack INT DEFAULT 0 COMMENT '站街三攻（奶系BUFF加成量）';

-- ============================================
-- 2. 扩展characters表 - 伤害词缀
-- ============================================

ALTER TABLE characters
    ADD COLUMN final_damage FLOAT DEFAULT 0 COMMENT '最终伤害',
    ADD COLUMN white_crit_damage FLOAT DEFAULT 0 COMMENT '白字附加伤害%',
    ADD COLUMN yellow_crit_damage FLOAT DEFAULT 0 COMMENT '黄字追加伤害%',
    ADD COLUMN crit_damage_bonus FLOAT DEFAULT 0 COMMENT '爆伤追加%',
    ADD COLUMN skill_lv_bonus INT DEFAULT 0 COMMENT '技攻Lv加成',
    ADD COLUMN skill_attack_bonus FLOAT DEFAULT 0 COMMENT '技攻角色攻击力';

-- ============================================
-- 3. 扩展characters表 - 属性抗性
-- ============================================

ALTER TABLE characters
    ADD COLUMN light_resistance INT DEFAULT 0 COMMENT '光属性抗性',
    ADD COLUMN fire_resistance INT DEFAULT 0 COMMENT '火属性抗性',
    ADD COLUMN ice_resistance INT DEFAULT 0 COMMENT '冰属性抗性',
    ADD COLUMN dark_resistance INT DEFAULT 0 COMMENT '暗属性抗性';

-- ============================================
-- 4. 扩展characters表 - 奶系BUFF字段
-- ============================================

ALTER TABLE characters
    ADD COLUMN buff_level INT DEFAULT 0 COMMENT 'BUFF换装等级',
    ADD COLUMN buff_strength INT DEFAULT 0 COMMENT 'BUFF增加的力量/体力',
    ADD COLUMN buff_intelligence INT DEFAULT 0 COMMENT 'BUFF增加的智力/精神';

-- ============================================
-- 5. 扩展characters表 - 其他战斗属性
-- ============================================

-- HP/MP
ALTER TABLE characters
    ADD COLUMN hp_max INT DEFAULT 0 COMMENT 'HP最大值',
    ADD COLUMN mp_max INT DEFAULT 0 COMMENT 'MP最大值';

-- 防御
ALTER TABLE characters
    ADD COLUMN physical_defense INT DEFAULT 0 COMMENT '物理防御力',
    ADD COLUMN magical_defense INT DEFAULT 0 COMMENT '魔法防御力';

-- 暴击/命中/回避
ALTER TABLE characters
    ADD COLUMN crit_rate INT DEFAULT 0 COMMENT '暴击率%',
    ADD COLUMN hit_rate INT DEFAULT 100 COMMENT '命中率%',
    ADD COLUMN avoidance INT DEFAULT 0 COMMENT '回避率%';

-- 三速
ALTER TABLE characters
    ADD COLUMN attack_speed INT DEFAULT 0 COMMENT '攻击速度%',
    ADD COLUMN casting_speed INT DEFAULT 0 COMMENT '施放速度%',
    ADD COLUMN move_speed INT DEFAULT 0 COMMENT '移动速度%';

-- 恢复量
ALTER TABLE characters
    ADD COLUMN hp_recovery INT DEFAULT 0 COMMENT 'HP恢复量',
    ADD COLUMN mp_recovery INT DEFAULT 0 COMMENT 'MP恢复量';

-- 最高属强（计算字段）
ALTER TABLE characters
    ADD COLUMN max_element INT DEFAULT 0 COMMENT '最高属强';

-- 物理/魔法暴击率
ALTER TABLE characters
    ADD COLUMN physical_crit_rate INT DEFAULT 0 COMMENT '物理暴击率%',
    ADD COLUMN magical_crit_rate INT DEFAULT 0 COMMENT '魔法暴击率%';

-- ============================================
-- 6. 扩展characters表 - 装备和誓约系统
-- ============================================

-- 装备数据JSON
ALTER TABLE characters
    ADD COLUMN equipment_data TEXT COMMENT '装备信息JSON（12套装备+部位装备信息）';

-- 誓约系统
ALTER TABLE characters
    ADD COLUMN oath_mode VARCHAR(20) COMMENT '誓约模式：BASIC/MODE1/MODE2',
    ADD COLUMN oath_attribute VARCHAR(100) COMMENT '当前激活的誓约属性';

-- 打造系统JSON
ALTER TABLE characters
    ADD COLUMN enhancement_data TEXT COMMENT '打造信息JSON（增幅/附魔/称号/宠物/换装）';

-- ============================================
-- 7. 扩展characters表 - 扩展已有字段
-- ============================================

-- 名望值已有，需要确认索引
-- 区服名称已有
-- 冒险团名称已有

-- 添加名望值索引（如果需要频繁查询）
-- CREATE INDEX idx_fame_value ON characters(fame_value);

-- ============================================
-- 8. 创建装备套装表（可选，用于独立管理装备套装）
-- ============================================

CREATE TABLE IF NOT EXISTS equipment_sets (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL COMMENT '套装名称',
    index_num INT NOT NULL COMMENT '套装序号（1-12）',
    set_type VARCHAR(20) DEFAULT 'normal' COMMENT '套装类型：normal-普通/special-特殊',
    description TEXT COMMENT '套装描述',
    tech_attack_bonus FLOAT DEFAULT 0 COMMENT '技攻加成%',
    attack_bonus INT DEFAULT 0 COMMENT '攻击强化',
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_index_num (index_num)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='装备套装表';

-- 插入12套装备数据
INSERT INTO equipment_sets (name, index_num, description, tech_attack_bonus, attack_bonus, created_at, updated_at) VALUES
('理想之黄金乡套', 1, '理想之黄金乡套装', 5.0, 5000, NOW(), NOW()),
('龙战八荒套', 2, '龙战八荒套装', 5.5, 5500, NOW(), NOW()),
('潜影暗袭套', 3, '潜影暗袭套装', 6.0, 6000, NOW(), NOW()),
('造化自然套', 4, '造化自然套装', 6.5, 6500, NOW(), NOW()),
('天命者的气运套', 5, '天命者的气运套装', 7.0, 7000, NOW(), NOW()),
('究极能量套', 6, '究极能量套装', 7.5, 7500, NOW(), NOW()),
('混沌净化套', 7, '混沌净化套装', 8.0, 8000, NOW(), NOW()),
('诸神黄昏之女武神套', 8, '诸神黄昏之女武神套装', 8.5, 8500, NOW(), NOW()),
('青丘灵珠套', 9, '青丘灵珠套装', 9.0, 9000, NOW(), NOW()),
('精灵国度套', 10, '精灵国度套装', 9.5, 9500, NOW(), NOW()),
('冥思者的领域套', 11, '冥思者的领域套装', 10.0, 10000, NOW(), NOW()),
('群猎美学套', 12, '群猎美学套装', 10.5, 10500, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 9. 创建誓约配置表
-- ============================================

CREATE TABLE IF NOT EXISTS oath_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    oath_mode VARCHAR(20) NOT NULL COMMENT '誓约模式',
    oath_name VARCHAR(50) NOT NULL COMMENT '誓约名称',
    oath_attribute VARCHAR(100) NOT NULL COMMENT '誓约属性描述',
    description TEXT COMMENT '模式效果描述',
    created_at DATETIME,
    updated_at DATETIME,
    UNIQUE KEY uk_oath_mode (oath_mode)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='誓约配置表';

-- 插入誓约配置数据
INSERT INTO oath_configs (oath_mode, oath_name, oath_attribute, description, created_at, updated_at) VALUES
('BASIC', '无限动力', '觉醒CD-30%，伤害均衡', '觉醒技能冷却时间减少30%，伤害均衡输出', NOW(), NOW()),
('MODE1', '压倒性的力量', '极端三觉伤害爆发', '三觉伤害极大提升，适合爆发输出', NOW(), NOW()),
('MODE2', '超载', '一二觉+80%/+96%，可刷新', '一觉+80%、二觉+96%伤害，可刷新CD', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- ============================================
-- 10. 创建打造配置表（增幅/附魔/称号等选项）
-- ============================================

CREATE TABLE IF NOT EXISTS enhancement_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    category VARCHAR(50) NOT NULL COMMENT '类别：amplify/enchant/title/pet/buffSwap',
    item_name VARCHAR(100) NOT NULL COMMENT '项目名称',
    item_value VARCHAR(100) NOT NULL COMMENT '项目值',
    tech_attack_bonus FLOAT DEFAULT 0 COMMENT '技攻加成%',
    attack_bonus INT DEFAULT 0 COMMENT '攻击强化加成',
    elemental_bonus INT DEFAULT 0 COMMENT '属强加成',
    four_stats_bonus INT DEFAULT 0 COMMENT '四维加成',
    buff_bonus_bonus INT DEFAULT 0 COMMENT '增益量加成',
    description TEXT COMMENT '描述',
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_category (category)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='打造配置表';

-- ============================================
-- 验证迁移结果
-- ============================================

-- 查看characters表结构
-- DESCRIBE characters;

-- 查看新增的表
-- SHOW TABLES LIKE '%equipment%';
-- SHOW TABLES LIKE '%oath%';
-- SHOW TABLES LIKE '%enhancement%';
