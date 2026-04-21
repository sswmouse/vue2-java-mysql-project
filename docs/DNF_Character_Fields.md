# DNF 角色系统字段设计文档

> 版本：v1.7
> 日期：2026-04-21
> 状态：大部分核心功能已实现

---

## 零、已实现系统清单

| 系统 | 模块 | 后端模型 | 前端页面 | 状态 |
|-----|------|---------|---------|-----|
| **角色系统** | 基础信息、战斗属性 | Character.java | Role.vue | ✅ 已实现 |
| **卡片系统** | 卡片收集 | CardTemplate, CharacterCard | Badge.vue | ✅ 已实现 |
| **装备系统** | 套装、物品 | EquipmentSet, EquipmentItem, CharacterEquipment | Equipment.vue | ✅ 已实现 |
| **增幅系统** | 角色增幅、增幅配置 | AmplifyOption, CharacterAmplify | Amplify.vue | ✅ 已实现 |
| **附魔系统** | 角色附魔、附魔配置 | EnchantOption, CharacterEnchant | Enchant.vue | ✅ 已实现 |
| **称号系统** | 称号管理 | Title, CharacterTitle | Title.vue | ✅ 已实现 |
| **宠物系统** | 宠物管理 | Pet, CharacterPet | Pet.vue | ✅ 已实现 |
| **迷雾系统** | 迷雾融合 | FogSystem | Mist.vue | ✅ 已实现 |
| **誓约系统** | 誓约配置 | OathConfig | - | ✅ 已实现 |
| **时装系统** | 时装属性 | - | Fashion.vue | ⚠️ 待完善 |
| **换装系统** | BUFF换装 | - | - | ❌ 未实现 |
| **徽章系统** | 徽章镶嵌、星徽 | - | - | ❌ 未实现（待改版） |

---

## 零、系统架构概览

> DNF系统分为**角色系统**和**账户系统**两大类

### 0.0 系统分类

| 分类 | 作用域 | 说明 |
|-----|-------|------|
| **角色系统** | 单个角色 | 打造系统 + 进度系统，每个角色独立 |
| **账户系统** | 跨区（跨1/跨2/...） | 迷雾、收集箱、冒险团，跨区下所有角色共享 |

### 0.1 角色系统结构

> **打造系统 + 进度系统 = 名望值 + 模拟伤害（奶系为增益量）**

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           角色系统（单角色生效）                               │
│                                                                              │
│   ┌─────────────────────────────┐    ┌─────────────────────────────┐        │
│   │       打造系统               │    │       进度系统              │        │
│   │   (enhancement)             │    │   (progress)               │        │
│   ├─────────────────────────────┤    ├─────────────────────────────┤        │
│   │  • 增幅 (amplify)           │    │  • 装备 (equipment)        │        │
│   │  • 附魔 (enchant)           │    │  • 贴膜 (membrane)         │        │
│   │  • 徽章 (emblem)            │    │    └ 狄瑞吉装备升级         │        │
│   │  • 称号 (title)             │    │  • 誓约 (oath)             │        │
│   │  • 宠物 (pet)               │    │                            │        │
│   │  • 时装 (costume)           │    │                            │        │
│   │  • 换装 (buffSwap)          │    │                            │        │
│   └──────────────┬──────────────┘    └──────────────┬──────────────┘        │
│                  │                                   │                        │
│                  └───────────────┬───────────────────┘                        │
│                                  ▼                                            │
│                    ┌─────────────────────────┐                               │
│                    │     名望值 (fameValue)  │                               │
│                    │  模拟伤害 (simDamage)   │  ← 输出职业                    │
│                    │  增益量 (buffBonus)    │  ← 奶系                       │
│                    └─────────────────────────┘                               │
└─────────────────────────────────────────────────────────────────────────────┘
```

#### 角色系统子系统

| 子系统 | 模块 | 说明 |
|-------|------|------|
| **打造系统** | 增幅 | 装备增幅/强化，提升四维和技攻 |
| | 附魔 | 装备附魔宝珠，提升属强和攻强 |
| | 徽章 | 徽章镶嵌和星徽，提升技攻/增益量 |
| | 称号 | 称号属性，属强+技攻 |
| | 宠物 | 宠物及宠物装备，三攻/属强/攻强 |
| | 时装 | 时装属性，攻速/释放/四维 |
| | 换装 | 换装系统，技攻(C)/增益量(奶) |
| **进度系统** | 装备 | 12套装备系统，技攻/攻强核心 |
| | 贴膜 | 狄瑞吉装备升级，防具/武器评分 |
| | 誓约 | 誓约模式，影响觉醒爆发 |

#### 角色其他系统
- **工会系统** - 角色所属工会，工会技能加成 ⚠️ 记录项
- **结婚系统** - 角色婚姻信息 ⚠️ 记录项

### 0.2 账户系统结构

> **跨区生效，该跨区下所有角色共享**

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           账户系统（跨区生效）                                 │
│                                                                              │
│   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                         │
│   │   迷雾系统   │  │  收集箱系统  │  │  冒险团系统  │                         │
│   │(fog/mist)   │  │(collection) │  │ (adventure) │                         │
│   └─────────────┘  └─────────────┘  └─────────────┘                         │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

| 系统 | 作用域 | 说明 |
|-----|-------|------|
| **迷雾系统** | 跨区 | 迷雾融合系统，全角色共享名望和技攻 |
| 收集箱系统 | 跨区 | 收集箱属性加成 ⚠️ 记录项 |
| 冒险团系统 | 跨区 | 冒险团等级和奖励 |

#### 迷雾融合系统详情（v1.08新增）

> **迷雾融合系统**是2026年版本推出的全新玩法，开局初始等级为1级，全角色适用
> **根据等级自动计算属性：名望、技能伤害(技攻%)、增益量、四维**

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| fogLevel | Integer | 迷雾融合等级 | 1-999 |
| fogFameValue | Integer | 冒险家名望加成 | 1000（1级） |
| fogTechAttack | Float | 技能伤害(技攻%) | 10.0（1级） |
| fogBuffBonus | Integer | 增益量加成 | 0 |
| fogFourStats | Integer | 四维加成 | 0 |
| fogCurrentExp | Integer | 当前经验值 | 0 |
| fogMaxExp | Integer | 升级所需经验 | 10000 |

**迷雾升级途径：**
- 打深渊
- 征讨地下城
- 军团本
- 攻坚战

```json
{
  "fogSystem": {
    "level": 1,
    "fameValue": 1000,
    "techAttack": 10.0,
    "buffBonus": 0,
    "fourStats": 0,
    "currentExp": 0,
    "maxExp": 10000
  }
}
```

### 0.3 完整架构图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           账户系统（跨区生效）                                 │
│   ┌─────────────┐  ┌─────────────┐  ┌─────────────┐                         │
│   │   迷雾系统   │  │  收集箱系统  │  │  冒险团系统  │                         │
│   └─────────────┘  └─────────────┘  └─────────────┘                         │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
                                    ▼
┌─────────────────────────────────────────────────────────────────────────────┐
│                           角色系统（单角色生效）                               │
│                                                                              │
│   ┌─────────────┐                        ┌─────────────┐                     │
│   │   基础信息   │◀──── 核心驱动 ────▶│  战斗属性    │                     │
│   │(identity)   │    jobNature/jobAttr   │  (stats)   │                     │
│   └─────────────┘                        └─────────────┘                     │
│         │                                      ▲                            │
│         │                                      │                            │
│         ▼                                      │                            │
│   ┌───────────┐                        ┌───────┴───────┐                     │
│   │ 打造系统  │◀──────────────────────▶│   进度系统    │                     │
│   │          │                        │              │                     │
│   │• 增幅    │                        │• 装备        │                     │
│   │• 附魔    │                        │• 贴膜(狄瑞吉) │                     │
│   │• 徽章    │                        │• 誓约        │                     │
│   │• 称号    │                        │              │                     │
│   │• 宠物    │                        └──────────────┘                     │
│   │• 时装    │                                │                            │
│   │• 换装    │                                ▼                            │
│   └───────────┘                        ┌─────────────┐                     │
│                                          │   名望值    │                     │
│                                          │  模拟伤害   │                     │
│                                          │  增益量     │                     │
│                                          └─────────────┘                     │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 一、系统联动关系图

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                              核心驱动字段                                     │
│                           ┌─────────────┐                                   │
│                           │  jobNature  │ ← 决定显示：C/奶 打造体系             │
│                           │  (输出/奶系) │ ← 决定显示：物理/魔法 属性            │
│                           └─────────────┘                                   │
└─────────────────────────────────────────────────────────────────────────────┘
                                    │
        ┌───────────────────────────┼───────────────────────────┐
        │                           │                           │
        ▼                           ▼                           ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│   基础信息     │         │   战斗属性      │         │   打造系统     │
│  (identity)   │────────▶│   (stats)     │◀────────│(enhancement)  │
└───────────────┘         └───────────────┘         └───────────────┘
        │                           ▲                           │
        │                           │                           │
        ▼                           │                           ▼
┌───────────────┐         ┌───────────────┐         ┌───────────────┐
│   装备系统     │────────▶│   名望值       │◀────────│   徽章系统      │
│ (equipment)   │         │  (fameValue)  │         │   (emblem)    │
└───────────────┘         └───────────────┘         └───────────────┘
        │
        ├──┬────────────────────────┬─────────────────────────────┐
        │  │                        │                             │
        ▼  ▼                        ▼                             ▼
┌───────────────┐  ┌───────────────┐  ┌───────────────┐
│   誓约系统     │   │   秘宝装备     │  │   狄瑞吉系统   │
│   (oath)      │  │(secretTreasure│  │   (upgrade)   │
└───────────────┘  └───────────────┘  └───────────────┘
        │
        └── VP系统 ⚠️ 记录项（暂不参与开发）
```

