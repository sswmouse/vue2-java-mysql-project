package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 装备数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquipmentDTO {
    private String setName;
    private Integer setIndex;
    private Integer setScore;
    private Map<String, EquipmentPiece> pieces;

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EquipmentPiece {
        private String name;
        private String grade;
        private String enchant;
    }
}
