package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 积分计算请求/响应DTO
 * 用于计算套装积分和阶段的请求和响应
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
public class CalculatePointsDTO {

    /**
     * 请求DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Request {

        /**
         * 套装ID
         */
        @NotNull(message = "套装ID不能为空")
        private Long setId;

        /**
         * 装备列表
         */
        @NotNull(message = "装备列表不能为空")
        private List<ItemSelection> items;
    }

    /**
     * 装备选择DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemSelection {

        /**
         * 装备ID
         */
        private Long itemId;

        /**
         * 部位名称
         */
        private String slotName;

        /**
         * 选择的品级
         */
        private String rarity;
    }

    /**
     * 响应DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class Response {

        /**
         * 套装ID
         */
        private Long setId;

        /**
         * 套装名称
         */
        private String setName;

        /**
         * 总积分
         */
        private Integer totalPoints;

        /**
         * 当前阶段名称
         */
        private String currentStage;

        /**
         * 下一阶段名称
         */
        private String nextStage;

        /**
         * 当前阶段进度百分比
         */
        private Double progress;

        /**
         * 额外名望加成
         */
        private Integer fameBonus;

        /**
         * 累计名望
         */
        private Integer cumulativeFame;

        /**
         * 阶段效果列表（已激活的效果）
         */
        private List<ActivatedEffect> activatedEffects;

        /**
         * 装备选择详情
         */
        private List<ItemDetail> itemDetails;
    }

    /**
     * 已激活效果DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ActivatedEffect {

        /**
         * 阶段名称
         */
        private String stage;

        /**
         * 阶段顺序
         */
        private Integer stageOrder;

        /**
         * 触发积分阈值
         */
        private Integer threshold;

        /**
         * 额外名望
         */
        private Integer fameBonus;

        /**
         * 累计名望
         */
        private Integer cumulativeFame;
    }

    /**
     * 装备详情DTO
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemDetail {

        /**
         * 装备ID
         */
        private Long itemId;

        /**
         * 装备名称
         */
        private String itemName;

        /**
         * 部位名称
         */
        private String slotName;

        /**
         * 部位类型
         */
        private String slotType;

        /**
         * 选择品级
         */
        private String rarity;

        /**
         * 积分
         */
        private Integer points;

        /**
         * 名望
         */
        private Integer fame;
    }
}
