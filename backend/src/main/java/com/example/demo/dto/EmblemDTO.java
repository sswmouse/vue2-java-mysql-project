package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import java.util.Map;

/**
 * 徽章数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmblemDTO {
    private String emblemGrade;
    private Integer starLevel;
    private Map<String, String> emblemSlots;
}
