package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色全部打造数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnhancementDataDTO {
    private AmplifyDTO amplify;
    private EnchantDTO enchant;
    private TitleDTO title;
    private PetDTO pet;
    private EquipmentDTO equipment;
    private EmblemDTO emblem;
}
