package com.example.demo.service.impl;

import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import com.example.demo.utils.JwtUtil;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 认证服务实现
 */
@Service
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public String login(String username, String password) {
        // 查询用户
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户名或密码错误"));

        // 验证密码
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("用户名或密码错误");
        }

        // 生成JWT Token
        return JwtUtil.generateToken(user.getId(), user.getUsername(), user.getRole());
    }

    @Override
    @Transactional
    public Long register(String username, String phone, String password) {
        // 检查用户名是否存在
        if (userRepository.existsByUsername(username)) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查手机号是否存在
        if (userRepository.existsByPhone(phone)) {
            throw new RuntimeException("手机号已被注册");
        }

        // 创建新用户
        User user = new User();
        user.setUsername(username);
        user.setPhone(phone);
        // 使用BCrypt加密密码
        user.setPassword(passwordEncoder.encode(password));
        // 默认角色为user
        user.setRole("user");

        User savedUser = userRepository.save(user);
        return savedUser.getId();
    }

    @Override
    @Transactional
    public void updateUserRole(Long userId, String role) {
        // 验证角色值
        if (!role.equals("admin") && !role.equals("user")) {
            throw new RuntimeException("无效的角色值，只能是admin或user");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        user.setRole(role);
        userRepository.save(user);
    }
}