### 1.1 联动关系详解

#### 1️⃣ 基础信息 → 其他系统（顶层驱动）
```
基础信息
├── jobNature (输出/奶系)
│   └── 决定打造系统显示：enhancementC / enhancementSupport
├── jobAttribute (物理/魔法)
│   └── 决定附魔/宝珠选择：物理系 vs 魔法系
└── serverName / adventureName
    └── 关联冒险团/区服，用于DNF同步
```

#### 2️⃣ 装备系统 → 战斗属性 → 名望值（核心链路）
```
装备系统
├── 12套装备 ──────────────────────┐
│   └── 直接影响：技攻/攻击强化    │ ← 核心差异，不是四维
│   └── 决定伤害计算和搭配策略     │
├── 誓约系统 ──────────────────────▶ 战斗属性 ──▶ 名望值
│   └── 影响觉醒伤害/爆发模式      │
└── 秘宝装备 ──────────────────────┘
    └── 特殊装备升级为太初，进一步提升属性
```

#### 3️⃣ 狄瑞吉系统 → 装备系统（装备升级链）
```
狄瑞吉装备系统
├── 防具升级 ──────▶ 影响防具部位的评分和属性
│   └── 材料：瘟疫之种，等级1-5
├── 武器调适 ──────▶ 影响太初传世武器
│   └── 材料：黑色灾祸，精准度0-100
├── 武器喂养 ──────▶ 影响技攻/增益量
│   └── 精准度越高，喂养收益越大
└── 制式武器升级 ──▶ 影响太初制式武器
    └── 材料：瘟疫之种 + 升级券
```

#### 4️⃣ 打造系统 ↔ 战斗属性（双向影响）
```
打造系统（C/奶分离，细分为5个子模块）
├── 增幅 ─────────────▶ 技攻 + 攻击强化
├── 附魔 ─────────────▶ 属强 + 攻击强化
├── 称号 ─────────────▶ 属强 + 技攻
├── 宠物 ─────────────▶ 三攻/属强 + 攻强
└── 换装 ─────────────▶ 技攻（C）/ 增益量（奶）
    └── 独立于主装备系统，记录换装搭配
```

#### 5️⃣ 徽章系统 ↔ 战斗属性（独立属性加成）
```
徽章系统
├── 徽章镶嵌 ─────────▶ 力量/智力/暴击（多彩/单色）
├── 星徽系统 ─────────▶ 技攻/增益量（根据品级和阶段）
│   └── 星徽等级：稀有/神器/传说/史诗
└── 升级材料 ──────────▶ 五曜星光
```

#### 6️⃣ VP系统 ⚠️ 记录项（暂不参与开发）
```
VP系统（个人习惯，记录项）
├── 形态选择 ─────────▶ 特定技能效果变化
├── 伤害强化 ─────────▶ 特定技能伤害提升
└── 点数分配 ─────────▶ 总点数 = 职业基础 + 装备加成
```

### 1.2 属性流向汇总

```
┌────────────────────────────────────────────────────────────────────────────┐
│                              属性流向图                                      │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                            │
│   【装备基础属性】                        【打造加成属性】                      │
│   ┌──────────┐                         ┌──────────┐                        │
│   │ 12套装备  │──▶ 技攻/攻击强化          │ 增幅     │──▶ 技攻/攻强             │
│   └──────────┘     (核心差异)           └──────────┘                        │
│   ┌──────────┐                          ┌──────────┐                      │
│   │ 誓约模式  │──▶ 觉醒/爆发伤害            │ 附魔     │──▶ 属强/攻强           │
│   └──────────┘                          └──────────┘                      │
│   ┌──────────┐                          ┌──────────┐                      │
│   │ 秘宝装备  │──▶ 特殊装备太初化           │ 称号     │──▶ 属强/技攻           │
│   └──────────┘                          └──────────┘                      │
│   ┌──────────┐                          ┌──────────┐                      │
│   │ 狄瑞吉升级│──▶ 防具/武器评分            │ 宠物     │──▶ 三攻/属强           │
│   └──────────┘                          └──────────┘                      │
│                                         ┌──────────┐                      │
│                                         │ 换装     │──▶ 技攻(C)/增益量(奶)   │
│   ⚠️ VP系统/工会/结婚/收集箱 ──▶ 记录项     └──────────┘                       │
│                                                                           │
│          │            │            │            │                         │
│          ▼            ▼            ▼            ▼                         │
│   ┌────────────────────────────────────────────────────┐                  │
│   │                   战斗属性汇总                       │                  │
│   │  技攻 + 攻强 + 属强 + 暴击 + 速度 + 防御 + ...         │                  │
│   │  模拟伤害(C)  |  站街三攻(奶/增益量)                   │                  │
│   └────────────────────────────────────────────────────┘                  │
│                              │                                            │
│                              ▼                                            │
│                    ┌─────────────────┐                                    │
│                    │     名望值       │                                    │
│                    │  (fameValue)    │                                    │
│                    └─────────────────┘                                    │
└────────────────────────────────────────────────────────────────────────────┘
```

