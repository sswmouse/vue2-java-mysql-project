package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色增幅配置实体类
 * 存储每个角色选择的装备增幅/强化配置
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-21
 */
@Entity
@Table(name = "character_amplify", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"character_id", "equipment_part"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterAmplify {

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
     * 增幅类型：增幅/强化
     */
    @Column(name = "amplify_type", nullable = false, length = 10)
    private String amplifyType;

    /**
     * 增幅等级 (1-15)
     */
    @Column(name = "amplify_level", nullable = false)
    private Integer amplifyLevel;

    /**
     * 名望值（根据等级和部位自动计算）
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
