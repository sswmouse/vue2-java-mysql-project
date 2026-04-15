package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色卡片DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterCardDTO {
    private Long id;
    private Long characterId;
    private Long templateId;
    private String cardName;
    private String cardData;
    private Boolean isDefault;
    private String createdAt;
    private String updatedAt;
    private String templateName;
    private String templateCode;
    private String characterName;
}
