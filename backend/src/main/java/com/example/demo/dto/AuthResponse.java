package com.example.demo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {

    private String token;

    private Long userId;

    private String username;

    private String phone;

    private String role;

    public AuthResponse(String token, UserDTO userDTO) {
        this.token = token;
        this.userId = userDTO.getId();
        this.username = userDTO.getUsername();
        this.phone = userDTO.getPhone();
        this.role = userDTO.getRole();
    }
}
