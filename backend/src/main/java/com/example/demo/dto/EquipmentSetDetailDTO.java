package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

/**
 * 装备套装详情DTO
 * 用于API返回套装详细信息，包含套装信息和装备列表
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentSetDetailDTO {

    /**
     * 套装基本信息
     */
    private SetBasicInfo setInfo;

    /**
     * 装备列表（按部位分类）
     */
    private ItemsInfo items;

    /**
     * 阶段效果列表
     */
    private List<StageEffect> effects;

    /**
     * 当前积分信息
     */
    private PointsInfo pointsInfo;

    /**
     * 套装基本信息内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class SetBasicInfo {
        private Long id;
        private String setName;
        private Integer setIndex;
        private String setType;
        private String description;
        private Float techAttackBonus;
        private Integer attackBonus;
    }

    /**
     * 装备列表信息内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemsInfo {
        /**
         * 防具装备列表
         */
        private List<EquipmentItemDTO> armorItems;

        /**
         * 首饰装备列表
         */
        private List<EquipmentItemDTO> jewelryItems;

        /**
         * 特殊装备列表
         */
        private List<EquipmentItemDTO> specialItems;
    }

    /**
     * 阶段效果信息内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class StageEffect {
        private String stage;
        private Integer stageOrder;
        private Integer threshold;
        private Integer fameBonus;
        private Integer cumulativeFame;
        private String stageType;
        private Boolean isActivated;
    }

    /**
     * 积分信息内部类
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class PointsInfo {
        private Integer totalPoints;
        private String currentStage;
        private String nextStage;
        private Double progress;
        private Integer fameBonus;
    }
}
