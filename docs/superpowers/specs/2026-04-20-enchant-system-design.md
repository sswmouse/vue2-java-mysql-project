# 附魔系统设计文档

> **目标：** 实现DNF装备附魔系统，用户端以卡片形式展示12个装备部位，管理端统一配置附魔选项

**日期：** 2026-04-20

---

## 一、功能概述

### 1.1 用户端 (`/enchantment`)
- 12个装备部位卡片网格展示（4x3布局）
- 右上角角色选择器
- 点击卡片弹出附魔选择弹窗
- 卡片显示：部位名称 + 附魔名称 + 附魔属性
- 未配置显示：部位名称 + 未附魔

### 1.2 管理端 (`/enchantment/options`)
- 管理员专属路由（需要管理员权限）
- 统一附魔选项管理
- 按部位筛选、增删改查功能
- 按礼包类型分组展示选项

---

## 二、数据结构

### 2.1 现有模型

**EnchantOption（已有）**
```java
@Entity
@Table(name = "enchant_options")
public class EnchantOption {
    Long id;
    String equipmentPart;    // 装备部位：武器/头肩/上衣/下装/鞋/腰带/项链/手镯/戒指/辅助装备/魔法石/耳环
    String name;             // 宝珠名称
    String packageType;      // 礼包类型：金秋套/春节套/其他
    String attributeType;    // 属性类型：技攻/力智/体精/暴击/属强/四维
    String attributeValue;   // 属性值：+30/+25/+8%等
    Integer sortOrder;       // 排序顺序
    Boolean isLatest;        // 是否最新赛季
    String remark;           // 备注
}
```

### 2.2 新建模型

**CharacterEnchant（角色附魔配置）**
```java
@Entity
@Table(name = "character_enchant", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"character_id", "equipment_part"})
})
public class CharacterEnchant {
    Long id;                  // 主键
    Long characterId;         // 角色ID
    String equipmentPart;     // 装备部位
    Long enchantOptionId;     // 附魔选项ID（外键）
    String enchantName;       // 附魔名称（冗余存储，便于显示）
    String attributeValue;    // 属性值（冗余存储）
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
}
```

### 2.3 12个装备部位列表
1. 武器
2. 头肩
3. 上衣
4. 下装
5. 鞋
6. 腰带
7. 项链
8. 手镯
9. 戒指
10. 辅助装备
11. 魔法石
12. 耳环

---

## 三、API接口

