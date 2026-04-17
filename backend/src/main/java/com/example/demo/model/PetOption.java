package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 宠物选项实体类
 * 存储所有可用的宠物选项数据
 */
@Entity
@Table(name = "pet_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 宠物名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 所属年份/赛季
     */
    @Column(name = "season", nullable = false, length = 50)
    private String season;

    /**
     * 宠物类型：神宠/灵宠/祥宠
     */
    @Column(name = "pet_type", length = 20)
    private String petType;

    /**
     * 属性描述
     */
    @Column(name = "attributes", length = 200)
    private String attributes;

    /**
     * 是否有专属技能
     */
    @Column(name = "has_skill")
    private Boolean hasSkill;

    /**
     * 技能描述
     */
    @Column(name = "skill_description", columnDefinition = "TEXT")
    private String skillDescription;

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
        if (hasSkill == null) hasSkill = false;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
