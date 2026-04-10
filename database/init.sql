-- 设置字符集，防止中文乱码
SET NAMES utf8mb4;
SET CHARACTER SET utf8mb4;
SET CHARACTER_SET_RESULTS=utf8mb4;
SET COLLATION_CONNECTION=utf8mb4_unicode_ci;

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

-- 创建角色类型表（DNF所有角色类型）
CREATE TABLE IF NOT EXISTS character_types (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    gender VARCHAR(10) NOT NULL COMMENT '性别：男/女',
    class_name VARCHAR(50) NOT NULL COMMENT '职业分类：鬼剑士/魔法师/格斗家/神枪手/圣职者/暗夜使者/守护者/魔枪士/枪剑士',
    job_name VARCHAR(50) NOT NULL COMMENT '具体职业：剑魂/元素/散打等',
    job_nature VARCHAR(20) NOT NULL COMMENT '职业性质：输出/奶系',
    job_attribute VARCHAR(50) NOT NULL COMMENT '职业属性：物理百分比/魔法百分比/物理固伤/魔法固伤/智力/精神/体力',
    special_case BOOLEAN DEFAULT FALSE COMMENT '是否特殊职业（如驱魔需要手动选择）',
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_class_name (class_name),
    INDEX idx_job_name (job_name),
    UNIQUE KEY uk_gender_class_job (gender, class_name, job_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色类型表';

-- 创建角色表
CREATE TABLE IF NOT EXISTS characters (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    user_id BIGINT NOT NULL COMMENT '所属用户ID',
    character_type_id BIGINT NOT NULL COMMENT '角色类型ID',
    character_name VARCHAR(50) NOT NULL COMMENT '角色名称',
    job_nature VARCHAR(20) NOT NULL COMMENT '职业性质：输出/奶系',
    job_attribute VARCHAR(50) NOT NULL COMMENT '职业属性',
    strength INT DEFAULT 0 COMMENT '力量',
    intelligence INT DEFAULT 0 COMMENT '智力',
    spirit INT DEFAULT 0 COMMENT '精神',
    vitality INT DEFAULT 0 COMMENT '体力',
    physical_attack INT DEFAULT 0 COMMENT '物理攻击力',
    magical_attack INT DEFAULT 0 COMMENT '魔法攻击力',
    independent_attack INT DEFAULT 0 COMMENT '独立攻击力',
    light_element INT DEFAULT 0 COMMENT '光属强',
    fire_element INT DEFAULT 0 COMMENT '火属强',
    ice_element INT DEFAULT 0 COMMENT '冰属强',
    dark_element INT DEFAULT 0 COMMENT '暗属强',
    avatar_url VARCHAR(500) COMMENT '角色外观图片URL',
    created_at DATETIME,
    updated_at DATETIME,
    INDEX idx_user_id (user_id),
    INDEX idx_character_type_id (character_type_id),
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (character_type_id) REFERENCES character_types(id) ON DELETE RESTRICT
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='角色表';

-- 插入DNF角色类型数据（根据实际游戏数据）
INSERT INTO character_types (gender, class_name, job_name, job_nature, job_attribute, special_case, created_at, updated_at) VALUES
-- 男鬼剑士
('男', '鬼剑士', '剑魂', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '鬼剑士', '狂战士', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '鬼剑士', '阿修罗', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '鬼剑士', '鬼泣', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 女鬼剑士
('女', '鬼剑士', '剑宗', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '鬼剑士', '剑魔', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '鬼剑士', '暗帝', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '鬼剑士', '刃影', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 男魔法师
('男', '魔法师', '元素爆破', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '魔法师', '冰结师', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '魔法师', '血法师', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '魔法师', '逐风者', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '魔法师', '次元行者', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 女魔法师
('女', '魔法师', '元素', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '魔法师', '战斗法师', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '魔法师', '召唤', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '魔法师', '魔道学者', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 男格斗家
('男', '格斗家', '街霸', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '格斗家', '柔道家', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '格斗家', '散打', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '格斗家', '念皇', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 女格斗家
('女', '格斗家', '街霸', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '格斗家', '柔道家', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '格斗家', '散打', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '格斗家', '念帝', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 男神枪手
('男', '神枪手', '漫游枪手', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '神枪手', '枪炮师', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '神枪手', '机械师', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '神枪手', '弹药专家', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 女神枪手
('女', '神枪手', '漫游枪手', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '神枪手', '枪炮师', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '神枪手', '机械师', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '神枪手', '弹药专家', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 男圣职者
('男', '圣职者', '圣骑士', '奶系', '智力', FALSE, NOW(), NOW()),
('男', '圣职者', '蓝拳圣使', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '圣职者', '驱魔人', '输出', '魔法百分比', TRUE, NOW(), NOW()),
('男', '圣职者', '复仇者', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 女圣职者
('女', '圣职者', '圣骑士', '奶系', '精神', FALSE, NOW(), NOW()),
('女', '圣职者', '异端审判者', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '圣职者', '巫女', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '圣职者', '缪斯', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 暗夜使者
('女', '暗夜使者', '刺客', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '暗夜使者', '死灵术士', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('女', '暗夜使者', '影舞者', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '暗夜使者', '忍者', '输出', '魔法百分比', FALSE, NOW(), NOW()),
-- 守护者
('女', '守护者', '骑士', '奶系', '体力', FALSE, NOW(), NOW()),
('女', '守护者', '混沌魔灵', '输出', '物理百分比', FALSE, NOW(), NOW()),
('女', '守护者', '帕拉丁', '奶系', '智力', FALSE, NOW(), NOW()),
('女', '守护者', '龙骑士', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 魔枪士
('男', '魔枪士', '决战者', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '魔枪士', '狩猎者', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '魔枪士', '暗枪士', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '魔枪士', '征战者', '输出', '物理百分比', FALSE, NOW(), NOW()),
-- 枪剑士
('男', '枪剑士', '特工', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '枪剑士', '战线佣兵', '输出', '物理百分比', FALSE, NOW(), NOW()),
('男', '枪剑士', '源能专家', '输出', '魔法百分比', FALSE, NOW(), NOW()),
('男', '枪剑士', '暗刃', '输出', '物理百分比', FALSE, NOW(), NOW())
ON DUPLICATE KEY UPDATE updated_at = NOW();

-- 创建数据库用户（如果需要）
-- CREATE USER IF NOT EXISTS 'vue2_user'@'localhost' IDENTIFIED BY 'password123';
-- GRANT ALL PRIVILEGES ON vue2_java_mysql.* TO 'vue2_user'@'localhost';
-- FLUSH PRIVILEGES;