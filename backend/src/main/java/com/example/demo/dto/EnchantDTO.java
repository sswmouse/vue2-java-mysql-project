package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 附魔数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EnchantDTO {
    private String weapon;
    private String head;
    private String chest;
    private String legs;
    private String shoes;
    private String necklace;
    private String bracelet;
    private String ring;
    private String subEquip;
    private String magicStone;
    private String auxiliary;
    private String elementType;
    private Integer elementValue;
}