### 1.3 数据依赖关系表

| 系统A | 依赖关系 | 系统B | 作用域 | 说明 |
|-------|---------|-------|--------|------|
| 打造系统 | ← 依赖 | 基础信息 | 角色 | jobNature决定C/奶显示分支 |
| 打造系统 | ← 依赖 | 基础信息 | 角色 | jobAttribute决定物理/魔法附魔 |
| 战斗属性 | ← 依赖 | 装备系统 | 角色 | 12套装备贡献主要属性 |
| 战斗属性 | ← 依赖 | 打造系统 | 角色 | 增幅/附魔/称号/宠物/换装加成 |
| 战斗属性 | ← 依赖 | 徽章系统 | 角色 | 徽章和星徽的属性加成 |
| 名望值 | ← 依赖 | 战斗属性 | 角色 | 所有属性综合计算 |
| 誓约系统 | ⊂ 从属 | 装备系统 | 角色 | 誓约是装备系统的子系统 |
| 秘宝装备 | ⊂ 从属 | 装备系统 | 角色 | 秘宝是特殊装备的升级 |
| 狄瑞吉系统 | ⊂ 从属 | 装备系统 | 角色 | 升级影响装备属性 |
| 换装 | ⊂ 从属 | 打造系统 | 角色 | 独立于主装备的换装记录 |
| 迷雾系统 | — | 账户 | 跨区 | 跨区下所有角色共享 |
| 收集箱系统 | — | 账户 | 跨区 | 跨区下所有角色共享 |
| 冒险团系统 | — | 账户 | 跨区 | 跨区下所有角色共享 |
| VP系统 | ⚠️ | 记录项 | — | 个人习惯，不参与开发 |
| 工会系统 | ⚠️ | 记录项 | 跨区 | 不参与开发 |
| 结婚系统 | ⚠️ | 记录项 | 跨区 | 不参与开发 |

---

## 一、基础信息（已有）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| id | Long | 角色ID | 1 |
| userId | Long | 用户ID | 1 |
| characterName | String | 角色名称 | 剑影-X |
| characterTypeId | Long | 职业类型ID | 1 |
| jobNature | String | 职业性质 | 输出/奶系 |
| jobAttribute | String | 职业属性 | 物理/魔法/奶 |
| serverName | String | 区服名称 | 跨一 |
| adventureName | String | 冒险团名称 | 我的冒险团 |
| level | Integer | 角色等级 | 110 |
| fameValue | Integer | 站街名望 | 98650 |
| avatarUrl | String | 角色头像URL | https://... |

---

## 二、战斗属性

> 以下根据游戏角色面板的实际分组方式进行组织
> **"有效"** = 字体为亮色，该职业可享受该属性加成
> **"无效"** = 字体为灰色，该职业无法享受该属性

### 2.1 HP/MP（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| hp | Integer | HP | 85620 |
| mp | Integer | MP | 28540 |
| hpMax | Integer | HP最大值 | 85620 |
| mpMax | Integer | MP最大值 | 28540 |

### 2.2 物理/魔法属性（根据 jobAttribute 显示）

> **物理职业**：亮色显示物理攻击、物理暴击；智力显示为灰色（无效）
> **魔法职业**：亮色显示魔法攻击、魔法暴击；力量显示为灰色（无效）

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| physicalAttack | Integer | 物理攻击力 | 物理职业 | 125680 |
| magicalAttack | Integer | 魔法攻击力 | 魔法职业 | 115420 |
| independentAttack | Integer | 独立攻击力 | 物理/魔法 | 3850 |
| physicalCritRate | Integer | 物理暴击率(%) | 物理职业 | 50 |
| magicalCritRate | Integer | 魔法暴击率(%) | 魔法职业 | 48 |

### 2.3 三攻属性（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| physicalAttack | Integer | 物理攻击力 | 125680 |
| magicalAttack | Integer | 魔法攻击力 | 115420 |
| independentAttack | Integer | 独立攻击力 | 3850 |

### 2.4 三速属性（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| attackSpeed | Integer | 攻击速度(%) | 45 |
| castingSpeed | Integer | 施放速度(%) | 53 |
| moveSpeed | Integer | 移动速度(%) | 150 |

### 2.5 暴击/命中率/回避率（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| critRate | Integer | 暴击率(%) | 50 |
| hitRate | Integer | 命中率(%) | 108 |
| avoidance | Integer | 回避率(%) | 24 |

### 2.6 防御属性（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| physicalDefense | Integer | 物理防御力 | 125680 |
| magicalDefense | Integer | 魔法防御力 | 98240 |

### 2.7 属性抗性（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| lightResistance | Integer | 光属性抗性 | 40 |
| fireResistance | Integer | 火属性抗性 | 35 |
| iceResistance | Integer | 冰属性抗性 | 42 |
| darkResistance | Integer | 暗属性抗性 | 38 |

### 2.8 HP/MP恢复量（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| hpRecovery | Integer | HP恢复量 | 1250 |
| mpRecovery | Integer | MP恢复量 | 680 |

### 2.9 四维属性

> **物理职业**：力量亮色，智力灰色（无效）
> **魔法职业**：智力亮色，力量灰色（无效）
> **奶系职业**：体力/精神亮色，力量/智力也亮色（奶系需要四维）

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| strength | Integer | 力量 | 物理职业/奶系 | 8562 |
| intelligence | Integer | 智力 | 魔法职业/奶系 | 6854 |
| vitality | Integer | 体力 | 奶系 | 4521 |
| spirit | Integer | 精神 | 奶系 | 4230 |

### 2.10 属性强化（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| lightElement | Integer | 光属性强化 | 520 |
| fireElement | Integer | 火属性强化 | 380 |
| iceElement | Integer | 冰属性强化 | 420 |
| darkElement | Integer | 暗属性强化 | 350 |
| maxElement | Integer | 最高属强（计算字段） | 520 |

### 2.11 核心输出属性（输出职业有效，奶系无效）

