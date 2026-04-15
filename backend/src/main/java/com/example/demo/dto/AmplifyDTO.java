package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 增幅数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmplifyDTO {
    private Integer level;
    private String attribute;
    private Boolean isReinforce;
    private Integer weaponLevel;
    private Boolean isWeaponReinforce;
}
