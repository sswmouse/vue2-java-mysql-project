package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 附魔宝珠选项实体类
 * 存储所有可用的附魔宝珠选项数据
 */
@Entity
@Table(name = "enchant_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnchantOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 装备部位：头肩/上衣/下装/鞋/腰带/项链/手镯/戒指/辅助装备/魔法石/耳环/武器
     */
    @Column(name = "equipment_part", nullable = false, length = 20)
    private String equipmentPart;

    /**
     * 宝珠名称
     */
    @Column(name = "name", nullable = false, length = 100)
    private String name;

    /**
     * 所属礼包类型：春节宝珠/国庆宝珠/其他附魔
     */
    @Column(name = "package_type", length = 50)
    private String packageType;

    /**
     * 属性值（如 +30, +25, +8% 等，支持多条，用逗号分隔）
     */
    @Column(name = "attribute_value", length = 200)
    private String attributeValue;

    /**
     * 排序顺序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

    /**
     * 名望值
     */
    @Column(name = "fame")
    private Integer fame;

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
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