> **技攻 vs 攻击强化**：
> - 技攻(techniqueAttack)：乘算稀释，百分比提升，全职业统一计算
> - 攻击强化(attackBonus)：加算叠加，各职业独立计算，决定角色强度

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| techniqueAttack | Float | 技能攻击力（技攻） | 输出职业(C) | 186.5 |
| attackBonus | Integer | 攻击强化 | 输出职业(C) | 58240 |
| finalDamage | Float | 最终伤害 | 输出职业(C) | 1.0 |
| whiteCritDamage | Float | 白字（附加伤害/追加伤害） | 输出职业(C) | 35.0 |
| yellowCritDamage | Float | 黄字追加伤害 | 输出职业(C) | 20.0 |
| critDamageBonus | Float | 爆伤追加 | 输出职业(C) | 30.0 |
| skillLvBonus | Integer | 技攻Lv加成（特定技能等级提升） | 输出职业(C) | 0 |
| skillAttackBonus | Float | 技攻角色攻击力（特定技能攻击力提升） | 输出职业(C) | 0 |

### 2.12 奶系专属属性（奶系职业有效，输出职业无效）

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| buffLevel | Integer | BUFF换装等级 | 奶系 | 48 |
| buffStrength | Integer | BUFF增加的力量/体力 | 奶系 | 2150 |
| buffIntelligence | Integer | BUFF增加的智力/精神 | 奶系 | 2100 |
| buffBonus | Integer | 增益量（奶系BUFF三攻加成） | 奶系 | 14580 |
| statStrength | Integer | 站街体力和力量（奶系四维面板） | 奶系 | 8562 |
| statIntelligence | Integer | 站街智力和精神（奶系四维面板） | 奶系 | 8562 |

> **站街三攻(statAttack)**：
> - 奶系专属，代表奶的站街面板三攻能给队友加多少三攻
> - 三攻越高，奶给C的三攻增益量越大

### 2.13 模拟战斗属性（输出职业有效，奶系无效）

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| simulateDamage | Integer | 模拟伤害（修炼场沙袋伤害） | 输出职业(C) | 85620000 |

> **模拟伤害(simulateDamage)**：
> - 修炼场沙袋伤害，反映C的整体打造水平
> - 打造越好，属性越高，模拟伤害越高

### 2.14 特殊功能属性（所有职业有效）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| itemDropBonus | Integer | 物品掉落增加(%) | 0 |
| goldBonus | Integer | 金币获取增加(%) | 0 |

---

## 三、职业属性有效性汇总表

### 3.1 物理输出职业（C）- jobAttribute = 物理

| 属性类别 | 字段 | 有效性 | 说明 |
|---------|------|-------|------|
| HP/MP | hpMax, mpMax | ✅ 有效 | - |
| 三攻 | physicalAttack, independentAttack | ✅ 有效 | magicalAttack ❌无效 |
| 暴击 | physicalCritRate, critRate | ✅ 有效 | magicalCritRate ❌无效 |
| 三速 | attackSpeed, castingSpeed, moveSpeed | ✅ 有效 | - |
| 命中/回避 | hitRate, avoidance | ✅ 有效 | - |
| 防御 | physicalDefense, magicalDefense | ✅ 有效 | - |
| 属性抗性 | light/fire/ice/darkResistance | ✅ 有效 | - |
| 恢复 | hpRecovery, mpRecovery | ✅ 有效 | - |
| 四维 | strength | ✅ 有效 | intelligence ❌无效 |
| 属强 | light/fire/ice/darkElement | ✅ 有效 | - |
| 核心输出 | techniqueAttack, attackBonus | ✅ 有效 | - |
| 伤害词缀 | finalDamage, whiteCritDamage, yellowCritDamage, critDamageBonus | ✅ 有效 | - |
| 奶系专属 | buffLevel, buffStrength, buffIntelligence, buffBonus | ❌ 无效 | 灰色显示 |
| 奶系BUFF | statStrength, statIntelligence | ❌ 无效 | - |
| 模拟伤害 | simulateDamage | ✅ 有效 | - |

### 3.2 魔法输出职业（C）- jobAttribute = 魔法

| 属性类别 | 字段 | 有效性 | 说明 |
|---------|------|-------|------|
| HP/MP | hpMax, mpMax | ✅ 有效 | - |
| 三攻 | magicalAttack, independentAttack | ✅ 有效 | physicalAttack ❌无效 |
| 暴击 | magicalCritRate, critRate | ✅ 有效 | physicalCritRate ❌无效 |
| 三速 | attackSpeed, castingSpeed, moveSpeed | ✅ 有效 | - |
| 命中/回避 | hitRate, avoidance | ✅ 有效 | - |
| 防御 | physicalDefense, magicalDefense | ✅ 有效 | - |
| 属性抗性 | light/fire/ice/darkResistance | ✅ 有效 | - |
| 恢复 | hpRecovery, mpRecovery | ✅ 有效 | - |
| 四维 | intelligence | ✅ 有效 | strength ❌无效 |
| 属强 | light/fire/ice/darkElement | ✅ 有效 | - |
| 核心输出 | techniqueAttack, attackBonus | ✅ 有效 | - |
| 伤害词缀 | finalDamage, whiteCritDamage, yellowCritDamage, critDamageBonus | ✅ 有效 | - |
| 奶系专属 | buffLevel, buffStrength, buffIntelligence, buffBonus | ❌ 无效 | 灰色显示 |
| 奶系BUFF | statStrength, statIntelligence | ❌ 无效 | - |
| 模拟伤害 | simulateDamage | ✅ 有效 | - |

### 3.3 奶系职业 - jobNature = 奶系

| 属性类别 | 字段 | 有效性 | 说明 |
|---------|------|-------|------|
| HP/MP | hpMax, mpMax | ✅ 有效 | - |
| 三攻 | 三攻字段用于奶系BUFF计算 | ⚠️ 部分 | - |
| 暴击 | critRate | ✅ 有效 | - |
| 三速 | attackSpeed, castingSpeed, moveSpeed | ✅ 有效 | - |
| 命中/回避 | hitRate, avoidance | ✅ 有效 | - |
| 防御 | physicalDefense, magicalDefense | ✅ 有效 | - |
| 属性抗性 | light/fire/ice/darkResistance | ✅ 有效 | - |
| 恢复 | hpRecovery, mpRecovery | ✅ 有效 | - |
| 四维 | vitality, spirit | ✅ 有效 | strength/intelligence也有效 |
| 属强 | light/fire/ice/darkElement | ✅ 有效 | - |
| 奶系专属 | buffLevel, buffStrength, buffIntelligence, buffBonus | ✅ 有效 | 亮色显示 |
| 站街面板 | statStrength, statIntelligence | ✅ 有效 | 奶系站街四维 |
| 增益量 | buffBonus | ✅ 有效 | 核心属性 |
| 核心输出 | techniqueAttack, attackBonus | ❌ 无效 | 灰色显示 |
| 伤害词缀 | finalDamage, whiteCritDamage等 | ❌ 无效 | 灰色显示 |
| 模拟伤害 | simulateDamage | ❌ 无效 | 灰色显示 |

---

## 四、装备系统（待添加）

### 4.1 装备套装（12套）

