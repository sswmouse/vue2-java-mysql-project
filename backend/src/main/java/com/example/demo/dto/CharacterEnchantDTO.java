package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

/**
 * 角色附魔配置数据传输对象
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-20
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CharacterEnchantDTO {

    private Long id;

    private Long characterId;

    /**
     * 装备部位
     */
    private String equipmentPart;

    /**
     * 附魔选项ID
     */
    private Long enchantOptionId;

    /**
     * 附魔名称
     */
    private String enchantName;

    /**
     * 属性类型
     */
    private String attributeType;

    /**
     * 属性值
     */
    private String attributeValue;

    /**
     * 礼包类型
     */
    private String packageType;
}
