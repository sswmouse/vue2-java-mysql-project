package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import java.time.LocalDateTime;

/**
 * 装备套装列表DTO
 * 用于API返回套装列表信息
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EquipmentSetDTO {

    /**
     * 套装ID
     */
    private Long id;

    /**
     * 套装名称
     */
    private String setName;

    /**
     * 套装序号(1-12)
     */
    private Integer setIndex;

    /**
     * 套装类型：normal-普通/special-特殊
     */
    private String setType;

    /**
     * 套装描述
     */
    private String description;

    /**
     * 技攻加成%
     */
    private Float techAttackBonus;

    /**
     * 攻击强化加成
     */
    private Integer attackBonus;

    /**
     * 当前总积分
     */
    private Integer currentPoints;

    /**
     * 当前阶段名称
     */
    private String currentStage;

    /**
     * 当前阶段进度百分比
     */
    private Double stageProgress;

    /**
     * 下一阶段名称
     */
    private String nextStage;

    /**
     * 达到下一阶段所需积分
     */
    private Integer pointsToNextStage;

    /**
     * 额外名望加成
     */
    private Integer fameBonus;

    /**
     * 已选择装备数量
     */
    private Integer selectedItemCount;

    /**
     * 总装备数量
     */
    private Integer totalItemCount;

    /**
     * 创建时间
     */
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    private LocalDateTime updatedAt;
}
