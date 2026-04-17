package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 称号选项实体类
 * 存储所有可用的称号选项数据
 */
@Entity
@Table(name = "title_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 称号名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 所属年份/赛季：2026/2025金秋/2025春节/2025耕耘/2024金秋/2024春节/2023金秋
     */
    @Column(name = "season", nullable = false, length = 50)
    private String season;

    /**
     * 称号类型：输出/奶系
     */
    @Column(name = "title_type", length = 20)
    private String titleType;

    /**
     * 主要属性描述
     */
    @Column(name = "main_attributes", length = 200)
    private String mainAttributes;

    /**
     * 是否有技能攻击力
     */
    @Column(name = "has_technique_attack")
    private Boolean hasTechniqueAttack;

    /**
     * 技攻数值
     */
    @Column(name = "technique_attack_value")
    private Integer techniqueAttackValue;

    /**
     * 排序顺序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 是否为最新赛季
     */
    @Column(name = "is_latest")
    private Boolean isLatest;

    /**
     * 备注说明
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (sortOrder == null) sortOrder = 0;
        if (isLatest == null) isLatest = false;
        if (hasTechniqueAttack == null) hasTechniqueAttack = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
