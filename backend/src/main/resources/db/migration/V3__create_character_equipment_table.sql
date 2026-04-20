-- V3: 创建角色装备配置表
CREATE TABLE IF NOT EXISTS character_equipment (
    id BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    character_id BIGINT NOT NULL COMMENT '角色ID',
    set_id BIGINT COMMENT '当前装备套装ID',
    set_name VARCHAR(100) COMMENT '当前装备套装名称',
    current_points INT COMMENT '当前积分',
    current_stage VARCHAR(50) COMMENT '当前阶段名称',
    equipment_config TEXT COMMENT '装备配置JSON',
    created_at DATETIME COMMENT '创建时间',
    updated_at DATETIME COMMENT '更新时间',
    UNIQUE KEY uk_character_id (character_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色装备配置表';
