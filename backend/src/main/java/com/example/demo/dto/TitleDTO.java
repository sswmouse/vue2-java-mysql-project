package com.example.demo.dto;

import lombok.Data;

/**
 * 称号DTO
 */
@Data
public class TitleDTO {
    private Long id;
    private String name;
    private Integer year;
    private String type;
    private String subType;
    private Integer fourStats;
    private Integer threeAttack;
    private Double critRate;
    private Integer elementalBonus;
    private Double attackSpeed;
    private Double attackStrength;
    private String skillBonus;
    private Integer skillBonusLevel;
    private Integer fame;
    private String description;
    private Boolean isActive;
}