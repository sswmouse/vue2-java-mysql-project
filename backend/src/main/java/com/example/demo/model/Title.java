package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 称号实体类
 */
@Data
@Entity
@Table(name = "title")
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 称号名称 */
    @Column(nullable = false, length = 100)
    private String name;

    /** 年份 */
    @Column(nullable = false)
    private Integer year;

    /** 称号类型：普通/至尊/耕耘/金秋/春节 */
    @Column(nullable = false, length = 50)
    private String type;

    /** 子类型 */
    @Column(length = 50)
    private String subType;

    /** 四维 */
    @Column
    private Integer fourStats = 0;

    /** 三攻 */
    @Column
    private Integer threeAttack = 0;

    /** 暴击率(%) */
    @Column(precision = 5, scale = 2)
    private Double critRate = 0.0;

    /** 属强 */
    @Column
    private Integer elementalBonus = 0;

    /** 三速(%) */
    @Column(precision = 5, scale = 2)
    private Double attackSpeed = 0.0;

    /** 攻击强化(%) */
    @Column(precision = 5, scale = 2)
    private Double attackStrength = 0.0;

    /** 技能加成描述 */
    @Column(length = 200)
    private String skillBonus;

    /** 技能加成等级 */
    @Column
    private Integer skillBonusLevel;

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