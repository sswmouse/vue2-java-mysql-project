# DNF角色打造与卡片系统设计文档

> 版本：v1.0
> 日期：2026-04-15
> 状态：已确认

---

## 一、系统架构

```
┌─────────────────────────────────────────────────────────────┐
│                        前端 (Vue2)                           │
├─────────────────────────────────────────────────────────────┤
│  角色管理      │      打造系统 (6模块)       │     卡片系统   │
│  · 角色列表     │  · 增幅  · 附魔  · 称号    │  · 卡片生成    │
│  · 角色创建     │  · 宠物  · 装备  · 徽章    │  · 模板库      │
│  · 角色详情     │      混合模式 + 实时保存     │  · 可编辑      │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                      后端 (Spring Boot)                      │
├─────────────────────────────────────────────────────────────┤
│  CharacterController │ EnhancementController │ CardController│
│  CharacterService    │ EnhancementService    │ CardService   │
│  CharacterRepository │ (共用 Character 实体) │ CardRepository │
└─────────────────────────────────────────────────────────────┘
                              │
                              ▼
┌─────────────────────────────────────────────────────────────┐
│                        MySQL                                │
├─────────────────────────────────────────────────────────────┤
│  characters 表 (主角色数据 + 6个打造系统 JSON 字段)          │
│  card_templates 表 (卡片模板)  │  character_cards 表 (生成卡片)│
└─────────────────────────────────────────────────────────────┘
```

---

## 二、数据库设计

### 2.1 角色表新增字段 (characters)

```sql
ALTER TABLE characters ADD COLUMN (
    -- 增幅
    amplify_data TEXT COMMENT '增幅JSON {level, attribute, isReinforce, weaponLevel}',
    -- 附魔
    enchant_data TEXT COMMENT '附魔JSON {weapon, head, chest, legs, shoes...}',
    -- 称号
    title_data TEXT COMMENT '称号JSON {name, year}',
    -- 宠物
    pet_data TEXT COMMENT '宠物JSON {pet, petEquips...}',
    -- 装备
    equipment_data TEXT COMMENT '装备JSON {setName, pieces...}',
    -- 徽章
    emblem_data TEXT COMMENT '徽章JSON {emblemGrade, starLevel...}'
);
```

### 2.2 JSON结构详细定义

**增幅数据 (amplify_data)**:
```json
{
  "level": 12,
  "attribute": "力量",
  "isReinforce": false,
  "weaponLevel": 25,
  "isWeaponReinforce": true
}
```

**附魔数据 (enchant_data)**:
```json
{
  "weapon": "格斗场冠军宝珠",
  "head": "冲击之魂头肩宝珠",
  "chest": "使徒希洛克宝珠",
  "legs": "使徒希洛克宝珠",
  "shoes": "格斗场冠军宝珠",
  "necklace": "光之主人宝珠",
  "bracelet": "火之主人宝珠",
  "ring": "冰之主人宝珠",
  "subEquip": "机械崛起融合石",
  "magicStone": "机械崛起融合石",
  "auxiliary": "机械崛起融合石",
  "elementType": "冰",
  "elementValue": 60
}
```

**称号数据 (title_data)**:
```json
{
  "name": "破坏之毁灭者",
  "year": "2024金秋"
}
```

**宠物数据 (pet_data)**:
```json
{
  "pet": "迷你铁齿狗",
  "petEquipRed": "力智+35",
  "petEquipBlue": "三攻+28",
  "petEquipGreen": "属强+10"
}
```

**装备数据 (equipment_data)**:
```json
{
  "setName": "究极能量套",
  "setIndex": 6,
  "setScore": 285,
  "pieces": {
    "weapon": { "name": "胜负之役棍棒", "grade": "太初" },
    "head": { "name": "xxx", "grade": "史诗" },
    "chest": { "name": "xxx", "grade": "史诗" },
    "legs": { "name": "xxx", "grade": "史诗" },
    "shoulder": { "name": "xxx", "grade": "史诗" },
    "shoes": { "name": "xxx", "grade": "史诗" },
    "necklace": { "name": "xxx", "grade": "太初" },
    "bracelet": { "name": "xxx", "grade": "太初" },
    "ring": { "name": "xxx", "grade": "太初" },
    "subEquip": { "name": "xxx", "grade": "史诗" },
    "magicStone": { "name": "xxx", "grade": "史诗" },
    "auxiliary": { "name": "xxx", "grade": "史诗" }
  }
}
```