#### 核心差异说明
> **12套装备的主要区别不是四维，而是技攻(techniqueAttack)和攻击强化(attackBonus)**
>
> 不同套装提供不同类型的技攻/攻强词缀，决定了伤害计算方式和搭配策略

#### 套装列表

1. 理想之黄金乡套
2. 龙站八荒套
3. 潜影暗袭套
4. 造化自然套
5. 天命者的气运套
6. 究极能量套
7. 混沌净化套
8. 诸神黄昏之女武神套
9. 青丘灵珠套
10. 精灵国度套
11. 冥思者的领域套
12. 群猎美学套


#### 装备品级说明
| 部位 | 最高品级 | 说明 |
|-----|---------|-----|
| 防具 | 史诗 | - |
| 首饰 | 太初 | - |
| 特殊装备 | 史诗 | 耳环/魔法石可制作秘宝升级为太初 |
| 武器 | 太初 | - |

#### 字段设计（JSON格式）
```json
{
  "equipmentSet": {
    "setName": "究极能量套",
    "setIndex": 7,
    "setScore": 280,
    "pieces": {
      "weapon": {
        "name": "胜负之役棍棒",
        "type": "传世武器",
        "grade": "太初",
        "enhanceLevel": 25,
        "amplifyLevel": 0
      },
      "armor": {
        "head": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "chest": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "legs": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "shoulder": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "shoes": { "name": "xxx", "grade": "史诗", "enchant": "xxx" }
      },
      "accessory": {
        "necklace": { "name": "xxx", "grade": "太初", "enchant": "xxx" },
        "bracelet": { "name": "xxx", "grade": "太初", "enchant": "xxx" },
        "ring": { "name": "xxx", "grade": "太初", "enchant": "xxx" }
      },
      "special": {
        "subEquip": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "magicStone": { "name": "xxx", "grade": "史诗", "enchant": "xxx" },
        "auxiliary": { "name": "xxx", "grade": "史诗", "enchant": "xxx" }
      }
    }
  }
}
```

### 4.2 誓约系统

> 替代原有的融合石系统

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| oathMode | Enum | 誓约模式 | BASIC/MODE1/MODE2 |
| oathAttribute | String | 当前激活的誓约属性 | 压倒性的力量 |

#### 誓约模式说明
| 模式 | 名称 | 效果 |
|-----|------|-----|
| BASIC | 无限动力 | 觉醒CD-30%，伤害均衡 |
| MODE1 | 压倒性的力量 | 极端三觉伤害爆发 |
| MODE2 | 超载 | 一二觉+80%/+96%，可刷新 |

### 4.3 秘宝装备

> 土豪玩家专属太初特殊装备

| 字段名 | 数据类型 | 说明 |
|-------|---------|-----|
| secretTreasure | JSON | 秘宝装备列表 |

```json
{
  "secretTreasures": [
    { "name": "秘宝耳环", "grade": "太初", "slot": "earring" },
    { "name": "秘宝魔法石", "grade": "太初", "slot": "magicStone" },
    { "name": "秘宝辅助装备", "grade": "太初", "slot": "auxiliary" }
  ]
}
```

---

## 五、工会系统 ⚠️ 记录项（暂不参与开发）

> 以下内容仅作为信息记录，不纳入实际开发设计

### 5.1 工会信息
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| guildName | String | 工会名称 | 永恒之巅 |
| guildLevel | Integer | 工会等级 | 30 |
| guildRank | String | 工会排名 | 跨区前100 |
| memberCount | Integer | 成员数量 | 45/50 |
| joinDate | LocalDateTime | 加入时间 | 2024-06-15 |

### 5.2 工会技能
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| guildSkillPoints | Integer | 可用技能点数 | 120 |
| guildSkillUsed | Integer | 已使用技能点数 | 95 |
| guildSkillFortune | Integer | 幸运技能等级 | 10 |
| guildSkillStrength | Integer | 力量技能等级 | 5 |
| guildSkillIntelligence | Integer | 智力技能等级 | 5 |
| guildSkillVitality | Integer | 体力技能等级 | 3 |
| guildSkillSpirit | Integer | 精神技能等级 | 3 |
| guildSkillCrit | Integer | 暴击技能等级 | 5 |
| guildSkillDefense | Integer | 防御技能等级 | 3 |
| guildSkillHp | Integer | HP技能等级 | 2 |

### 5.3 工会属性加成
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| guildBonusStrength | Integer | 力量加成 | +65 |
| guildBonusIntelligence | Integer | 智力加成 | +65 |
| guildBonusVitality | Integer | 体力加成 | +45 |
| guildBonusSpirit | Integer | 精神加成 | +40 |
| guildBonusCrit | Integer | 暴击率加成 | +5% |
| guildBonusDefense | Integer | 防御力加成 | +1500 |
| guildBonusHp | Integer | HP最大值加成 | +500 |

```json
{
  "guildSystem": {
    "guildInfo": {
      "name": "永恒之巅",
      "level": 30,
      "rank": "跨区前100",
      "memberCount": 45,
      "maxMembers": 50,
      "joinDate": "2024-06-15"
    },
    "guildSkills": {
      "availablePoints": 120,
      "usedPoints": 95,
      "skills": {
        "fortune": 10,
        "strength": 5,
        "intelligence": 5,
        "vitality": 3,
        "spirit": 3,
        "crit": 5,
        "defense": 3,
        "hp": 2
      }
    },
    "guildBonuses": {
      "strength": 65,
      "intelligence": 65,
      "vitality": 45,
      "spirit": 40,
      "crit": 5,
      "defense": 1500,
      "hp": 500
    }
  }
}
```

---

## 六、狄瑞吉装备升级系统

### 6.1 狄瑞吉防具升级
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| armorUpgradeMaterial | String | 升级材料 | 瘟疫之种 |
| armorUpgradeCost | Integer | 升级消耗（每部位） | 480 |
| armorUpgradeLevel | Integer | 升级等级 | 1-5 |
| armorUpgradeScore | Integer | 升级增加积分 | 20/部位 |

```json
{
  "dirizgiArmorUpgrade": {
    "material": "瘟疫之种",
    "totalCost": 2400,
    "upgradedPieces": ["head", "chest", "legs"],
    "upgradeLevel": 1,
    "additionalScore": 60
  }
}
```

### 6.2 太初传世武器调适与升级
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| weaponAdaptationMaterial | String | 调适材料 | 黑色灾祸 |
| weaponAdaptationLevel | Integer | 调适层数 | 0-3 |
| weaponAdaptationCost | Integer | 调适消耗（每次） | 40 |
| weaponUpgradeCost | Integer | 升级消耗（每次） | 80 |
| weaponPrecision | Float | 精准度 | 0-100 |
| isUpgraded | Boolean | 是否升级为黑色瘟疫版 | true/false |

```json
{
  "dirizgiWeapon": {
    "material": "黑色灾祸",
    "type": "太初传世武器",
    "adaptationLevel": 3,
    "isUpgraded": true,
    "precision": 0,
    "totalAdaptationCost": 200
  }
}
```

