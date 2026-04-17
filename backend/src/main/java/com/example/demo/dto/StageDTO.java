package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 阶段配置DTO
 * 用于API返回阶段配置信息
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageDTO {

    /**
     * 阶段ID
     */
    private Long id;

    /**
     * 阶段名称：稀有1-5/神器1-5/传说1-5/史诗1-5/太初
     */
    private String stage;

    /**
     * 阶段顺序(1-21)
     */
    private Integer stageOrder;

    /**
     * 触发积分阈值
     */
    private Integer threshold;

    /**
     * 阶段额外名望
     */
    private Integer fameBonus;

    /**
     * 累计名望
     */
    private Integer cumulativeFame;

    /**
     * 阶段类型：minor-小阶段/major-大阶段
     */
    private String stageType;

    /**
     * 品级（用于前端分组显示）
     */
    private String rarity;

    /**
     * 小阶段序号（1-5），太初返回null
     */
    private Integer subStage;

    /**
     * 是否为最后一个阶段
     */
    private Boolean isLastStage;
}
