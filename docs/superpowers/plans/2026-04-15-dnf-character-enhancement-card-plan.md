# DNF角色打造与卡片系统实施计划

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 实现角色打造系统（增幅/附魔/称号/宠物/装备/徽章6大模块）+ 卡片系统（模板选择/生成/展示）

**Architecture:** 打造数据存储在characters表JSON字段中，通过REST API实时保存。前端打造页面采用Tab布局，实时预览打造效果。卡片系统通过模板引擎生成可视化卡片，支持多模板切换和编辑。

**Tech Stack:** Spring Boot + MySQL + Vue2 + Element UI

---

## 文件结构

```
backend/
├── src/main/java/com/example/demo/
│   ├── controller/
│   │   ├── EnhancementController.java      # 打造系统接口
│   │   └── CardController.java             # 卡片系统接口
│   ├── service/
│   │   ├── EnhancementService.java         # 打造服务接口
│   │   └── CardService.java                # 卡片服务接口
│   ├── service/impl/
│   │   ├── EnhancementServiceImpl.java     # 打造服务实现
│   │   └── CardServiceImpl.java            # 卡片服务实现
│   ├── model/
│   │   ├── CardTemplate.java               # 卡片模板实体
│   │   └── CharacterCard.java              # 角色卡片实体
│   ├── dto/
│   │   ├── AmplifyDTO.java                  # 增幅DTO
│   │   ├── EnchantDTO.java                  # 附魔DTO
│   │   ├── TitleDTO.java                    # 称号DTO
│   │   ├── PetDTO.java                      # 宠物DTO
│   │   ├── EquipmentDTO.java                # 装备DTO
│   │   ├── EmblemDTO.java                   # 徽章DTO
│   │   ├── CardTemplateDTO.java             # 卡片模板DTO
│   │   └── CharacterCardDTO.java            # 角色卡片DTO
│   └── repository/
│       ├── CardTemplateRepository.java      # 卡片模板仓库
│       └── CharacterCardRepository.java     # 角色卡片仓库

frontend/src/
├── api/
│   ├── enhancement.js                        # 打造系统API
│   └── card.js                              # 卡片系统API
├── views/
│   ├── Amplify.vue                          # 增幅页面
│   ├── Enchant.vue                          # 附魔页面
│   ├── TitlePage.vue                        # 称号页面
│   ├── PetPage.vue                          # 宠物页面
│   ├── EquipmentPage.vue                    # 装备页面
│   ├── EmblemPage.vue                       # 徽章页面
│   └── CardManager.vue                      # 卡片管理页面
└── router/
    └── index.js                             # 更新路由
```

---

## 实施阶段

### Phase 1: 数据库与后端基础

#### Task 1: 数据库表创建

**Files:**
- Create: `backend/src/main/resources/db/migration/V1__create_card_tables.sql`

- [ ] **Step 1: 创建SQL迁移文件**

```sql
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
```

- [ ] **Step 2: 验证SQL执行**

Run: `mysql -u root -p < backend/src/main/resources/db/migration/V1__create_card_tables.sql`
Expected: 执行成功，无报错

---

#### Task 2: 后端实体类创建

**Files:**
- Create: `backend/src/main/java/com/example/demo/model/CardTemplate.java`
- Create: `backend/src/main/java/com/example/demo/model/CharacterCard.java`

- [ ] **Step 1: 创建CardTemplate实体类**

```java
package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 卡片模板实体类
 */
@Entity
@Table(name = "card_templates")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 模板名称
     */
    @Column(name = "template_name", nullable = false, length = 50)
    private String templateName;

    /**
     * 模板编码
     */
    @Column(name = "template_code", nullable = false, length = 20, unique = true)
    private String templateCode;

    /**
     * 模板样式JSON
     */
    @Column(name = "template_style", columnDefinition = "TEXT")
    private String templateStyle;

    /**
     * 预览图URL
     */
    @Column(name = "preview_image")
    private String previewImage;

    /**
     * 是否默认模板
     */
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

- [ ] **Step 2: 创建CharacterCard实体类**

```java
package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色卡片实体类
 */
@Entity
@Table(name = "character_cards")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterCard {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "character_id", nullable = false)
    private Long characterId;

    /**
     * 模板ID
     */
    @Column(name = "template_id", nullable = false)
    private Long templateId;

    /**
     * 卡片名称
     */
    @Column(name = "card_name", length = 100)
    private String cardName;

    /**
     * 卡片数据快照JSON
     */
    @Column(name = "card_data", columnDefinition = "TEXT")
    private String cardData;

    /**
     * 是否设为默认展示
     */
    @Column(name = "is_default")
    private Boolean isDefault = false;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
```

- [ ] **Step 3: 创建Repository接口**

```java
// CardTemplateRepository.java
package com.example.demo.repository;

import com.example.demo.model.CardTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CardTemplateRepository extends JpaRepository<CardTemplate, Long> {
    List<CardTemplate> findAll();
    Optional<CardTemplate> findByTemplateCode(String templateCode);
    Optional<CardTemplate> findByIsDefaultTrue();
}

// CharacterCardRepository.java
package com.example.demo.repository;

import com.example.demo.model.CharacterCard;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface CharacterCardRepository extends JpaRepository<CharacterCard, Long> {
    List<CharacterCard> findByCharacterId(Long characterId);
    List<CharacterCard> findByCharacterIdAndIsDefaultTrue(Long characterId);

    @Modifying
    @Query("UPDATE CharacterCard c SET c.isDefault = false WHERE c.characterId = :characterId")
    void clearDefaultByCharacterId(@Param("characterId") Long characterId);
}
```

- [ ] **Step 4: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/model/CardTemplate.java
git add backend/src/main/java/com/example/demo/model/CharacterCard.java
git add backend/src/main/java/com/example/demo/repository/CardTemplateRepository.java
git add backend/src/main/java/com/example/demo/repository/CharacterCardRepository.java
git add backend/src/main/resources/db/migration/V1__create_card_tables.sql
git commit -m "feat: add card templates and character cards tables"
```

---

### Phase 2: DTO类创建

#### Task 3: 打造系统DTO类

**Files:**
- Create: `backend/src/main/java/com/example/demo/dto/AmplifyDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/EnchantDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/TitleDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/PetDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/EquipmentDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/EmblemDTO.java`

- [ ] **Step 1: 创建AmplifyDTO (增幅)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 增幅数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmplifyDTO {
    /** 增幅等级 */
    private Integer level;
    /** 增幅属性：力量/智力/体力/精神 */
    private String attribute;
    /** 是否强化（非增幅） */
    private Boolean isReinforce;
    /** 武器强化/增幅等级 */
    private Integer weaponLevel;
    /** 武器是否强化 */
    private Boolean isWeaponReinforce;
}
```

- [ ] **Step 2: 创建EnchantDTO (附魔)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 附魔数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnchantDTO {
    private String weapon;
    private String head;
    private String chest;
    private String legs;
    private String shoes;
    private String necklace;
    private String bracelet;
    private String ring;
    private String subEquip;
    private String magicStone;
    private String auxiliary;
    private String elementType;
    private Integer elementValue;
}
```

- [ ] **Step 3: 创建TitleDTO (称号)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 称号数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {
    private String name;
    private String year;
}
```

- [ ] **Step 4: 创建PetDTO (宠物)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 宠物数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    private String pet;
    private String petEquipRed;
    private String petEquipBlue;
    private String petEquipGreen;
}
```

- [ ] **Step 5: 创建EquipmentDTO (装备)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 装备数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private String setName;
    private Integer setIndex;
    private Integer setScore;
    private Map<String, EquipmentPiece> pieces;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentPiece {
        private String name;
        private String grade;
        private String enchant;
    }
}
```

- [ ] **Step 6: 创建EmblemDTO (徽章)**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 徽章数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmblemDTO {
    private String emblemGrade;
    private Integer starLevel;
    private Map<String, String> emblemSlots;
}
```

