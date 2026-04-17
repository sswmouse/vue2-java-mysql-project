package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.Map;

/**
 * 装备详情DTO
 * 用于API返回单件装备详细信息
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentItemDTO {

    /**
     * 装备ID
     */
    private Long id;

    /**
     * 所属套装ID
     */
    private Long setId;

    /**
     * 所属套装名称
     */
    private String setName;

    /**
     * 部位类型：armor-防具/jewelry-首饰/special-特殊装备
     */
    private String slotType;

    /**
     * 部位名称：头肩/胸甲/腰带/下装/鞋/项链/手镯/戒指/辅助装备/魔法石/耳环
     */
    private String slotName;

    /**
     * 装备名称
     */
    private String itemName;

    /**
     * 当前品级
     */
    private String currentRarity;

    /**
     * 属性信息（根据品级）
     */
    private Map<String, Object> stats;

    /**
     * 当前品级积分
     */
    private Integer points;

    /**
     * 当前品级名望
     */
    private Integer fame;

    /**
     * 当前品级中文名
     */
    private String rarityDisplayName;

    /**
     * 排序顺序
     */
    private Integer sortOrder;

    /**
     * 可用品级列表
     */
    private String[] availableRarities;

    /**
     * 所有品级积分映射
     */
    private Map<String, Integer> rarityPointsMap;

    /**
     * 所有品级名望映射
     */
    private Map<String, Integer> rarityFameMap;
}
