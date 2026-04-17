-- ============================================================
-- DNF装备管理系统 - 数据库修复和新建表脚本
-- 执行时间: 2026-04-17
-- 说明: 修复equipment_sets表乱码数据，创建equipment_items和stage_configs表
-- ============================================================

-- 设置字符集
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET CHARACTER_SET_RESULTS=utf8mb4;
SET COLLATION_CONNECTION=utf8mb4_unicode_ci;

-- 使用数据库
USE vue2_java_mysql;

-- ============================================================
-- 1. 修复equipment_sets表的乱码数据
-- ============================================================
-- 查看当前数据状态
-- SELECT id, index_num, name, description FROM equipment_sets;

-- 更新12套装备的正确中文名称
UPDATE equipment_sets SET name = '潜影暗袭', description = '潜影暗袭套装效果' WHERE index_num = 1;
UPDATE equipment_sets SET name = '精灵国度', description = '精灵国度套装效果' WHERE index_num = 2;
UPDATE equipment_sets SET name = '理想之黄金乡', description = '理想之黄金乡套装效果' WHERE index_num = 3;
UPDATE equipment_sets SET name = '龙战八荒', description = '龙战八荒套装效果' WHERE index_num = 4;
UPDATE equipment_sets SET name = '混沌净化', description = '混沌净化套装效果' WHERE index_num = 5;
UPDATE equipment_sets SET name = '天命者的气运', description = '天命者的气运套装效果' WHERE index_num = 6;
UPDATE equipment_sets SET name = '究极能量', description = '究极能量套装效果' WHERE index_num = 7;
UPDATE equipment_sets SET name = '造化自然', description = '造化自然套装效果' WHERE index_num = 8;
UPDATE equipment_sets SET name = '诸神黄昏之女武神', description = '诸神黄昏之女武神套装效果' WHERE index_num = 9;
UPDATE equipment_sets SET name = '青丘灵珠', description = '青丘灵珠套装效果' WHERE index_num = 10;
UPDATE equipment_sets SET name = '群猎美学', description = '群猎美学套装效果' WHERE index_num = 11;
UPDATE equipment_sets SET name = '冥思者的魔力领域', description = '冥思者的魔力领域套装效果' WHERE index_num = 12;

-- ============================================================
-- 2. 创建equipment_items装备详情表
-- ============================================================
DROP TABLE IF EXISTS equipment_items;

CREATE TABLE IF NOT EXISTS equipment_items (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    set_id BIGINT NOT NULL COMMENT '所属套装ID',
    slot_type VARCHAR(20) NOT NULL COMMENT '部位类型：armor-防具/jewelry-首饰/special-特殊装备',
    slot_name VARCHAR(50) NOT NULL COMMENT '部位名称：头肩/胸甲/腰带/下装/鞋/项链/手镯/戒指/辅助装备/魔法石/耳环',
    item_name VARCHAR(100) NOT NULL COMMENT '装备名称',
    rarity_stats JSON COMMENT '各品级属性(JSON格式)',
    rarity_points JSON COMMENT '各品级积分(JSON格式)',
    rarity_fame JSON COMMENT '各品级名望(JSON格式)',
    sort_order INT DEFAULT 0 COMMENT '排序顺序',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    INDEX idx_set_id (set_id),
    INDEX idx_slot_type (slot_type),
    FOREIGN KEY (set_id) REFERENCES equipment_sets(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='装备详情表';

-- ============================================================
-- 3. 创建stage_configs阶段配置表
-- ============================================================
DROP TABLE IF EXISTS stage_configs;

CREATE TABLE IF NOT EXISTS stage_configs (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    stage VARCHAR(20) NOT NULL COMMENT '阶段名称：稀有1-5/神器1-5/传说1-5/史诗1-5/太初',
    stage_order INT NOT NULL COMMENT '阶段顺序(1-21)',
    threshold INT NOT NULL COMMENT '触发积分阈值',
    fame_bonus INT NOT NULL COMMENT '阶段额外名望',
    cumulative_fame INT NOT NULL COMMENT '累计名望',
    stage_type VARCHAR(20) COMMENT '阶段类型：minor-小阶段/major-大阶段',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    UNIQUE KEY uk_stage (stage),
    INDEX idx_stage_order (stage_order),
    INDEX idx_threshold (threshold)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='套装阶段配置表';

-- ============================================================
-- 4. 初始化21个阶段配置数据
-- ============================================================
INSERT INTO stage_configs (stage, stage_order, threshold, fame_bonus, cumulative_fame, stage_type, created_at, updated_at) VALUES
-- 稀有阶段（5个）
('稀有1', 1, 750, 0, 0, 'minor', NOW(), NOW()),
('稀有2', 2, 835, 1000, 1000, 'minor', NOW(), NOW()),
('稀有3', 3, 920, 2000, 2000, 'minor', NOW(), NOW()),
('稀有4', 4, 1005, 3000, 3000, 'minor', NOW(), NOW()),
('稀有5', 5, 1090, 4000, 4000, 'minor', NOW(), NOW()),
-- 神器阶段（5个）- 大阶段跨品级，加110分
('神器1', 6, 1200, 5500, 5500, 'major', NOW(), NOW()),
('神器2', 7, 1285, 6500, 6500, 'minor', NOW(), NOW()),
('神器3', 8, 1370, 7500, 7500, 'minor', NOW(), NOW()),
('神器4', 9, 1455, 8500, 8500, 'minor', NOW(), NOW()),
('神器5', 10, 1540, 9500, 9500, 'minor', NOW(), NOW()),
-- 传说阶段（5个）- 大阶段跨品级，加110分
('传说1', 11, 1650, 11000, 11000, 'major', NOW(), NOW()),
('传说2', 12, 1735, 12000, 12000, 'minor', NOW(), NOW()),
('传说3', 13, 1820, 13000, 13000, 'minor', NOW(), NOW()),
('传说4', 14, 1905, 14000, 14000, 'minor', NOW(), NOW()),
('传说5', 15, 1990, 15000, 15000, 'minor', NOW(), NOW()),
-- 史诗阶段（5个）- 大阶段跨品级，加110分
('史诗1', 16, 2100, 16500, 16500, 'major', NOW(), NOW()),
('史诗2', 17, 2185, 17500, 17500, 'minor', NOW(), NOW()),
('史诗3', 18, 2270, 18500, 18500, 'minor', NOW(), NOW()),
('史诗4', 19, 2355, 19500, 19500, 'minor', NOW(), NOW()),
('史诗5', 20, 2440, 20500, 20500, 'minor', NOW(), NOW()),
-- 太初阶段（1个）- 大阶段跨品级，加110分
('太初', 21, 2550, 22000, 22000, 'major', NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 验证数据
SELECT '=== 装备套装数据修复完成 ===' AS message;
SELECT id, index_num, name, description FROM equipment_sets ORDER BY index_num;

SELECT '=== 阶段配置数据初始化完成 ===' AS message;
SELECT id, stage, stage_order, threshold, fame_bonus, cumulative_fame, stage_type FROM stage_configs ORDER BY stage_order;
