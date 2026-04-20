package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.util.List;

/**
 * 角色装备配置DTO
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-18
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEquipmentDTO {

    /**
     * 套装ID
     */
    private Long setId;

    /**
     * 套装名称
     */
    private String setName;

    /**
     * 套装索引（1-12）
     */
    private Integer setIndex;

    /**
     * 是否当前穿戴
     */
    private Boolean isEquipped;

    /**
     * 当前积分
     */
    private Integer currentPoints;

    /**
     * 当前阶段
     */
    private String currentStage;

    /**
     * 装备配置列表
     */
    private List<ItemConfig> items;

    /**
     * 单件装备配置
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ItemConfig {
        /**
         * 部位名称
         */
        private String slotName;

        /**
         * 部位ID（前端使用）
         */
        private Integer itemId;

        /**
         * 品级
         */
        private String rarity;
    }
}
