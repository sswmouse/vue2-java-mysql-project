package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 宠物实体类
 */
@Data
@Entity
@Table(name = "pet")
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 宠物名称 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 年份 */
    @Column(nullable = false)
    private Integer year;

    /** 宠物类型：普通/至尊/耕耘 */
    @Column(nullable = false, length = 50)
    private String type;

    /** 四维 */
    @Column(length = 50)
    private String fourStats;

    /** 四维备选 */
    @Column(length = 50)
    private String fourStatsAlt;

    /** 暴击率(%) */
    @Column(precision = 5, scale = 2)
    private Double critRate = 0.0;

    /** 属强 */
    @Column
    private Integer elementalBonus = 0;

    /** 三速(%) */
    @Column(precision = 5, scale = 2)
    private Double attackSpeed = 0.0;

    /** MP加成(%) */
    @Column(precision = 5, scale = 2)
    private Double mpBonus = 0.0;

    /** 技能等级范围 */
    @Column(length = 50)
    private String skillLevelRange;

    /** 增益量(%) */
    @Column(precision = 5, scale = 2)
    private Double gainBonus = 0.0;

    /** 攻击强化(%) */
    @Column(precision = 5, scale = 2)
    private Double attackStrength = 0.0;

    /** 白字伤害(%) */
    @Column(precision = 5, scale = 2)
    private Double whiteDamage = 0.0;

    /** 额外黄字(%) */
    @Column(precision = 5, scale = 2)
    private Double extraYellow = 0.0;

    /** 三攻(%) */
    @Column(precision = 5, scale = 2)
    private Double threeAttack = 0.0;

    /** CD缩减(%) */
    @Column(precision = 5, scale = 2)
    private Double cdReduction = 0.0;

    /** 名望 */
    @Column
    private Integer fame = 0;

    /** 额外描述 */
    @Column(length = 500)
    private String description;

    /** 是否启用 */
    @Column
    private Boolean isActive = true;

    /** 创建时间 */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    /** 更新时间 */
    @Column
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