- [ ] **Step 7: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/dto/AmplifyDTO.java
git add backend/src/main/java/com/example/demo/dto/EnchantDTO.java
git add backend/src/main/java/com/example/demo/dto/TitleDTO.java
git add backend/src/main/java/com/example/demo/dto/PetDTO.java
git add backend/src/main/java/com/example/demo/dto/EquipmentDTO.java
git add backend/src/main/java/com/example/demo/dto/EmblemDTO.java
git commit -m "feat: add enhancement DTOs (amplify/enchant/title/pet/equipment/emblem)"
```

---

#### Task 4: 卡片系统DTO类

**Files:**
- Create: `backend/src/main/java/com/example/demo/dto/CardTemplateDTO.java`
- Create: `backend/src/main/java/com/example/demo/dto/CharacterCardDTO.java`

- [ ] **Step 1: 创建CardTemplateDTO**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 卡片模板DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTemplateDTO {
    private Long id;
    private String templateName;
    private String templateCode;
    private String templateStyle;
    private String previewImage;
    private Boolean isDefault;
}
```

- [ ] **Step 2: 创建CharacterCardDTO**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色卡片DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterCardDTO {
    private Long id;
    private Long characterId;
    private Long templateId;
    private String cardName;
    private String cardData;
    private Boolean isDefault;
    private String createdAt;
    private String updatedAt;
    // 关联数据
    private String templateName;
    private String templateCode;
    private String characterName;
}
```

- [ ] **Step 3: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/dto/CardTemplateDTO.java
git add backend/src/main/java/com/example/demo/dto/CharacterCardDTO.java
git commit -m "feat: add card DTOs (template/card)"
```

---

### Phase 3: Service层开发

#### Task 5: EnhancementService开发

**Files:**
- Create: `backend/src/main/java/com/example/demo/service/EnhancementService.java`
- Create: `backend/src/main/java/com/example/demo/service/impl/EnhancementServiceImpl.java`

- [ ] **Step 1: 创建EnhancementService接口**

```java
package com.example.demo.service;

import com.example.demo.dto.*;

/**
 * 打造系统服务接口
 */
public interface EnhancementService {

    /**
     * 获取角色全部打造数据
     */
    EnhancementDataDTO getEnhancementData(Long characterId);

    /**
     * 更新增幅数据
     */
    void updateAmplify(Long characterId, AmplifyDTO amplifyDTO);

    /**
     * 更新附魔数据
     */
    void updateEnchant(Long characterId, EnchantDTO enchantDTO);

    /**
     * 更新称号数据
     */
    void updateTitle(Long characterId, TitleDTO titleDTO);

    /**
     * 更新宠物数据
     */
    void updatePet(Long characterId, PetDTO petDTO);

    /**
     * 更新装备数据
     */
    void updateEquipment(Long characterId, EquipmentDTO equipmentDTO);

    /**
     * 更新徽章数据
     */
    void updateEmblem(Long characterId, EmblemDTO emblemDTO);
}
```

- [ ] **Step 2: 创建EnhancementDataDTO**

```java
package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色全部打造数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnhancementDataDTO {
    private AmplifyDTO amplify;
    private EnchantDTO enchant;
    private TitleDTO title;
    private PetDTO pet;
    private EquipmentDTO equipment;
    private EmblemDTO emblem;
}
```

- [ ] **Step 3: 创建EnhancementServiceImpl实现类**

```java
package com.example.demo.service.impl;

import com.example.demo.dto.*;
import com.example.demo.model.Character;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.EnhancementService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EnhancementServiceImpl implements EnhancementService {

    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    @Override
    public EnhancementDataDTO getEnhancementData(Long characterId) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));

        EnhancementDataDTO dataDTO = new EnhancementDataDTO();

        try {
            if (character.getAmplifyData() != null) {
                dataDTO.setAmplify(objectMapper.readValue(character.getAmplifyData(), AmplifyDTO.class));
            }
            if (character.getEnchantData() != null) {
                dataDTO.setEnchant(objectMapper.readValue(character.getEnchantData(), EnchantDTO.class));
            }
            if (character.getTitleData() != null) {
                dataDTO.setTitle(objectMapper.readValue(character.getTitleData(), TitleDTO.class));
            }
            if (character.getPetData() != null) {
                dataDTO.setPet(objectMapper.readValue(character.getPetData(), PetDTO.class));
            }
            if (character.getEquipmentData() != null) {
                dataDTO.setEquipment(objectMapper.readValue(character.getEquipmentData(), EquipmentDTO.class));
            }
            if (character.getEmblemData() != null) {
                dataDTO.setEmblem(objectMapper.readValue(character.getEmblemData(), EmblemDTO.class));
            }
        } catch (Exception e) {
            throw new RuntimeException("解析打造数据失败", e);
        }

        return dataDTO;
    }

    @Override
    @Transactional
    public void updateAmplify(Long characterId, AmplifyDTO amplifyDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setAmplifyData(objectMapper.writeValueAsString(amplifyDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存增幅数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEnchant(Long characterId, EnchantDTO enchantDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEnchantData(objectMapper.writeValueAsString(enchantDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存附魔数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateTitle(Long characterId, TitleDTO titleDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setTitleData(objectMapper.writeValueAsString(titleDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存称号数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updatePet(Long characterId, PetDTO petDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setPetData(objectMapper.writeValueAsString(petDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存宠物数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEquipment(Long characterId, EquipmentDTO equipmentDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEquipmentData(objectMapper.writeValueAsString(equipmentDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存装备数据失败", e);
        }
    }

    @Override
    @Transactional
    public void updateEmblem(Long characterId, EmblemDTO emblemDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        try {
            character.setEmblemData(objectMapper.writeValueAsString(emblemDTO));
            characterRepository.save(character);
        } catch (Exception e) {
            throw new RuntimeException("保存徽章数据失败", e);
        }
    }
}
```

- [ ] **Step 4: 更新Character实体类，添加新字段**

修改 `backend/src/main/java/com/example/demo/model/Character.java`，在文件末尾 `enhancementData` 字段后添加:

```java
// ============================================
// 打造系统字段（6个模块）
// ============================================

/**
 * 增幅数据JSON
 */
@Column(name = "amplify_data", columnDefinition = "TEXT")
private String amplifyData;

/**
 * 附魔数据JSON
 */
@Column(name = "enchant_data", columnDefinition = "TEXT")
private String enchantData;

/**
 * 称号数据JSON
 */
@Column(name = "title_data", columnDefinition = "TEXT")
private String titleData;

/**
 * 宠物数据JSON
 */
@Column(name = "pet_data", columnDefinition = "TEXT")
private String petData;

/**
 * 徽章数据JSON
 */
@Column(name = "emblem_data", columnDefinition = "TEXT")
private String emblemData;
```

- [ ] **Step 5: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/service/EnhancementService.java
git add backend/src/main/java/com/example/demo/service/impl/EnhancementServiceImpl.java
git add backend/src/main/java/com/example/demo/dto/EnhancementDataDTO.java
git add backend/src/main/java/com/example/demo/model/Character.java
git commit -m "feat: add enhancement service (amplify/enchant/title/pet/equipment/emblem)"
```

---

#### Task 6: CardService开发

**Files:**
- Create: `backend/src/main/java/com/example/demo/service/CardService.java`
- Create: `backend/src/main/java/com/example/demo/service/impl/CardServiceImpl.java`

- [ ] **Step 1: 创建CardService接口**

```java
package com.example.demo.service;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import java.util.List;

/**
 * 卡片系统服务接口
 */
public interface CardService {

    /**
     * 获取所有卡片模板
     */
    List<CardTemplateDTO> getAllTemplates();

    /**
     * 获取角色的所有卡片
     */
    List<CharacterCardDTO> getCharacterCards(Long characterId);

    /**
     * 创建卡片
     */
    CharacterCardDTO createCard(Long characterId, CharacterCardDTO cardDTO);

    /**
     * 更新卡片
     */
    CharacterCardDTO updateCard(Long cardId, CharacterCardDTO cardDTO);

    /**
     * 删除卡片
     */
    void deleteCard(Long cardId);