### 6.3 狄瑞吉武器喂养
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| feedingMaterial | String | 喂养材料 | 黑色灾祸 |
| feedingCost | Integer | 每次喂养消耗 | 8 |
| precisionLevel | Float | 精准度等级 | 0-100 |
| techAttackBonus | Float | 技攻加成 | 10.0 |
| buffBonus | Integer | 增益量加成 | 400 |

```json
{
  "dirizgiWeaponFeeding": {
    "material": "黑色灾祸",
    "precisionLevel": 85.5,
    "techAttackBonus": 8.55,
    "buffBonus": 3420,
    "feedingCount": 42
  }
}
```

### 6.4 太初制式武器升级
| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| materialRequired | String | 所需材料 | 瘟疫之种 |
| upgradeTicketCost | Integer | 升级券消耗 | 240 |
| conversionTicketCost | Integer | 定向转换券消耗 | 480 |
| upgradeTicketOwned | Integer | 已拥有升级券数量 | 1 |
| conversionTicketOwned | Integer | 已拥有定向转换券数量 | 0 |

---

## 七、VP系统 ⚠️ 记录项（暂不参与开发）

> 以下内容仅作为信息记录，不纳入实际开发设计

| 字段名 | 数据类型 | 说明 |
|-------|---------|-----|
| vpTotalPoints | Integer | VP总点数 |
| vpUsedPoints | Integer | VP已用点数 |
| vpRemainPoints | Integer | VP剩余点数 |
| vpFormSelections | JSON | 形态选择（5个技能） |
| vpDamageBoost | JSON | 伤害强化（3个技能） |

```json
{
  "vpSystem": {
    "totalPoints": 100,
    "usedPoints": 75,
    "remainPoints": 25,
    "formSelections": [
      { "skillName": "75级技能", "formType": "形态A", "level": 1 }
    ],
    "damageBoosts": [
      { "skillName": "45级技能", "boostType": "纯伤害", "level": 5 }
    ]
  }
}
```

---

## 八、打造系统（C/奶 分离）

> 根据 jobNature 字段自动切换显示
> 打造系统细分为5个子模块：增幅、附魔、称号、宠物、换装

### 8.1 增幅系统

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| amplifyLevel | Integer | 增幅等级 | 10 |
| amplifyAttribute | String | 增幅属性 | 力量/体力/智力/精神 |
| isReinforce | Boolean | 是否强化（非增幅） | true/false |
| weaponEnhanceLevel | Integer | 武器强化/增幅等级 | 25 |

```json
{
  "amplify": {
    "level": 10,
    "attribute": "力量",
    "isReinforce": false,
    "weaponLevel": 25,
    "weaponType": "强化"
  }
}
```

### 8.2 附魔系统

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| enchantWeapon | String | 武器附魔 | 格斗场冠军宝珠 |
| enchantHead | String | 头肩附魔 | 冲击之魂头肩宝珠 |
| enchantChest | String | 上衣附魔 | 使徒希洛克宝珠 |
| enchantLegs | String | 下装附魔 | 使徒希洛克宝珠 |
| enchantShoes | String | 鞋附魔 | 格斗场冠军宝珠 |
| enchantNecklace | String | 项链附魔 | 光之主人宝珠 |
| enchantBracelet | String | 手镯附魔 | 火之主人宝珠 |
| enchantRing | String | 戒指附魔 | 冰之主人宝珠 |
| enchantSubEquip | String | 辅助装备附魔 | 机械崛起融合石 |
| enchantMagicStone | String | 魔法石附魔 | 机械崛起融合石 |
| enchantAuxiliary | String | 耳环附魔 | 机械崛起融合石 |

```json
{
  "enchants": {
    "weapon": "格斗场冠军宝珠",
    "armor_head": "冲击之魂头肩宝珠",
    "armor_chest": "使徒希洛克宝珠",
    "armor_legs": "使徒希洛克宝珠",
    "armor_shoes": "格斗场冠军宝珠",
    "accessory_necklace": "光之主人宝珠",
    "accessory_bracelet": "火之主人宝珠",
    "accessory_ring": "冰之主人宝珠",
    "special_sub": "机械崛起融合石",
    "special_magic": "机械崛起融合石",
    "special_aux": "机械崛起融合石"
  }
}
```

### 8.3 称号系统

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| title | String | 当前称号名称 | 破坏之毁灭者 |
| titleYear | String | 称号年份 | 2024金秋 |

```json
{
  "title": {
    "name": "破坏之毁灭者",
    "year": "2024金秋",
    "effects": ["属强+15", "力智+50"]
  }
}
```

### 8.4 宠物系统

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| currentPet | String | 当前宠物名称 | 迷你铁齿狗 |
| petEquipRed | String | 红色宠物装备 | 宠物装备红 |
| petEquipBlue | String | 蓝色宠物装备 | 宠物装备蓝 |
| petEquipGreen | String | 绿色宠物装备 | 宠物装备绿 |
| petEquipAttribute | JSON | 宠物装备属性详情 | - |

```json
{
  "pet": {
    "currentPet": "迷你铁齿狗",
    "petEquips": [
      { "slot": "red", "name": "宠物装备红", "attribute": "力智+35" },
      { "slot": "blue", "name": "宠物装备蓝", "attribute": "三攻+28" },
      { "slot": "green", "name": "宠物装备绿", "attribute": "属强+10" }
    ]
  }
}
```

### 8.5 换装系统

> **换装的作用不是使用其他套装，而是增加角色的技攻(techniqueAttack)，对奶系来说增加增益量(buffBonus)**

| 字段名 | 数据类型 | 说明 | 适用职业 | 示例 |
|-------|---------|------|---------|-----|
| buffSetName | String | 换装套装名称 | C/奶 | 深渊之冥魄套装 |
| buffLevel | Integer | BUFF等级 | C | 20 |
| techniqueBonus | Float | 换装提供的技攻加成 | C | 3.5 |
| buffBonusBonus | Integer | 换装提供的增益量加成 | 奶系 | 2800 |
| isPerfect | Boolean | 是否完美换装 | C/奶 | true/false |

```json
{
  "buffSwap": {
    "setName": "深渊之冥魄套装",
    "buffLevel": 20,
    "techniqueBonus": 3.5,
    "buffBonusBonus": 0,
    "isPerfect": true
  }
}
```

```json
{
  "buffSwap": {
    "setName": "奶系换装套装",
    "buffLevel": 48,
    "techniqueBonus": 0,
    "buffBonusBonus": 2800,
    "isPerfect": true
  }
}
```

### 8.6 奶系专属字段（jobNature = 奶系）

| 字段名 | 数据类型 | 说明 | 示例 |
|-------|---------|------|-----|
| amplifyAttribute | String | 增幅属性为体力/精神 | 体力/精神 |
| medal | String | 勋章名称 | 2024年Gala冠军勋章 |
| guardianBeads | JSON | 守护珠列表 | - |
| buffEnchant | JSON | 奶系专属附魔 | - |

