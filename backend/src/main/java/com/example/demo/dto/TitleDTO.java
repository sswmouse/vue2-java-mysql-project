package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 称号数据DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TitleDTO {
    private String name;
    private String year;
}
