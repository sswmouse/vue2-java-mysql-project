package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 卡片模板DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CardTemplateDTO {
    private Long id;
    private String templateName;
    private String templateCode;
    private String templateStyle;
    private String previewImage;
    private Boolean isDefault;
}
