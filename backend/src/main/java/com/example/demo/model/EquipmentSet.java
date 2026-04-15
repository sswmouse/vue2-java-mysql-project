package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 装备套装实体类
 * 存储12套装备的基本信息
 */
@Entity
@Table(name = "equipment_sets")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentSet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 套装名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 套装序号（1-12）
     */
    @Column(name = "index_num", nullable = false)
    private Integer indexNum;

    /**
     * 套装类型：normal-普通/special-特殊
     */
    @Column(name = "set_type", length = 20)
    private String setType;

    /**
     * 套装描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    /**
     * 技攻加成%
     */
    @Column(name = "tech_attack_bonus")
    private Float techAttackBonus;

    /**
     * 攻击强化
     */
    @Column(name = "attack_bonus")
    private Integer attackBonus;

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
