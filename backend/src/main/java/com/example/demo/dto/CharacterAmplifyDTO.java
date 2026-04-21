package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 角色增幅配置数据传输对象
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-21
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterAmplifyDTO {

    private Long id;

    private Long characterId;

    /**
     * 装备部位
     */
    private String equipmentPart;

    /**
     * 增幅类型：增幅/强化
     */
    private String amplifyType;

    /**
     * 增幅等级
     */
    private Integer amplifyLevel;

    /**
     * 名望值
     */
    private Integer fame;
}
