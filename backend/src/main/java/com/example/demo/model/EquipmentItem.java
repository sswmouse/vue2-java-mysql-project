package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 装备详情实体类
 * 存储每套装备的11件装备详情信息
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Entity
@Table(name = "equipment_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentItem {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属套装ID
     */
    @Column(name = "set_id", nullable = false)
    private Long setId;

    /**
     * 部位类型：armor-防具/jewelry-首饰/special-特殊装备
     */
    @Column(name = "slot_type", nullable = false, length = 20)
    private String slotType;

    /**
     * 部位名称：头肩/胸甲/腰带/下装/鞋/项链/手镯/戒指/辅助装备/魔法石/耳环
     */
    @Column(name = "slot_name", nullable = false, length = 50)
    private String slotName;

    /**
     * 装备名称
     */
    @Column(name = "item_name", nullable = false, length = 100)
    private String itemName;

    /**
     * 各品级属性(JSON格式)
     * 格式: {"rare": {...}, "legendary": {...}, "epic": {...}, "mythic": {...}}
     */
    @Column(name = "rarity_stats", columnDefinition = "JSON")
    private String rarityStats;

    /**
     * 各品级积分(JSON格式)
     * 格式: {"rare": 65, "legendary": 165, "epic": 215, "mythic": 265}
     */
    @Column(name = "rarity_points", columnDefinition = "JSON")
    private String rarityPoints;

    /**
     * 各品级名望(JSON格式)
     * 格式: {"rare": 1000, "legendary": 1200, "epic": 1300, "mythic": 1400}
     */
    @Column(name = "rarity_fame", columnDefinition = "JSON")
    private String rarityFame;

    /**
     * 排序顺序
     */
    @Column(name = "sort_order")
    private Integer sortOrder;

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

    /**
     * 持久化前设置创建和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        if (sortOrder == null) {
            sortOrder = 0;
        }
    }

    /**
     * 更新前设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 部位类型常量
     */
    public static final String SLOT_TYPE_ARMOR = "armor";
    public static final String SLOT_TYPE_JEWELRY = "jewelry";
    public static final String SLOT_TYPE_SPECIAL = "special";

    /**
     * 防具部位名称常量
     */
    public static final String SLOT_ARMOR_SHOULDER = "头肩";
    public static final String SLOT_ARMOR_CHEST = "胸甲";
    public static final String SLOT_ARMOR_BELT = "腰带";
    public static final String SLOT_ARMOR_LEGS = "下装";
    public static final String SLOT_ARMOR_SHOES = "鞋";

    /**
     * 首饰部位名称常量
     */
    public static final String SLOT_JEWELRY_NECKLACE = "项链";
    public static final String SLOT_JEWELRY_BRACELET = "手镯";
    public static final String SLOT_JEWELRY_RING = "戒指";

    /**
     * 特殊装备部位名称常量
     */
    public static final String SLOT_SPECIAL_SUPPORT = "辅助装备";
    public static final String SLOT_SPECIAL_STONE = "魔法石";
    public static final String SLOT_SPECIAL_EARRING = "耳环";
}