### 3.1 后端已有接口
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/enchant-options` | 获取所有附魔选项 |
| GET | `/api/enchant-options/{id}` | 获取单个选项 |
| GET | `/api/enchant-options/part/{part}` | 按部位获取选项 |
| GET | `/api/enchant-options/grouped` | 按部位分组获取 |
| GET | `/api/enchant-options/parts` | 获取所有部位 |
| POST | `/api/enchant-options` | 创建选项 |
| PUT | `/api/enchant-options/{id}` | 更新选项 |
| DELETE | `/api/enchant-options/{id}` | 删除选项 |
| POST | `/api/enchant-options/batch` | 批量创建 |

### 3.2 新建接口
| 方法 | 路径 | 描述 |
|------|------|------|
| GET | `/api/character-enchant/{characterId}` | 获取角色所有附魔配置 |
| GET | `/api/character-enchant/{characterId}/{part}` | 获取角色指定部位附魔 |
| POST | `/api/character-enchant/{characterId}` | 保存/更新角色附魔 |
| DELETE | `/api/character-enchant/{characterId}/{part}` | 删除角色指定部位附魔 |

---

## 四、页面设计

### 4.1 用户端 (`/enchantment`)

**布局：**
```
┌──────────────────────────────────────────────────────────┐
│  附魔管理                              [选择角色 ▼]     │
├──────────────────────────────────────────────────────────┤
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐        │
│  │  武器   │ │  头肩   │ │  上衣   │ │  下装   │        │
│  │ 金秋宝珠│ │ 春节宝珠│ │ 未附魔  │ │ 金秋宝珠│        │
│  │ 技攻+30 │ │ 技攻+30 │ │        │ │ 力智+70 │        │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘        │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐        │
│  │   鞋    │ │  腰带   │ │  项链   │ │  手镯   │        │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘        │
│  ┌─────────┐ ┌─────────┐ ┌─────────┐ ┌─────────┐        │
│  │  戒指   │ │ 辅助装备│ │ 魔法石  │ │  耳环   │        │
│  └─────────┘ └─────────┘ └─────────┘ └─────────┘        │
└──────────────────────────────────────────────────────────┘
```

**卡片样式：**
- 尺寸：固定高度 160px
- 顶部：部位名称（金色标题）
- 中部：附魔名称（白色）
- 底部：属性值（灰色/蓝色）
- 未附魔：显示"未附魔"文字，灰色边框

### 4.2 选择弹窗

**布局：**
```
┌──────────────────────────────────────────────────────────┐
│  选择 [部位名称] 附魔                                 X  │
├──────────────────────────────────────────────────────────┤
│  ┌────────────────────────────────────────────────────┐  │
│  │ 全部选项 ▼                                         │  │
│  └────────────────────────────────────────────────────┘  │
│                                                          │
│  ┌─ 金秋套 ──────────────────────────────────────────┐   │
│  │ ○ 金秋宝珠(技攻+30)  - 技攻 +30                  │   │
│  │ ○ 金秋宝珠(技攻+4%)  - 技攻 +4%                  │   │
│  │ ○ 金秋宝珠(暴击+8%)  - 暴击 +8%                   │   │
│  └────────────────────────────────────────────────────┘   │
│                                                          │
│  ┌─ 春节套 ──────────────────────────────────────────┐   │
│  │ ○ 春节宝珠(技攻+30)  - 技攻 +30                  │   │
│  └────────────────────────────────────────────────────┘   │
│                                                          │
│  ┌─ 其他 ────────────────────────────────────────────┐   │
│  │ ○ 未附魔                                          │   │
│  └────────────────────────────────────────────────────┘   │
│                                                          │
│                        [取消]  [确定]                    │
└──────────────────────────────────────────────────────────┘
```

### 4.3 管理端 (`/enchantment/options`)

**布局：**
```
┌──────────────────────────────────────────────────────────┐
│  附魔选项管理                                            │
├──────────────────────────────────────────────────────────┤
│  部位: [全部 ▼]  礼包: [全部 ▼]     [+ 添加选项]        │
├──────────────────────────────────────────────────────────┤
│  ┌────────────────────────────────────────────────────┐  │
│  │ 部位     │ 名称          │ 礼包    │ 属性     │ 操作│  │
│  ├────────────────────────────────────────────────────┤  │
│  │ 武器     │ 金秋宝珠     │ 金秋套  │ 技攻+30  │编辑 │  │
│  │ 武器     │ 春节宝珠     │ 春节套  │ 技攻+30  │删除 │  │
│  │ 头肩     │ 金秋宝珠     │ 金秋套  │ 技攻+30  │编辑 │  │
│  └────────────────────────────────────────────────────┘  │
└──────────────────────────────────────────────────────────┘
```

---

## 五、文件结构

### 5.1 后端新增
```
backend/src/main/java/com/example/demo/
├── model/
│   └── CharacterEnchant.java          # 新建
├── repository/
│   └── CharacterEnchantRepository.java # 新建
├── service/
│   ├── CharacterEnchantService.java   # 新建
│   └── impl/
│       └── CharacterEnchantServiceImpl.java  # 新建
├── controller/
│   └── CharacterEnchantController.java # 新建
└── dto/
    └── CharacterEnchantDTO.java       # 新建
```

### 5.2 前端新增/修改
```
frontend/src/
├── views/
│   ├── Enchant.vue                    # 重写（卡片形式）
│   └── enchant/
│       └── Options.vue                # 新建（管理端）
├── components/
│   ├── EnchantCard.vue                # 新建
│   └── EnchantSelectDialog.vue        # 新建
├── api/
│   └── enchant.js                     # 新建
└── router/
    └── index.js                       # 修改（添加子路由）
```

---

## 六、实现步骤

### Phase 1: 后端基础
1. 创建 `CharacterEnchant` 实体类
2. 创建 `CharacterEnchantRepository`
3. 创建 `CharacterEnchantService` 及实现
4. 创建 `CharacterEnchantController`

### Phase 2: 前端基础
1. 创建 `api/enchant.js` API模块
2. 创建 `EnchantCard.vue` 卡片组件
3. 创建 `EnchantSelectDialog.vue` 选择弹窗
4. 重写 `Enchant.vue` 用户端页面

### Phase 3: 管理端
1. 创建 `views/enchant/Options.vue` 管理页面
2. 添加路由子路由 `/enchantment/options`
3. 添加管理员权限控制

### Phase 4: 集成测试
1. 测试角色附魔保存/加载
2. 测试管理端选项CRUD
3. 测试权限控制
