package com.example.demo.service;

/**
 * 认证服务接口
 */
public interface AuthService {

    /**
     * 用户登录
     * @param username 用户名
     * @param password 密码
     * @return JWT Token
     */
    String login(String username, String password);

    /**
     * 用户注册
     * @param username 用户名
     * @param phone 手机号
     * @param password 密码
     * @return 注册的用户ID
     */
    Long register(String username, String phone, String password);

    /**
     * 更新用户角色
     * @param userId 用户ID
     * @param role 新角色
     */
    void updateUserRole(Long userId, String role);
}
