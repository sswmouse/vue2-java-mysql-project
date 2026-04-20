package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色装备配置实体类
 * 存储每个角色选择的装备套装配置
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@Entity
@Table(name = "character_equipment", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"character_id", "set_id"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEquipment {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 角色ID
     */
    @Column(name = "character_id", nullable = false)
    private Long characterId;

    /**
     * 当前装备套装ID
     */
    @Column(name = "set_id")
    private Long setId;

    /**
     * 当前装备套装名称
     */
    @Column(name = "set_name", length = 100)
    private String setName;

    /**
     * 套装索引（1-12）
     */
    @Column(name = "set_index")
    private Integer setIndex;

    /**
     * 是否当前穿戴（每个角色只能有一套处于穿戴状态）
     */
    @Column(name = "is_equipped")
    private Boolean isEquipped;

    /**
     * 当前积分
     */
    @Column(name = "current_points")
    private Integer currentPoints;

    /**
     * 当前阶段名称
     */
    @Column(name = "current_stage", length = 50)
    private String currentStage;

    /**
     * 装备配置JSON
     * 格式: {items: [{slotName: "头肩", rarity: "史诗"}, ...]}
     */
    @Column(name = "equipment_config", columnDefinition = "TEXT")
    private String equipmentConfig;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
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
