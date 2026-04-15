-- 卡片模板表
CREATE TABLE IF NOT EXISTS card_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(50) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(20) NOT NULL UNIQUE COMMENT '模板编码',
    template_style TEXT COMMENT '模板样式JSON',
    preview_image VARCHAR(255) COMMENT '预览图URL',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认模板',
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='卡片模板表';

-- 角色卡片表
CREATE TABLE IF NOT EXISTS character_cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    character_id BIGINT NOT NULL COMMENT '角色ID',
    template_id BIGINT NOT NULL COMMENT '模板ID',
    card_name VARCHAR(100) COMMENT '卡片名称',
    card_data TEXT COMMENT '卡片数据快照JSON',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否设为默认展示',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (character_id) REFERENCES characters(id) ON DELETE CASCADE,
    FOREIGN KEY (template_id) REFERENCES card_templates(id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色卡片表';

-- 插入默认模板数据
INSERT INTO card_templates (template_name, template_code, template_style, is_default, created_at, updated_at) VALUES
('经典卡', 'classic', '{"background":{"type":"gradient","colors":["#1a1a2e","#16213e"]},"layout":"vertical","fontFamily":"Microsoft YaHei","elements":[{"type":"avatar","position":"top","size":120},{"type":"name","position":"below-avatar","fontSize":24},{"type":"stats","position":"center","columns":2},{"type":"equipment","position":"bottom","style":"badge"}]}', true, NOW(), NOW()),
('简洁卡', 'minimal', '{"background":{"type":"solid","color":"#2d3436"},"layout":"compact","fontFamily":"Microsoft YaHei","elements":[{"type":"avatar","position":"left","size":80},{"type":"name","position":"right","fontSize":20},{"type":"stats","position":"center","columns":4}]}', false, NOW(), NOW()),
('展示卡', 'showcase', '{"background":{"type":"image"},"layout":"hero","fontFamily":"Microsoft YaHei","elements":[{"type":"avatar","position":"hero","size":200},{"type":"overlay","position":"bottom"},{"type":"stats","position":"bottom","columns":3}]}', false, NOW(), NOW());
