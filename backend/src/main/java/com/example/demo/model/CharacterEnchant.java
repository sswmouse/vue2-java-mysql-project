package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色附魔配置实体类
 * 存储每个角色选择的装备附魔配置
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-20
 */
@Entity
@Table(name = "character_enchant", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"character_id", "equipment_part"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEnchant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "character_id", nullable = false)
    private Long characterId;

    /**
     * 装备部位：武器/头肩/上衣/下装/鞋/腰带/项链/手镯/戒指/辅助装备/魔法石/耳环
     */
    @Column(name = "equipment_part", nullable = false, length = 20)
    private String equipmentPart;

    /**
     * 附魔选项ID
     */
    @Column(name = "enchant_option_id")
    private Long enchantOptionId;

    /**
     * 附魔名称（冗余存储）
     */
    @Column(name = "enchant_name", length = 100)
    private String enchantName;

    /**
     * 属性值
     */
    @Column(name = "attribute_value", length = 200)
    private String attributeValue;

    /**
     * 礼包类型
     */
    @Column(name = "package_type", length = 50)
    private String packageType;

    /**
     * 名望值
     */
    @Column(name = "fame")
    private Integer fame;

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