    /**
     * 设为默认卡片
     */
    void setDefaultCard(Long cardId);
}
```

- [ ] **Step 2: 创建CardServiceImpl实现类**

```java
package com.example.demo.service.impl;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import com.example.demo.model.CardTemplate;
import com.example.demo.model.Character;
import com.example.demo.model.CharacterCard;
import com.example.demo.repository.CardTemplateRepository;
import com.example.demo.repository.CardTemplateRepository;
import com.example.demo.repository.CharacterCardRepository;
import com.example.demo.repository.CharacterRepository;
import com.example.demo.service.CardService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CardServiceImpl implements CardService {

    private final CardTemplateRepository cardTemplateRepository;
    private final CharacterCardRepository characterCardRepository;
    private final CharacterRepository characterRepository;
    private final ObjectMapper objectMapper;

    @Override
    public List<CardTemplateDTO> getAllTemplates() {
        return cardTemplateRepository.findAll().stream()
                .map(this::toTemplateDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<CharacterCardDTO> getCharacterCards(Long characterId) {
        return characterCardRepository.findByCharacterId(characterId).stream()
                .map(this::toCardDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public CharacterCardDTO createCard(Long characterId, CharacterCardDTO cardDTO) {
        Character character = characterRepository.findById(characterId)
                .orElseThrow(() -> new RuntimeException("角色不存在"));
        CardTemplate template = cardTemplateRepository.findById(cardDTO.getTemplateId())
                .orElseThrow(() -> new RuntimeException("模板不存在"));

        CharacterCard card = new CharacterCard();
        card.setCharacterId(characterId);
        card.setTemplateId(cardDTO.getTemplateId());
        card.setCardName(cardDTO.getCardName());
        card.setIsDefault(cardDTO.getIsDefault() != null && cardDTO.getIsDefault());

        // 生成卡片数据快照
        try {
            String cardData = objectMapper.writeValueAsString(buildCardSnapshot(character));
            card.setCardData(cardData);
        } catch (Exception e) {
            throw new RuntimeException("生成卡片数据失败", e);
        }

        // 如果设为默认，先清除其他默认
        if (card.getIsDefault()) {
            characterCardRepository.clearDefaultByCharacterId(characterId);
        }

        CharacterCard saved = characterCardRepository.save(card);
        return toCardDTO(saved);
    }

    @Override
    @Transactional
    public CharacterCardDTO updateCard(Long cardId, CharacterCardDTO cardDTO) {
        CharacterCard card = characterCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("卡片不存在"));

        if (cardDTO.getCardName() != null) {
            card.setCardName(cardDTO.getCardName());
        }
        if (cardDTO.getTemplateId() != null) {
            card.setTemplateId(cardDTO.getTemplateId());
        }
        if (cardDTO.getIsDefault() != null && cardDTO.getIsDefault()) {
            characterCardRepository.clearDefaultByCharacterId(card.getCharacterId());
            card.setIsDefault(true);
        }

        CharacterCard saved = characterCardRepository.save(card);
        return toCardDTO(saved);
    }

    @Override
    @Transactional
    public void deleteCard(Long cardId) {
        characterCardRepository.deleteById(cardId);
    }

    @Override
    @Transactional
    public void setDefaultCard(Long cardId) {
        CharacterCard card = characterCardRepository.findById(cardId)
                .orElseThrow(() -> new RuntimeException("卡片不存在"));
        characterCardRepository.clearDefaultByCharacterId(card.getCharacterId());
        card.setIsDefault(true);
        characterCardRepository.save(card);
    }

    private CardTemplateDTO toTemplateDTO(CardTemplate template) {
        CardTemplateDTO dto = new CardTemplateDTO();
        dto.setId(template.getId());
        dto.setTemplateName(template.getTemplateName());
        dto.setTemplateCode(template.getTemplateCode());
        dto.setTemplateStyle(template.getTemplateStyle());
        dto.setPreviewImage(template.getPreviewImage());
        dto.setIsDefault(template.getIsDefault());
        return dto;
    }

    private CharacterCardDTO toCardDTO(CharacterCard card) {
        CharacterCardDTO dto = new CharacterCardDTO();
        dto.setId(card.getId());
        dto.setCharacterId(card.getCharacterId());
        dto.setTemplateId(card.getTemplateId());
        dto.setCardName(card.getCardName());
        dto.setCardData(card.getCardData());
        dto.setIsDefault(card.getIsDefault());
        dto.setCreatedAt(card.getCreatedAt() != null ? card.getCreatedAt().toString() : null);
        dto.setUpdatedAt(card.getUpdatedAt() != null ? card.getUpdatedAt().toString() : null);

        // 查询关联数据
        cardTemplateRepository.findById(card.getTemplateId()).ifPresent(template -> {
            dto.setTemplateName(template.getTemplateName());
            dto.setTemplateCode(template.getTemplateCode());
        });
        characterRepository.findById(card.getCharacterId()).ifPresent(character -> {
            dto.setCharacterName(character.getCharacterName());
        });

        return dto;
    }

    private java.util.Map<String, Object> buildCardSnapshot(Character character) {
        java.util.Map<String, Object> snapshot = new java.util.HashMap<>();
        snapshot.put("characterId", character.getId());
        snapshot.put("characterName", character.getCharacterName());
        snapshot.put("level", character.getLevel());
        snapshot.put("fameValue", character.getFameValue());
        snapshot.put("jobNature", character.getJobNature());
        snapshot.put("jobAttribute", character.getJobAttribute());
        snapshot.put("serverName", character.getServerName());
        snapshot.put("adventureName", character.getAdventureName());
        snapshot.put("avatarUrl", character.getAvatarUrl());
        // 其他字段...
        return snapshot;
    }
}
```

- [ ] **Step 3: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/service/CardService.java
git add backend/src/main/java/com/example/demo/service/impl/CardServiceImpl.java
git commit -m "feat: add card service (CRUD operations)"
```

---

### Phase 4: Controller层开发

#### Task 7: EnhancementController开发

**Files:**
- Create: `backend/src/main/java/com/example/demo/controller/EnhancementController.java`

- [ ] **Step 1: 创建EnhancementController**

```java
package com.example.demo.controller;

import com.example.demo.dto.*;
import com.example.demo.service.EnhancementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 打造系统Controller
 */
@RestController
@RequestMapping("/api/characters/{characterId}/enhancement")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class EnhancementController {

    private final EnhancementService enhancementService;

    /**
     * 获取角色全部打造数据
     */
    @GetMapping
    public ResponseEntity<EnhancementDataDTO> getEnhancementData(@PathVariable Long characterId) {
        EnhancementDataDTO data = enhancementService.getEnhancementData(characterId);
        return ResponseEntity.ok(data);
    }

    /**
     * 更新增幅数据
     */
    @PutMapping("/amplify")
    public ResponseEntity<Void> updateAmplify(
            @PathVariable Long characterId,
            @RequestBody AmplifyDTO amplifyDTO) {
        enhancementService.updateAmplify(characterId, amplifyDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新附魔数据
     */
    @PutMapping("/enchant")
    public ResponseEntity<Void> updateEnchant(
            @PathVariable Long characterId,
            @RequestBody EnchantDTO enchantDTO) {
        enhancementService.updateEnchant(characterId, enchantDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新称号数据
     */
    @PutMapping("/title")
    public ResponseEntity<Void> updateTitle(
            @PathVariable Long characterId,
            @RequestBody TitleDTO titleDTO) {
        enhancementService.updateTitle(characterId, titleDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新宠物数据
     */
    @PutMapping("/pet")
    public ResponseEntity<Void> updatePet(
            @PathVariable Long characterId,
            @RequestBody PetDTO petDTO) {
        enhancementService.updatePet(characterId, petDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新装备数据
     */
    @PutMapping("/equipment")
    public ResponseEntity<Void> updateEquipment(
            @PathVariable Long characterId,
            @RequestBody EquipmentDTO equipmentDTO) {
        enhancementService.updateEquipment(characterId, equipmentDTO);
        return ResponseEntity.ok().build();
    }

    /**
     * 更新徽章数据
     */
    @PutMapping("/emblem")
    public ResponseEntity<Void> updateEmblem(
            @PathVariable Long characterId,
            @RequestBody EmblemDTO emblemDTO) {
        enhancementService.updateEmblem(characterId, emblemDTO);
        return ResponseEntity.ok().build();
    }
}
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/controller/EnhancementController.java
git commit -m "feat: add enhancement controller (6 endpoints)"
```

---

#### Task 8: CardController开发

**Files:**
- Create: `backend/src/main/java/com/example/demo/controller/CardController.java`

- [ ] **Step 1: 创建CardController**

```java
package com.example.demo.controller;

import com.example.demo.dto.CardTemplateDTO;
import com.example.demo.dto.CharacterCardDTO;
import com.example.demo.service.CardService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

/**
 * 卡片系统Controller
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CardController {

    private final CardService cardService;

    /**
     * 获取所有卡片模板
     */
    @GetMapping("/cards/templates")
    public ResponseEntity<List<CardTemplateDTO>> getAllTemplates() {
        List<CardTemplateDTO> templates = cardService.getAllTemplates();
        return ResponseEntity.ok(templates);
    }

    /**
     * 获取角色的所有卡片
     */
    @GetMapping("/characters/{characterId}/cards")
    public ResponseEntity<List<CharacterCardDTO>> getCharacterCards(@PathVariable Long characterId) {
        List<CharacterCardDTO> cards = cardService.getCharacterCards(characterId);
        return ResponseEntity.ok(cards);
    }

    /**
     * 创建卡片
     */
    @PostMapping("/characters/{characterId}/cards")
    public ResponseEntity<CharacterCardDTO> createCard(
            @PathVariable Long characterId,
            @RequestBody CharacterCardDTO cardDTO) {
        CharacterCardDTO created = cardService.createCard(characterId, cardDTO);
        return ResponseEntity.ok(created);
    }

    /**
     * 更新卡片
     */
    @PutMapping("/cards/{cardId}")
    public ResponseEntity<CharacterCardDTO> updateCard(
            @PathVariable Long cardId,
            @RequestBody CharacterCardDTO cardDTO) {
        CharacterCardDTO updated = cardService.updateCard(cardId, cardDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * 删除卡片
     */
    @DeleteMapping("/cards/{cardId}")
    public ResponseEntity<Void> deleteCard(@PathVariable Long cardId) {
        cardService.deleteCard(cardId);
        return ResponseEntity.noContent().build();
    }

    /**
     * 设为默认卡片
     */
    @PutMapping("/cards/{cardId}/default")
    public ResponseEntity<Void> setDefaultCard(@PathVariable Long cardId) {
        cardService.setDefaultCard(cardId);
        return ResponseEntity.ok().build();
    }
}
```

- [ ] **Step 2: 提交代码**

```bash
git add backend/src/main/java/com/example/demo/controller/CardController.java
git commit -m "feat: add card controller (CRUD + default operations)"
```

---

### Phase 5: 前端API层

#### Task 9: 前端API接口

**Files:**
- Create: `frontend/src/api/enhancement.js`
- Create: `frontend/src/api/card.js`

- [ ] **Step 1: 创建enhancement.js**

```javascript
/**
 * 打造系统API
 */
export default {
    // 获取角色打造数据
    getEnhancementData: (characterId) => `/api/characters/${characterId}/enhancement`,

    // 更新增幅
    updateAmplify: (characterId) => `/api/characters/${characterId}/enhancement/amplify`,

    // 更新附魔
    updateEnchant: (characterId) => `/api/characters/${characterId}/enhancement/enchant`,

    // 更新称号
    updateTitle: (characterId) => `/api/characters/${characterId}/enhancement/title`,

    // 更新宠物
    updatePet: (characterId) => `/api/characters/${characterId}/enhancement/pet`,

    // 更新装备
    updateEquipment: (characterId) => `/api/characters/${characterId}/enhancement/equipment`,

    // 更新徽章
    updateEmblem: (characterId) => `/api/characters/${characterId}/enhancement/emblem`
}
```

- [ ] **Step 2: 创建card.js**

```javascript
/**
 * 卡片系统API
 */
export default {
    // 获取模板列表
    templates: '/api/cards/templates',

    // 获取角色卡片列表
    getCharacterCards: (characterId) => `/api/characters/${characterId}/cards`,

    // 创建卡片
    createCard: (characterId) => `/api/characters/${characterId}/cards`,

    // 更新卡片
    updateCard: (cardId) => `/api/cards/${cardId}`,

    // 删除卡片
    deleteCard: (cardId) => `/api/cards/${cardId}`,

    // 设为默认
    setDefault: (cardId) => `/api/cards/${cardId}/default`
}
```

- [ ] **Step 3: 更新api/index.js**

在 `frontend/src/api/index.js` 中添加:

```javascript
import enhancement from './enhancement'
import card from './card'

export {
    enhancement,
    card
}
```

- [ ] **Step 4: 提交代码**

```bash
git add frontend/src/api/enhancement.js
git add frontend/src/api/card.js
git add frontend/src/api/index.js
git commit -m "feat: add frontend API for enhancement and card systems"
```

---

### Phase 6: 前端打造页面

#### Task 10: 打造页面组件（通用布局）

**Files:**
- Create: `frontend/src/components/EnhancementLayout.vue`
- Modify: `frontend/src/views/Role.vue` (添加Tab路由)

- [ ] **Step 1: 创建EnhancementLayout通用布局组件**

```vue
<template>
    <div class="enhancement-layout">
        <!-- 角色信息头部 -->
        <div class="character-header">
            <div class="character-info">
                <img v-if="character.avatarUrl" :src="character.avatarUrl" class="avatar" />
                <div v-else class="avatar-placeholder">
                    <i class="el-icon-user" />
                </div>
                <div class="info-text">
                    <h2>{{ character.characterName }}</h2>
                    <p>Lv.{{ character.level }} | 名望: {{ character.fameValue }}</p>
                    <p>{{ character.jobNature }} | {{ character.jobAttribute }}</p>
                </div>
            </div>
            <el-button @click="handleBack">返回角色列表</el-button>
        </div>

        <!-- Tab导航 -->
        <el-tabs v-model="activeTab" @tab-click="handleTabClick">
            <el-tab-pane label="总览" name="overview">
                <slot name="overview" />
            </el-tab-pane>
            <el-tab-pane label="增幅" name="amplify">
                <slot name="amplify" />
            </el-tab-pane>
            <el-tab-pane label="附魔" name="enchant">
                <slot name="enchant" />
            </el-tab-pane>
            <el-tab-pane label="称号" name="title">
                <slot name="title" />
            </el-tab-pane>
            <el-tab-pane label="宠物" name="pet">
                <slot name="pet" />
            </el-tab-pane>
            <el-tab-pane label="装备" name="equipment">
                <slot name="equipment" />
            </el-tab-pane>
            <el-tab-pane label="徽章" name="emblem">
                <slot name="emblem" />
            </el-tab-pane>
            <el-tab-pane label="卡片" name="cards">
                <slot name="cards" />
            </el-tab-pane>
        </el-tabs>
    </div>
</template>

<script>
export default {
    name: 'EnhancementLayout',
    props: {
        character: {
            type: Object,
            required: true
        },
        activeTab: {
            type: String,
            default: 'overview'
        }
    },
    methods: {
        handleBack() {
            this.$router.push('/role')
        },
        handleTabClick(tab) {
            this.$emit('tab-change', tab.name)
        }
    }
}
</script>

<style lang="less" scoped>
.enhancement-layout {
    padding: 24px;

    .character-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
        padding: 20px;
        background: var(--theme-bg-card);
        border-radius: 12px;
        margin-bottom: 24px;

        .character-info {
            display: flex;
            align-items: center;
            gap: 16px;

            .avatar {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                object-fit: cover;
            }

            .avatar-placeholder {
                width: 80px;
                height: 80px;
                border-radius: 50%;
                background: var(--theme-bg-dark);
                display: flex;
                align-items: center;
                justify-content: center;
                font-size: 32px;
                color: var(--theme-text-secondary);
            }

            .info-text {
                h2 {
                    margin: 0 0 8px 0;
                    color: var(--theme-accent);
                }
                p {
                    margin: 4px 0;
                    color: var(--theme-text-secondary);
                }
            }
        }
    }
}
</style>
```

- [ ] **Step 2: 创建Amplify.vue (增幅页面)**

```vue
<template>
    <div class="amplify-page">
        <!-- 当前状态 -->
        <div class="current-status">
            <h3>当前增幅状态</h3>
            <p>增幅等级: +{{ amplifyData.level || 0 }} {{ amplifyData.attribute || '' }}</p>
            <p>武器: {{ amplifyData.isWeaponReinforce ? '强化' : '增幅' }} +{{ amplifyData.weaponLevel || 0 }}</p>
        </div>

        <!-- 增幅等级选择 -->
        <div class="form-section">
            <h4>增幅等级</h4>
            <div class="level-selector">
                <el-radio-group v-model="form.level" @change="handleChange">
                    <el-radio-button :label="10">+10</el-radio-button>
                    <el-radio-button :label="11">+11</el-radio-button>
                    <el-radio-button :label="12">+12</el-radio-button>
                    <el-radio-button :label="13">+13</el-radio-button>
                    <el-radio-button :label="14">+14</el-radio-button>
                    <el-radio-button :label="15">+15</el-radio-button>
                    <el-radio-button :label="16">+16</el-radio-button>
                    <el-radio-button :label="17">+17</el-radio-button>
                </el-radio-group>
            </div>
        </div>

        <!-- 增幅属性选择 -->
        <div class="form-section">
            <h4>增幅属性</h4>
            <el-radio-group v-model="form.attribute" @change="handleChange">
                <el-radio label="力量">力量</el-radio>
                <el-radio label="智力">智力</el-radio>
                <el-radio label="体力">体力</el-radio>
                <el-radio label="精神">精神</el-radio>
            </el-radio-group>
        </div>

        <!-- 武器强化 -->
        <div class="form-section">
            <h4>武器强化/增幅</h4>
            <el-radio-group v-model="form.isWeaponReinforce" @change="handleChange">
                <el-radio :label="true">强化</el-radio>
                <el-radio :label="false">增幅</el-radio>
            </el-radio-group>
            <el-input-number
                v-model="form.weaponLevel"
                :min="0"
                :max="40"
                @change="handleChange"
                class="weapon-level-input"
            />
        </div>

        <!-- 保存按钮 -->
        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import api from '@/api'
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'AmplifyPage',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        amplifyData: {
            type: Object,
            default: () => ({})
        }
    },
    data() {
        return {
            saving: false,
            form: {
                level: 0,
                attribute: '力量',
                isReinforce: false,
                weaponLevel: 0,
                isWeaponReinforce: true
            }
        }
    },
    watch: {
        amplifyData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true,
            deep: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updateAmplify(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
                this.$emit('saved', this.form)
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.amplify-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
            color: var(--theme-text-primary);
        }

        .weapon-level-input {
            margin-top: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 3: 创建Enchant.vue (附魔页面)**

```vue
<template>
    <div class="enchant-page">
        <div class="current-status">
            <h3>当前附魔状态</h3>
            <p>主属性: {{ enchantData.elementType || '无' }} +{{ enchantData.elementValue || 0 }}</p>
        </div>

        <!-- 属强类型和数值 -->
        <div class="form-section">
            <h4>属性强化</h4>
            <el-select v-model="form.elementType" placeholder="选择属性" @change="handleChange">
                <el-option label="光属性" value="光" />
                <el-option label="火属性" value="火" />
                <el-option label="冰属性" value="冰" />
                <el-option label="暗属性" value="暗" />
            </el-select>
            <el-input-number
                v-model="form.elementValue"
                :min="0"
                :max="999"
                @change="handleChange"
                class="element-input"
            />
        </div>

        <!-- 预设宝珠选择 -->
        <div class="form-section">
            <h4>防具附魔</h4>
            <el-form label-width="100px">
                <el-form-item label="头肩">
                    <el-select v-model="form.head" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="冲击之魂头肩宝珠" value="冲击之魂头肩宝珠" />
                        <el-option label="使徒宝珠" value="使徒宝珠" />
                    </el-select>
                </el-form-item>
                <el-form-item label="上衣/下装">
                    <el-select v-model="form.chest" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="使徒希洛克宝珠" value="使徒希洛克宝珠" />
                    </el-select>
                </el-form-item>
                <el-form-item label="鞋/腰带/肩膀">
                    <el-select v-model="form.shoes" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="格斗场冠军宝珠" value="格斗场冠军宝珠" />
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <!-- 首饰附魔 -->
        <div class="form-section">
            <h4>首饰附魔</h4>
            <el-form label-width="100px">
                <el-form-item label="项链">
                    <el-select v-model="form.necklace" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="光之主人宝珠" value="光之主人宝珠" />
                        <el-option label="火之主人宝珠" value="火之主人宝珠" />
                        <el-option label="冰之主人宝珠" value="冰之主人宝珠" />
                        <el-option label="暗之主人宝珠" value="暗之主人宝珠" />
                    </el-select>
                </el-form-item>
                <el-form-item label="手镯">
                    <el-select v-model="form.bracelet" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="光之主人宝珠" value="光之主人宝珠" />
                    </el-select>
                </el-form-item>
                <el-form-item label="戒指">
                    <el-select v-model="form.ring" placeholder="选择宝珠" @change="handleChange">
                        <el-option label="冰之主人宝珠" value="冰之主人宝珠" />
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'EnchantPage',
    props: {
        characterId: Number,
        enchantData: Object
    },
    data() {
        return {
            saving: false,
            form: {
                weapon: '',
                head: '',
                chest: '',
                legs: '',
                shoes: '',
                necklace: '',
                bracelet: '',
                ring: '',
                subEquip: '',
                magicStone: '',
                auxiliary: '',
                elementType: '冰',
                elementValue: 0
            }
        }
    },
    watch: {
        enchantData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true,
            deep: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updateEnchant(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
                this.$emit('saved', this.form)
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.enchant-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .element-input {
            margin-left: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 4: 创建TitlePage.vue (称号页面)**

```vue
<template>
    <div class="title-page">
        <div class="form-section">
            <h4>当前称号</h4>
            <el-select v-model="form.name" placeholder="选择称号" @change="handleChange" class="title-select">
                <el-option label="破坏之毁灭者" value="破坏之毁灭者" />
                <el-option label="超越极限者" value="超越极限者" />
                <el-option label="至高的神" value="至高的神" />
            </el-select>
        </div>

        <div class="form-section">
            <h4>称号年份</h4>
            <el-select v-model="form.year" placeholder="选择年份" @change="handleChange">
                <el-option label="2024金秋" value="2024金秋" />
                <el-option label="2024耕耘" value="2024耕耘" />
                <el-option label="2023金秋" value="2023金秋" />
            </el-select>
        </div>

        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'TitlePage',
    props: {
        characterId: Number,
        titleData: Object
    },
    data() {
        return {
            saving: false,
            form: {
                name: '',
                year: ''
            }
        }
    },
    watch: {
        titleData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updateTitle(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.title-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .title-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 5: 创建PetPage.vue (宠物页面)**

```vue
<template>
    <div class="pet-page">
        <div class="form-section">
            <h4>当前宠物</h4>
            <el-select v-model="form.pet" placeholder="选择宠物" @change="handleChange">
                <el-option label="迷你铁齿狗" value="迷你铁齿狗" />
                <el-option label="迷你帕拉丁" value="迷你帕拉丁" />
                <el-option label="迷你亡魂阿震" value="迷你亡魂阿震" />
            </el-select>
        </div>

        <div class="form-section">
            <h4>宠物装备</h4>
            <el-form label-width="120px">
                <el-form-item label="红色宠物装备">
                    <el-input v-model="form.petEquipRed" placeholder="属性描述" @change="handleChange" />
                </el-form-item>
                <el-form-item label="蓝色宠物装备">
                    <el-input v-model="form.petEquipBlue" placeholder="属性描述" @change="handleChange" />
                </el-form-item>
                <el-form-item label="绿色宠物装备">
                    <el-input v-model="form.petEquipGreen" placeholder="属性描述" @change="handleChange" />
                </el-form-item>
            </el-form>
        </div>

        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'PetPage',
    props: {
        characterId: Number,
        petData: Object
    },
    data() {
        return {
            saving: false,
            form: {
                pet: '',
                petEquipRed: '',
                petEquipBlue: '',
                petEquipGreen: ''
            }
        }
    },
    watch: {
        petData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updatePet(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.pet-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 6: 创建EquipmentPage.vue (装备页面)**

```vue
<template>
    <div class="equipment-page">
        <div class="form-section">
            <h4>选择装备套装</h4>
            <el-select v-model="form.setName" placeholder="选择套装" @change="handleSetChange" class="equipment-select">
                <el-option label="理想之黄金乡套" value="理想之黄金乡套" />
                <el-option label="龙战八荒套" value="龙战八荒套" />
                <el-option label="潜影暗袭套" value="潜影暗袭套" />
                <el-option label="造化自然套" value="造化自然套" />
                <el-option label="天命者的气运套" value="天命者的气运套" />
                <el-option label="究极能量套" value="究极能量套" />
                <el-option label="混沌净化套" value="混沌净化套" />
                <el-option label="诸神黄昏之女武神套" value="诸神黄昏之女武神套" />
                <el-option label="青丘灵珠套" value="青丘灵珠套" />
                <el-option label="精灵国度套" value="精灵国度套" />
                <el-option label="冥思者的领域套" value="冥思者的领域套" />
                <el-option label="群猎美学套" value="群猎美学套" />
            </el-select>
        </div>

        <div class="form-section">
            <h4>套装评分</h4>
            <el-input-number v-model="form.setScore" :min="0" :max="999" @change="handleChange" />
        </div>

        <div class="form-section">
            <h4>武器信息</h4>
            <el-form label-width="100px">
                <el-form-item label="武器名称">
                    <el-input v-model="form.weaponName" placeholder="武器名称" @change="handleChange" />
                </el-form-item>
                <el-form-item label="武器品级">
                    <el-select v-model="form.weaponGrade" @change="handleChange">
                        <el-option label="传世武器" value="传世武器" />
                        <el-option label="太初" value="太初" />
                    </el-select>
                </el-form-item>
            </el-form>
        </div>

        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'EquipmentPage',
    props: {
        characterId: Number,
        equipmentData: Object
    },
    data() {
        return {
            saving: false,
            form: {
                setName: '',
                setIndex: 0,
                setScore: 0,
                weaponName: '',
                weaponGrade: '太初'
            }
        }
    },
    watch: {
        equipmentData: {
            handler(newData) {
                if (newData) {
                    if (newData.setName) this.form.setName = newData.setName
                    if (newData.setScore) this.form.setScore = newData.setScore
                    if (newData.pieces && newData.pieces.weapon) {
                        this.form.weaponName = newData.pieces.weapon.name || ''
                        this.form.weaponGrade = newData.pieces.weapon.grade || '太初'
                    }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleSetChange() {
            const setIndexMap = {
                '理想之黄金乡套': 1,
                '龙战八荒套': 2,
                '潜影暗袭套': 3,
                '造化自然套': 4,
                '天命者的气运套': 5,
                '究极能量套': 6,
                '混沌净化套': 7,
                '诸神黄昏之女武神套': 8,
                '青丘灵珠套': 9,
                '精灵国度套': 10,
                '冥思者的领域套': 11,
                '群猎美学套': 12
            }
            this.form.setIndex = setIndexMap[this.form.setName] || 0
            this.handleChange()
        },
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                const saveData = {
                    setName: this.form.setName,
                    setIndex: this.form.setIndex,
                    setScore: this.form.setScore,
                    pieces: {
                        weapon: {
                            name: this.form.weaponName,
                            grade: this.form.weaponGrade
                        }
                    }
                }
                await this.$request({
                    url: apiEnhancement.updateEquipment(this.characterId),
                    method: 'put',
                    data: saveData
                })
                this.$message.success('保存成功')
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.equipment-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }

        .equipment-select {
            width: 300px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 7: 创建EmblemPage.vue (徽章页面)**

```vue
<template>
    <div class="emblem-page">
        <div class="form-section">
            <h4>徽章品级</h4>
            <el-select v-model="form.emblemGrade" placeholder="选择品级" @change="handleChange">
                <el-option label="稀有" value="稀有" />
                <el-option label="神器" value="神器" />
                <el-option label="传说" value="传说" />
                <el-option label="史诗" value="史诗" />
                <el-option label="神圣" value="神圣" />
            </el-select>
        </div>

        <div class="form-section">
            <h4>星徽等级</h4>
            <el-input-number v-model="form.starLevel" :min="0" :max="5" @change="handleChange" />
        </div>

        <div class="actions">
            <el-button type="primary" :loading="saving" @click="handleSave">保存更改</el-button>
        </div>
    </div>
</template>

<script>
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'EmblemPage',
    props: {
        characterId: Number,
        emblemData: Object
    },
    data() {
        return {
            saving: false,
            form: {
                emblemGrade: '神器',
                starLevel: 0,
                emblemSlots: {}
            }
        }
    },
    watch: {
        emblemData: {
            handler(newData) {
                if (newData) {
                    this.form = { ...this.form, ...newData }
                }
            },
            immediate: true
        }
    },
    methods: {
        handleChange() {
            this.$emit('update', this.form)
        },
        async handleSave() {
            this.saving = true
            try {
                await this.$request({
                    url: apiEnhancement.updateEmblem(this.characterId),
                    method: 'put',
                    data: this.form
                })
                this.$message.success('保存成功')
            } catch (error) {
                this.$message.error('保存失败')
            } finally {
                this.saving = false
            }
        }
    }
}
</script>

<style lang="less" scoped>
.emblem-page {
    padding: 20px;

    .form-section {
        margin-bottom: 24px;

        h4 {
            margin-bottom: 12px;
        }
    }

    .actions {
        margin-top: 32px;
    }
}
</style>
```

- [ ] **Step 8: 更新CharacterDetail.vue整合所有打造页面**

修改 `frontend/src/views/CharacterDetail.vue`，替换内容为:

```vue
<template>
    <div class="character-detail-page">
        <enhancement-layout :character="character" :active-tab="activeTab">
            <!-- 总览 -->
            <template #overview>
                <div class="overview-content">
                    <h3>角色打造总览</h3>
                    <el-descriptions :column="2" border>
                        <el-descriptions-item label="增幅">
                            +{{ enhancementData.amplify?.level || 0 }}
                            {{ enhancementData.amplify?.attribute || '' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="武器">
                            {{ enhancementData.amplify?.isWeaponReinforce ? '强化' : '增幅' }}
                            +{{ enhancementData.amplify?.weaponLevel || 0 }}
                        </el-descriptions-item>
                        <el-descriptions-item label="属强">
                            {{ enhancementData.enchant?.elementType || '无' }}
                            +{{ enhancementData.enchant?.elementValue || 0 }}
                        </el-descriptions-item>
                        <el-descriptions-item label="称号">
                            {{ enhancementData.title?.name || '未设置' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="宠物">
                            {{ enhancementData.pet?.pet || '未设置' }}
                        </el-descriptions-item>
                        <el-descriptions-item label="装备套装">
                            {{ enhancementData.equipment?.setName || '未设置' }}
                        </el-descriptions-item>
                    </el-descriptions>
                </div>
            </template>

            <!-- 增幅 -->
            <template #amplify>
                <amplify-page
                    :character-id="characterId"
                    :amplify-data="enhancementData.amplify || {}"
                    @saved="loadEnhancementData"
                />
            </template>

            <!-- 附魔 -->
            <template #enchant>
                <enchant-page
                    :character-id="characterId"
                    :enchant-data="enhancementData.enchant || {}"
                    @saved="loadEnhancementData"
                />
            </template>

            <!-- 称号 -->
            <template #title>
                <title-page
                    :character-id="characterId"
                    :title-data="enhancementData.title || {}"
                />
            </template>

            <!-- 宠物 -->
            <template #pet>
                <pet-page
                    :character-id="characterId"
                    :pet-data="enhancementData.pet || {}"
                />
            </template>

            <!-- 装备 -->
            <template #equipment>
                <equipment-page
                    :character-id="characterId"
                    :equipment-data="enhancementData.equipment || {}"
                />
            </template>

            <!-- 徽章 -->
            <template #emblem>
                <emblem-page
                    :character-id="characterId"
                    :emblem-data="enhancementData.emblem || {}"
                />
            </template>

            <!-- 卡片 -->
            <template #cards>
                <card-manager :character-id="characterId" :character="character" />
            </template>
        </enhancement-layout>
    </div>
</template>

<script>
import api from '@/api'
import apiEnhancement from '@/api/enhancement'
import EnhancementLayout from '@/components/EnhancementLayout.vue'
import AmplifyPage from '@/views/Amplify.vue'
import EnchantPage from '@/views/Enchant.vue'
import TitlePage from '@/views/TitlePage.vue'
import PetPage from '@/views/PetPage.vue'
import EquipmentPage from '@/views/EquipmentPage.vue'
import EmblemPage from '@/views/EmblemPage.vue'
import CardManager from '@/views/CardManager.vue'

export default {
    name: 'CharacterDetail',
    components: {
        EnhancementLayout,
        AmplifyPage,
        EnchantPage,
        TitlePage,
        PetPage,
        EquipmentPage,
        EmblemPage,
        CardManager
    },
    data() {
        return {
            characterId: null,
            character: {},
            activeTab: 'overview',
            enhancementData: {}
        }
    },
    mounted() {
        this.characterId = parseInt(this.$route.params.id)
        this.loadCharacter()
        this.loadEnhancementData()
    },
    methods: {
        async loadCharacter() {
            try {
                const data = await this.$request({
                    url: api.character.detail(this.characterId),
                    method: 'get'
                })
                this.character = data
            } catch (error) {
                console.error('加载角色失败:', error)
            }
        },
        async loadEnhancementData() {
            try {
                const data = await this.$request({
                    url: apiEnhancement.getEnhancementData(this.characterId),
                    method: 'get'
                })
                this.enhancementData = data || {}
            } catch (error) {
                console.error('加载打造数据失败:', error)
            }
        }
    }
}
</script>

<style lang="less" scoped>
.character-detail-page {
    min-height: 100%;
    background: var(--theme-bg-dark);
}
</style>
```

- [ ] **Step 9: 更新路由**

在 `frontend/src/router/index.js` 中更新CharacterDetail路由为支持子路由，或保持单页模式使用tab切换。

- [ ] **Step 10: 提交代码**

```bash
git add frontend/src/components/EnhancementLayout.vue
git add frontend/src/views/Amplify.vue
git add frontend/src/views/Enchant.vue
git add frontend/src/views/TitlePage.vue
git add frontend/src/views/PetPage.vue
git add frontend/src/views/EquipmentPage.vue
git add frontend/src/views/EmblemPage.vue
git add frontend/src/views/CharacterDetail.vue
git commit -m "feat: add 6 enhancement pages and layout"
```

---

### Phase 7: 卡片管理页面

#### Task 11: CardManager组件

**Files:**
- Create: `frontend/src/views/CardManager.vue`

- [ ] **Step 1: 创建CardManager.vue**

```vue
<template>
    <div class="card-manager">
        <!-- 工具栏 -->
        <div class="toolbar">
            <el-button type="primary" icon="el-icon-plus" @click="handleCreate">生成卡片</el-button>
        </div>

        <!-- 卡片列表 -->
        <div class="card-list">
            <el-row :gutter="20">
                <el-col
                    v-for="card in cards"
                    :key="card.id"
                    :xs="24"
                    :sm="12"
                    :md="8"
                    :lg="6"
                >
                    <div class="card-item" :class="{ 'is-default': card.isDefault }">
                        <!-- 卡片预览 -->
                        <div class="card-preview" :class="'template-' + card.templateCode">
                            <div class="card-content">
                                <div class="avatar-section">
                                    <img v-if="character.avatarUrl" :src="character.avatarUrl" class="avatar" />
                                    <div v-else class="avatar-placeholder">
                                        <i class="el-icon-user" />
                                    </div>
                                </div>
                                <h3 class="character-name">{{ character.characterName }}</h3>
                                <p class="character-info">
                                    Lv.{{ character.level }} | {{ character.jobNature }}
                                </p>
                                <div class="stats-grid">
                                    <div class="stat-item">
                                        <span class="stat-label">名望</span>
                                        <span class="stat-value">{{ character.fameValue || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">增幅</span>
                                        <span class="stat-value">+{{ enhancementData.amplify?.level || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">属强</span>
                                        <span class="stat-value">{{ enhancementData.enchant?.elementValue || 0 }}</span>
                                    </div>
                                    <div class="stat-item">
                                        <span class="stat-label">装备</span>
                                        <span class="stat-value">{{ enhancementData.equipment?.setName?.slice(0, 4) || '无' }}</span>
                                    </div>
                                </div>
                            </div>
                            <div class="template-badge">{{ card.templateName }}</div>
                        </div>

                        <!-- 操作按钮 -->
                        <div class="card-actions">
                            <span class="card-name">{{ card.cardName || '未命名' }}</span>
                            <div class="action-buttons">
                                <el-button
                                    v-if="!card.isDefault"
                                    size="mini"
                                    @click="handleSetDefault(card.id)"
                                >
                                    设为默认
                                </el-button>
                                <el-button
                                    v-if="card.isDefault"
                                    size="mini"
                                    type="success"
                                    plain
                                >
                                    默认卡片
                                </el-button>
                                <el-button
                                    size="mini"
                                    type="danger"
                                    @click="handleDelete(card.id)"
                                >
                                    删除
                                </el-button>
                            </div>
                        </div>
                    </div>
                </el-col>
            </el-row>

            <!-- 空状态 -->
            <div v-if="cards.length === 0" class="empty-state">
                <i class="el-icon-picture-outline" />
                <p>还没有生成卡片</p>
                <p>点击"生成卡片"按钮创建你的角色卡片</p>
            </div>
        </div>

        <!-- 创建卡片弹窗 -->
        <el-dialog
            title="生成卡片"
            :visible.sync="dialogVisible"
            width="600px"
        >
            <el-form label-width="100px">
                <el-form-item label="卡片名称">
                    <el-input v-model="form.cardName" placeholder="给你的卡片起个名字" />
                </el-form-item>
                <el-form-item label="选择模板">
                    <el-radio-group v-model="form.templateId">
                        <el-radio
                            v-for="template in templates"
                            :key="template.id"
                            :label="template.id"
                            class="template-option"
                        >
                            <div class="template-info">
                                <span>{{ template.templateName }}</span>
                            </div>
                        </el-radio>
                    </el-radio-group>
                </el-form-item>
            </el-form>
            <span slot="footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" :loading="saving" @click="handleSubmit">生成</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import api from '@/api'
import apiCard from '@/api/card'
import apiEnhancement from '@/api/enhancement'

export default {
    name: 'CardManager',
    props: {
        characterId: {
            type: Number,
            required: true
        },
        character: {
            type: Object,
            required: true
        }
    },
    data() {
        return {
            cards: [],
            templates: [],
            enhancementData: {},
            dialogVisible: false,
            saving: false,
            form: {
                cardName: '',
                templateId: null,
                isDefault: true
            }
        }
    },
    mounted() {
        this.loadCards()
        this.loadTemplates()
        this.loadEnhancementData()
    },
    methods: {
        async loadCards() {
            try {
                const data = await this.$request({
                    url: apiCard.getCharacterCards(this.characterId),
                    method: 'get'
                })
                this.cards = data || []
            } catch (error) {
                console.error('加载卡片失败:', error)
            }
        },
        async loadTemplates() {
            try {
                const data = await this.$request({
                    url: apiCard.templates,
                    method: 'get'
                })
                this.templates = data || []
                // 默认选中第一个模板
                if (this.templates.length > 0 && !this.form.templateId) {
                    const defaultTemplate = this.templates.find(t => t.isDefault) || this.templates[0]
                    this.form.templateId = defaultTemplate.id
                }
            } catch (error) {
                console.error('加载模板失败:', error)
            }
        },
        async loadEnhancementData() {
            try {
                const data = await this.$request({
                    url: apiEnhancement.getEnhancementData(this.characterId),
                    method: 'get'
                })
                this.enhancementData = data || {}
            } catch (error) {
                console.error('加载打造数据失败:', error)
            }
        },
        handleCreate() {
            this.dialogVisible = true
        },
        async handleSubmit() {
            if (!this.form.templateId) {
                this.$message.warning('请选择模板')
                return
            }
            this.saving = true
            try {
                await this.$request({
                    url: apiCard.createCard(this.characterId),
                    method: 'post',
                    data: this.form
                })
                this.$message.success('卡片生成成功')
                this.dialogVisible = false
                this.form.cardName = ''
                this.loadCards()
            } catch (error) {
                this.$message.error('生成失败')
            } finally {
                this.saving = false
            }
        },
        async handleSetDefault(cardId) {
            try {
                await this.$request({
                    url: apiCard.setDefault(cardId),
                    method: 'put'
                })
                this.$message.success('设置成功')
                this.loadCards()
            } catch (error) {
                this.$message.error('设置失败')
            }
        },
        async handleDelete(cardId) {
            try {
                await this.$confirm('确定要删除这张卡片吗?', '提示', {
                    type: 'warning'
                })
                await this.$request({
                    url: apiCard.deleteCard(cardId),
                    method: 'delete'
                })
                this.$message.success('删除成功')
                this.loadCards()
            } catch (error) {
                if (error !== 'cancel') {
                    this.$message.error('删除失败')
                }
            }
        }
    }
}
</script>

<style lang="less" scoped>
.card-manager {
    padding: 20px;

    .toolbar {
        margin-bottom: 24px;
    }

    .card-list {
        .card-item {
            margin-bottom: 24px;
            border-radius: 12px;
            overflow: hidden;
            background: var(--theme-bg-card);
            transition: transform 0.3s, box-shadow 0.3s;

            &:hover {
                transform: translateY(-4px);
                box-shadow: 0 8px 24px rgba(0, 0, 0, 0.3);
            }

            &.is-default {
                border: 2px solid var(--theme-accent);
            }
        }

        .card-preview {
            height: 280px;
            padding: 20px;
            background: linear-gradient(135deg, #1a1a2e, #16213e);
            color: #fff;
            position: relative;

            .card-content {
                text-align: center;
            }

            .avatar-section {
                margin-bottom: 12px;

                .avatar {
                    width: 80px;
                    height: 80px;
                    border-radius: 50%;
                    border: 3px solid rgba(255, 255, 255, 0.3);
                }

                .avatar-placeholder {
                    width: 80px;
                    height: 80px;
                    border-radius: 50%;
                    background: rgba(255, 255, 255, 0.1);
                    margin: 0 auto;
                    display: flex;
                    align-items: center;
                    justify-content: center;
                    font-size: 32px;
                }
            }

            .character-name {
                margin: 8px 0;
                font-size: 20px;
                color: #ffd700;
            }

            .character-info {
                margin: 4px 0;
                font-size: 12px;
                color: rgba(255, 255, 255, 0.7);
            }

            .stats-grid {
                display: grid;
                grid-template-columns: repeat(2, 1fr);
                gap: 8px;
                margin-top: 16px;
                padding: 12px;
                background: rgba(0, 0, 0, 0.2);
                border-radius: 8px;

                .stat-item {
                    text-align: center;

                    .stat-label {
                        display: block;
                        font-size: 11px;
                        color: rgba(255, 255, 255, 0.6);
                    }

                    .stat-value {
                        font-size: 16px;
                        font-weight: bold;
                    }
                }
            }

            .template-badge {
                position: absolute;
                top: 10px;
                right: 10px;
                padding: 4px 8px;
                background: rgba(0, 0, 0, 0.5);
                border-radius: 4px;
                font-size: 11px;
            }
        }

        .card-actions {
            padding: 12px 16px;
            display: flex;
            justify-content: space-between;
            align-items: center;

            .card-name {
                font-size: 14px;
                color: var(--theme-text-secondary);
            }

            .action-buttons {
                display: flex;
                gap: 8px;
            }
        }

        .empty-state {
            text-align: center;
            padding: 60px 20px;
            color: var(--theme-text-secondary);

            i {
                font-size: 64px;
                margin-bottom: 16px;
            }

            p {
                margin: 8px 0;
            }
        }
    }

    .template-option {
        display: block;
        margin-bottom: 12px;

        .template-info {
            display: inline-block;
            margin-left: 8px;
        }
    }
}
</style>
```

- [ ] **Step 2: 提交代码**

```bash
git add frontend/src/views/CardManager.vue
git commit -m "feat: add card manager page with templates and CRUD operations"
```

---

### Phase 8: 数据库字段补充

#### Task 12: 添加打造系统字段到数据库

**Files:**
- Modify: `backend/src/main/resources/application.properties` (无变更)
- Execute: SQL脚本

- [ ] **Step 1: 创建SQL脚本添加字段**

创建 `backend/src/main/resources/db/migration/V2__add_enhancement_fields.sql`:

```sql
-- 为characters表添加打造系统字段
ALTER TABLE characters ADD COLUMN IF NOT EXISTS amplify_data TEXT COMMENT '增幅数据JSON';
ALTER TABLE characters ADD COLUMN IF NOT EXISTS enchant_data TEXT COMMENT '附魔数据JSON';
ALTER TABLE characters ADD COLUMN IF NOT EXISTS title_data TEXT COMMENT '称号数据JSON';
ALTER TABLE characters ADD COLUMN IF NOT EXISTS pet_data TEXT COMMENT '宠物数据JSON';
ALTER TABLE characters ADD COLUMN IF NOT EXISTS emblem_data TEXT COMMENT '徽章数据JSON';
```

- [ ] **Step 2: 执行SQL**

Run: 手动在数据库中执行上述SQL

---

### Phase 9: 测试与验证

#### Task 13: 功能测试

- [ ] **Step 1: 启动后端服务**

Run: `cd backend && ./mvnw spring-boot:run` 或通过IDE启动

- [ ] **Step 2: 启动前端服务**

Run: `cd frontend && npm run serve`

- [ ] **Step 3: 测试打造接口**

```bash
# 测试获取打造数据
curl http://localhost:8080/api/characters/1/enhancement

# 测试更新增幅
curl -X PUT http://localhost:8080/api/characters/1/enhancement/amplify \
  -H "Content-Type: application/json" \
  -d '{"level": 12, "attribute": "力量", "isReinforce": false, "weaponLevel": 25, "isWeaponReinforce": true}'
```

- [ ] **Step 4: 测试卡片接口**

```bash
# 获取模板列表
curl http://localhost:8080/api/cards/templates

# 创建卡片
curl -X POST http://localhost:8080/api/characters/1/cards \
  -H "Content-Type: application/json" \
  -d '{"templateId": 1, "cardName": "我的剑影", "isDefault": true}'
```

- [ ] **Step 5: 前端功能测试**

1. 进入角色详情页
2. 切换到增幅Tab，选择增幅等级并保存
3. 切换到附魔Tab，选择属性强化
4. 切换到卡片Tab，点击生成卡片
5. 验证卡片展示

---

## 总结

本计划包含 **9个Phase，13个Task**，预计工作量：

| Phase | 内容 | 任务数 |
|-------|------|--------|
| Phase 1 | 数据库与后端基础 | 2 |
| Phase 2 | DTO类创建 | 2 |
| Phase 3 | Service层开发 | 2 |
| Phase 4 | Controller层开发 | 2 |
| Phase 5 | 前端API层 | 1 |
| Phase 6 | 前端打造页面 | 1 |
| Phase 7 | 卡片管理页面 | 1 |
| Phase 8 | 数据库字段补充 | 1 |
| Phase 9 | 测试与验证 | 1 |

**Plan complete and saved to `docs/superpowers/plans/2026-04-15-dnf-character-enhancement-card-plan.md`.**

---

**执行方式选择:**

1. **Subagent-Driven (推荐)** - 我启动独立子代理按任务执行，每完成一个任务进行review
2. **Inline Execution** - 在当前会话中按批次执行任务，配合检查点

你想用哪种方式开始执行？