**徽章数据 (emblem_data)**:
```json
{
  "emblemGrade": "神器",
  "starLevel": 3,
  "emblemSlots": {
    "red": "玲珑绿色徽章",
    "yellow": "玲珑黄色徽章",
    "blue": "玲珑蓝色徽章",
    "green": "玲珑绿色徽章",
    "colorful": "玲珑彩色徽章"
  }
}
```

### 2.3 卡片模板表 (card_templates)

```sql
CREATE TABLE card_templates (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    template_name VARCHAR(50) NOT NULL COMMENT '模板名称',
    template_code VARCHAR(20) NOT NULL UNIQUE COMMENT '模板编码',
    template_style TEXT COMMENT '模板样式JSON',
    preview_image VARCHAR(255) COMMENT '预览图URL',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否默认模板',
    created_at DATETIME,
    updated_at DATETIME
);
```

### 2.4 角色卡片表 (character_cards)

```sql
CREATE TABLE character_cards (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    character_id BIGINT NOT NULL,
    template_id BIGINT NOT NULL,
    card_name VARCHAR(100) COMMENT '卡片名称',
    card_data TEXT COMMENT '卡片数据快照JSON',
    is_default BOOLEAN DEFAULT FALSE COMMENT '是否设为默认展示',
    created_at DATETIME,
    updated_at DATETIME,
    FOREIGN KEY (character_id) REFERENCES characters(id),
    FOREIGN KEY (template_id) REFERENCES card_templates(id)
);
```

---

## 三、API接口设计

### 3.1 打造系统接口

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/api/characters/{id}/enhancement` | 获取角色全部打造数据 |
| PUT | `/api/characters/{id}/amplify` | 更新增幅数据 |
| PUT | `/api/characters/{id}/enchant` | 更新附魔数据 |
| PUT | `/api/characters/{id}/title` | 更新称号数据 |
| PUT | `/api/characters/{id}/pet` | 更新宠物数据 |
| PUT | `/api/characters/{id}/equipment` | 更新装备数据 |
| PUT | `/api/characters/{id}/emblem` | 更新徽章数据 |

### 3.2 卡片接口

| 方法 | 路径 | 说明 |
|-----|------|------|
| GET | `/api/cards/templates` | 获取模板列表 |
| GET | `/api/characters/{id}/cards` | 获取角色所有卡片 |
| POST | `/api/characters/{id}/cards` | 创建卡片 |
| PUT | `/api/cards/{id}` | 更新卡片 |
| DELETE | `/api/cards/{id}` | 删除卡片 |
| PUT | `/api/cards/{id}/default` | 设为默认展示 |

---

## 四、前端页面设计

### 4.1 路由结构

```javascript
{
  path: '/role/:id',
  component: Role,
  children: [
    { path: '', redirect: 'overview' },
    { path: 'overview', component: RoleOverview },      // 角色总览
    { path: 'amplify', component: Amplify },             // 增幅
    { path: 'enchant', component: Enchant },           // 附魔
    { path: 'title', component: Title },               // 称号
    { path: 'pet', component: Pet },                   // 宠物
    { path: 'equipment', component: Equipment },       // 装备
    { path: 'emblem', component: Emblem },             // 徽章
    { path: 'cards', component: CharacterCards }       // 卡片管理
  ]
}
```

### 4.2 卡片模板样式

#### 模板1: 经典卡 (DNF风格)
- 深蓝渐变背景
- 角色立绘/头像居中
- 属性面板表格展示
- 套装标签展示

#### 模板2: 简洁卡 (扁平化)
- 纯色/渐变背景
- 大号数字展示
- 紧凑布局
- 扁平化设计

#### 模板3: 展示卡 (大图模式)
- 角色立绘占70%高度
- 半透明遮罩上的文字
- 突出打造成果
- 视觉效果强

---

## 五、实现优先级

| 阶段 | 内容 |
|-----|------|
| **Phase 1** | 角色基础完善 + 数据库字段 |
| **Phase 2** | API接口开发 (6个打造接口 + 卡片接口) |
| **Phase 3** | 打造系统前端 (6个Tab页) |
| **Phase 4** | 卡片系统 (模板 + 生成 + 展示) |
| **Phase 5** | 卡片模板美化 + 个性化 |
