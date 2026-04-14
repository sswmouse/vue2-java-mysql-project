package com.example.demo.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
public class UserDTO {

    private Long id;

    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String username;

    @NotBlank(message = "手机号不能为空")
    private String phone;

    @Size(min = 6, message = "密码长度不能小于6位")
    private String password;

    private String role = "user";

    // 头像URL
    private String avatar;

    // 主题偏好
    private String theme = "dnf";
}