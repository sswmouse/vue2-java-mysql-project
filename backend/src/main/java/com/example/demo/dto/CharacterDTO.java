package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 角色DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTO {

    private Long id;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "角色类型ID不能为空")
    private Long characterTypeId;

    /**
     * 角色类型信息（用于前端展示）
     */
    private CharacterTypeDTO characterType;

    @NotBlank(message = "角色名称不能为空")
    private String characterName;

    @NotBlank(message = "职业性质不能为空")
    private String jobNature;

    @NotBlank(message = "职业属性不能为空")
    private String jobAttribute;

    private Integer strength;
    private Integer intelligence;
    private Integer spirit;
    private Integer vitality;

    private Integer physicalAttack;
    private Integer magicalAttack;
    private Integer independentAttack;

    private Integer lightElement;
    private Integer fireElement;
    private Integer iceElement;
    private Integer darkElement;

    /**
     * 最高属强（用于展示）
     */
    private Integer maxElement;

    private String avatarUrl;
}
