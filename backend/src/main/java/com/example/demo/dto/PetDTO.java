package com.example.demo.dto;

import lombok.Data;

/**
 * 宠物DTO
 */
@Data
public class PetDTO {
    private Long id;
    private String name;
    private Integer year;
    private String type;
    private String fourStats;
    private String fourStatsAlt;
    private Double critRate;
    private Integer elementalBonus;
    private Double attackSpeed;
    private Double mpBonus;
    private String skillLevelRange;
    private Double gainBonus;
    private Double attackStrength;
    private Double whiteDamage;
    private Double extraYellow;
    private Double threeAttack;
    private Double cdReduction;
    private Integer fame;
    private String description;
    private Boolean isActive;
}