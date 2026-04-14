package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 角色更新DTO
 */
@Data
public class RoleUpdateDTO {

    @NotBlank(message = "角色不能为空")
    private String role;
}
