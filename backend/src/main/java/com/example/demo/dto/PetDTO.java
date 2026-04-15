package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 宠物数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PetDTO {
    private String pet;
    private String petEquipRed;
    private String petEquipBlue;
    private String petEquipGreen;
}