```json
{
  "supportExclusives": {
    "medal": "2024年Gala冠军勋章",
    "guardianBeads": [
      { "attribute": "物理/魔法暴击+5.0%", "locked": true },
      { "attribute": "使徒/异性攻击+5.0%", "locked": true },
      { "attribute": "所有属性强化+24", "locked": true },
      { "attribute": "物理/魔法/独立攻击力+50", "locked": true },
      { "attribute": "四维+30", "locked": false }
    ],
    "enchants": {
      "weapon": "110级传说奶武器附魔",
      "armor_head": "节日技能宝珠（荣誉祝福+1）",
      "armor_chest": "使徒宝珠（荣誉祝福+1）",
      "armor_legs": "使徒宝珠（荣誉祝福+1）",
      "armor_shoes": "节日技能宝珠（荣誉祝福+1）"
    }
  }
}
```

### 8.7 完整JSON结构示例

```json
{
  "enhancement": {
    "amplify": {
      "level": 10,
      "attribute": "力量",
      "isReinforce": false,
      "weaponLevel": 25,
      "weaponType": "强化"
    },
    "enchants": {
      "weapon": "格斗场冠军宝珠",
      "armor_head": "冲击之魂头肩宝珠",
      "armor_chest": "使徒希洛克宝珠",
      "armor_legs": "使徒希洛克宝珠",
      "armor_shoes": "格斗场冠军宝珠",
      "accessory_necklace": "光之主人宝珠",
      "accessory_bracelet": "火之主人宝珠",
      "accessory_ring": "冰之主人宝珠",
      "special_sub": "机械崛起融合石",
      "special_magic": "机械崛起融合石",
      "special_aux": "机械崛起融合石"
    },
    "title": {
      "name": "破坏之毁灭者",
      "year": "2024金秋"
    },
    "pet": {
      "currentPet": "迷你铁齿狗",
      "petEquips": [
        { "slot": "red", "name": "宠物装备红", "attribute": "力智+35" },
        { "slot": "blue", "name": "宠物装备蓝", "attribute": "三攻+28" },
        { "slot": "green", "name": "宠物装备绿", "attribute": "属强+10" }
      ]
    },
    "buffSwap": {
      "setName": "深渊之冥魄套装",
      "buffLevel": 20,
      "armorSet": "魔战无双",
      "specialSet": "时空护甲",
      "weaponName": "释言之剑",
      "isPerfect": true
    }
  }
}
```

---

## 九、徽章系统

### 9.1 徽章改版（千海天版本）

#### 徽章系统变更
| 内容 | 说明 |
|-----|-----|
| 界面统一 | 徽章和装扮栏徽章孔合并到一个界面 |
| 徽章孔数量 | 彩色6个、红/黄/蓝/绿各4个、白金2个常驻+2个增益 |
| 新增神圣品级 | 最高品级为神圣徽章 |
| 材料 | 五曜星光（账号绑定） |

#### 升级方式
| 类型 | 说明 |
|-----|-----|
| 概率突破 | 消耗五曜星尘，有几率提升品级 |
| 必定突破 | 需要两个相同徽章，仅限灿烂/玲珑 |

#### 9.2 星徽系统（徽章槽）
| 品级 | 阶段数 | 说明 |
|-----|-------|-----|
| 稀有 | 5 | - |
| 神器 | 5 | - |
| 传说 | 5 | - |
| 史诗 | 1 | 最高品级 |

- 星徽自带属性（不镶嵌也有）
- 根据镶嵌徽章数量获得技攻和增益量属性
- 升级消耗五曜星光

---

## 十、其他系统 ⚠️ 记录项（暂不参与开发）

> 以下内容仅作为信息记录，不纳入实际开发设计

- [ ] VP系统 ⚠️ 记录项（个人习惯）
- [ ] 工会系统 ⚠️ 记录项
- [ ] 收集箱系统 ⚠️ 记录项
- [ ] 结婚系统 ⚠️ 记录项

### 已实现的其他系统
- [x] 卡片收集系统 ✅
- [x] 迷雾融合系统 ✅
- [x] 誓约配置系统 ✅

---

## 十一、字段清单汇总

### 11.1 Character 实体类字段（后端）

| 序号 | 字段名 | 类型 | 说明 | 状态 |
|-----|-------|------|-----|-----|
| 1 | id | Long | 主键 | ✅ 已有 |
| 2 | userId | Long | 用户ID | ✅ 已有 |
| 3 | characterTypeId | Long | 职业类型ID | ✅ 已有 |
| 4 | characterName | String | 角色名称 | ✅ 已有 |
| 5 | jobNature | String | 职业性质 | ✅ 已有 |
| 6 | jobAttribute | String | 职业属性 | ✅ 已有 |
| 7 | serverName | String | 区服名称 | ✅ 已有 |
| 8 | adventureName | String | 冒险团名称 | ✅ 已有 |
| 9 | level | Integer | 角色等级 | ✅ 已有 |
| 10 | fameValue | Integer | 站街名望 | ✅ 已有 |
| 11 | avatarUrl | String | 角色头像 | ✅ 已有 |
| 12-13 | 核心输出 | Float/Integer | techniqueAttack, attackBonus | ⚠️ 待完善 |
| 14-17 | 四维属性 | Integer | 力量/智力/体力/精神 | ✅ 已有 |
| 18-20 | 三攻属性 | Integer | 物理/魔法/独立攻击 | ✅ 已有 |
| 21 | simulateDamage | Integer | 模拟伤害（修炼场沙袋） | ⚠️ 待完善 |
| 22 | statAttack | Integer | 站街三攻（奶系BUFF加成量） | ⚠️ 待完善 |
| 23-27 | 属性强化 | Integer | 光/火/冰/暗/maxElement | ✅ 已有 |
| 28-32 | 防御/HP/MP | Integer | 物理/魔法防御，HP/MP | ✅ 已有 |
| 33-36 | 暴击/命中/回避 | Integer | critRate, hitRate, avoidance | ✅ 已有 |
| 37-39 | 三速 | Integer | attack/casting/moveSpeed | ✅ 已有 |
| 40-41 | 恢复量 | Integer | hpRecovery, mpRecovery | ✅ 已有 |
| 42-45 | 属性抗性 | Integer | light/fire/ice/darkResistance | ⚠️ 待完善 |
| 46-50 | 伤害词缀 | Float | finalDamage, whiteCritDamage, yellowCritDamage, critDamageBonus, skillLvBonus | ⚠️ 待完善 |
| 51-52 | 奶系BUFF | Integer | buffStrength, buffIntelligence | ⚠️ 待完善 |
| 53-56 | 装备/誓约/打造 | JSON/String | equipmentData, oathMode, oathAttribute, enhancementData | ⚠️ 部分已有 |
| 57 | lastSyncTime | LocalDateTime | 最后同步时间 | ✅ 已有 |
| 58 | syncStatus | String | 同步状态 | ✅ 已有 |
| 59 | syncErrorMsg | String | 同步错误信息 | ✅ 已有 |
| 60 | createdAt | LocalDateTime | 创建时间 | ✅ 已有 |
| 61 | updatedAt | LocalDateTime | 更新时间 | ✅ 已有 |

### 11.2 独立系统实体类

| 系统 | 实体类 | 说明 | 状态 |
|-----|-------|-----|-----|
| 卡片系统 | CardTemplate, CharacterCard | 卡片模板和角色卡片 | ✅ 已有 |
| 装备系统 | EquipmentSet, EquipmentItem, CharacterEquipment | 装备套装和物品 | ✅ 已有 |
| 增幅系统 | AmplifyOption, CharacterAmplify | 增幅配置和角色增幅 | ✅ 已有 |
| 附魔系统 | EnchantOption, CharacterEnchant | 附魔配置和角色附魔 | ✅ 已有 |
| 称号系统 | Title, CharacterTitle | 称号和角色称号 | ✅ 已有 |
| 宠物系统 | Pet, CharacterPet | 宠物和角色宠物 | ✅ 已有 |
| 迷雾系统 | FogSystem | 迷雾融合 | ✅ 已有 |
| 誓约系统 | OathConfig | 誓约配置 | ✅ 已有 |

---

## 十二、数据库变更

### 已完成的数据库变更

以下表已通过 JPA/Hibernate 自动创建：

| 表名 | 说明 | 状态 |
|-----|-----|-----|
| users | 用户表 | ✅ 已创建 |
| character_types | 职业类型表 | ✅ 已创建 |
| characters | 角色表 | ✅ 已创建 |
| fog_systems | 迷雾系统表 | ✅ 已创建 |
| oath_configs | 誓约配置表 | ✅ 已创建 |
| equipment_sets | 装备套装表 | ✅ 已创建 |
| equipment_items | 装备物品表 | ✅ 已创建 |
| character_equipment | 角色装备关联表 | ✅ 已创建 |
| card_templates | 卡片模板表 | ✅ 已创建 |
| character_cards | 角色卡片表 | ✅ 已创建 |
| amplify_options | 增幅配置表 | ✅ 已创建 |
| character_amplifies | 角色增幅表 | ✅ 已创建 |
| enchant_options | 附魔配置表 | ✅ 已创建 |
| character_enchants | 角色附魔表 | ✅ 已创建 |
| titles | 称号表 | ✅ 已创建 |
| character_titles | 角色称号表 | ✅ 已创建 |
| pets | 宠物表 | ✅ 已创建 |
| character_pets | 角色宠物表 | ✅ 已创建 |

### 待完善的字段变更

以下字段需要在 Character 表中完善：

```sql
-- 核心输出属性（待完善）
ALTER TABLE characters
    ADD COLUMN technique_attack FLOAT COMMENT '技能攻击力（技攻）',
    ADD COLUMN attack_bonus INT COMMENT '攻击强化',
    ADD COLUMN buff_bonus INT COMMENT '增益量（奶系BUFF三攻加成）',
    ADD COLUMN simulate_damage INT COMMENT '模拟伤害（修炼场沙袋）',
    ADD COLUMN stat_attack INT COMMENT '站街三攻（奶系BUFF加成量）';

-- 属性抗性（待完善）
ALTER TABLE characters
    ADD COLUMN light_resistance INT COMMENT '光属性抗性',
    ADD COLUMN fire_resistance INT COMMENT '火属性抗性',
    ADD COLUMN ice_resistance INT COMMENT '冰属性抗性',
    ADD COLUMN dark_resistance INT COMMENT '暗属性抗性';

-- 伤害词缀（待完善）
ALTER TABLE characters
    ADD COLUMN final_damage FLOAT COMMENT '最终伤害',
    ADD COLUMN white_crit_damage FLOAT COMMENT '白字附加伤害',
    ADD COLUMN yellow_crit_damage FLOAT COMMENT '黄字追加伤害',
    ADD COLUMN crit_damage_bonus FLOAT COMMENT '爆伤追加',
    ADD COLUMN skill_lv_bonus INT COMMENT '技攻Lv加成';

-- 奶系BUFF（待完善）
ALTER TABLE characters
    ADD COLUMN buff_strength INT COMMENT 'BUFF增加的力量/体力',
    ADD COLUMN buff_intelligence INT COMMENT 'BUFF增加的智力/精神';
```

> ⚠️ VP系统字段暂不添加（记录项）

---

## 十三、实现优先级

| 优先级 | 内容 | 说明 | 状态 |
|-------|-----|-----|-----|
| P0 | 核心属性 | 技攻/攻击强化/增益量/模拟伤害/站街三攻 | ⚠️ 部分实现 |
| P1 | 装备系统 | 12套装备 + 誓约模式（核心差异：技攻/攻强） | ✅ 已实现 |
| P2 | 打造系统 | 增幅/附魔/称号/宠物/换装 | ✅ 已实现（换装除外） |
| P3 | 徽章系统 | 等改版后补充 | ❌ 待改版 |
| P4 | 狄瑞吉系统 | 装备升级链 | ❌ 未实现 |

> ⚠️ 以下为记录项，不参与开发：
> - VP系统（个人习惯）
> - 工会系统
> - 结婚系统
> - 收集箱系统

---

## 十四、文档修订记录

| 版本 | 日期 | 修订内容 |
|-----|------|---------|
| v1.0 | 2026-04-15 | 初始版本 |
| v1.1 | 2026-04-15 | VP系统/工会/结婚/收集箱标记为记录项；打造系统细化为5个子模块 |
| v1.2 | 2026-04-15 | 新增技攻/攻击强化/增益量核心属性；新增模拟伤害/站街三攻；装备系统核心差异改为技攻和攻强；换装作用改为增加技攻/增益量 |
| v1.3 | 2026-04-15 | 按照游戏角色面板分组重排战斗属性；添加伤害词缀（最终伤害/白字/黄字/爆伤追加）；添加属性抗性字段；添加奶系BUFF字段；新增职业属性有效性汇总表（C物理/C魔法/奶系） |
| v1.4 | 2026-04-15 | 新增系统架构概览，区分角色系统（单角色生效）和账户系统（跨区生效）；迷雾/收集箱/冒险团系统归属账户层；更新数据依赖关系表增加作用域字段 |
| v1.5 | 2026-04-15 | 重构角色系统结构：角色系统 = 打造系统 + 进度系统；打造系统增加时装模块；进度系统 = 装备 + 贴膜(狄瑞吉) + 誓约；工会/结婚系统归属角色系统；打造+进度 = 名望+模拟伤害(增益量) |
| v1.6 | 2026-04-15 | 完善迷雾融合系统详情：增加等级/名望/技攻/增益量/四维字段；调整/+-按钮操作；移除黑色灾影（属于武器贴膜系统） |
| v1.7 | 2026-04-21 | 更新已实现系统清单；称号/宠物系统标记为已实现；更新项目结构与实际代码一致 |

---

## 十五、反馈与补充

> 请在此处补充你需要的字段或修改：

1. _______________________________________________
2. _______________________________________________
3. _______________________________________________
4. _______________________________________________
5. _______________________________________________
