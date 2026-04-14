package com.example.demo.controller;

import com.example.demo.config.JwtAuthFilter;
import com.example.demo.dto.*;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.service.AuthService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * 认证控制器
 * 处理登录、注册、角色更新等请求
 */
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;
    private final UserRepository userRepository;

    public AuthController(AuthService authService, UserRepository userRepository) {
        this.authService = authService;
        this.userRepository = userRepository;
    }

    /**
     * 用户登录
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@Validated @RequestBody LoginDTO loginDTO) {
        try {
            String token = authService.login(loginDTO.getUsername(), loginDTO.getPassword());

            // 获取用户信息
            User user = userRepository.findByUsername(loginDTO.getUsername())
                    .orElseThrow(() -> new RuntimeException("用户不存在"));

            UserDTO userDTO = new UserDTO();
            userDTO.setId(user.getId());
            userDTO.setUsername(user.getUsername());
            userDTO.setPhone(user.getPhone());
            userDTO.setRole(user.getRole());

            return ResponseEntity.ok(new AuthResponse(token, userDTO));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 用户注册
     */
    @PostMapping("/register")
    public ResponseEntity<?> register(@Validated @RequestBody RegisterDTO registerDTO) {
        try {
            Long userId = authService.register(
                    registerDTO.getUsername(),
                    registerDTO.getPhone(),
                    registerDTO.getPassword()
            );

            Map<String, Object> response = new HashMap<>();
            response.put("message", "注册成功");
            response.put("userId", userId);

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("未登录"));
        }

        JwtAuthFilter.UserPrincipal principal = (JwtAuthFilter.UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setUsername(user.getUsername());
        userDTO.setPhone(user.getPhone());
        userDTO.setRole(user.getRole());

        return ResponseEntity.ok(userDTO);
    }

    /**
     * 获取用户权限树
     * 返回用户的菜单权限和路由权限
     */
    @GetMapping("/permissions")
    public ResponseEntity<?> getPermissions(Authentication authentication) {
        if (authentication == null || authentication.getPrincipal() == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(createErrorResponse("未登录"));
        }

        JwtAuthFilter.UserPrincipal principal = (JwtAuthFilter.UserPrincipal) authentication.getPrincipal();
        User user = userRepository.findById(principal.getUserId())
                .orElseThrow(() -> new RuntimeException("用户不存在"));

        Map<String, Object> permissions = new HashMap<>();

        // 设置用户基本信息
        permissions.put("userId", user.getId());
        permissions.put("username", user.getUsername());
        permissions.put("role", user.getRole());

        // 菜单权限列表
        List<String> menuPermissions = new ArrayList<>();

        // 所有用户都有的菜单
        menuPermissions.add("menu:home");
        menuPermissions.add("menu:role");
        menuPermissions.add("menu:equipment");
        menuPermissions.add("menu:fashion");
        menuPermissions.add("menu:enhancement");
        menuPermissions.add("menu:enchantment");
        menuPermissions.add("menu:badge");
        menuPermissions.add("menu:title");
        menuPermissions.add("menu:pet");
        menuPermissions.add("menu:mist");

        // 管理员专属菜单
        if ("admin".equals(user.getRole())) {
            menuPermissions.add("menu:users");
        }

        permissions.put("menuPermissions", menuPermissions);

        // 菜单树结构
        List<Map<String, Object>> menuTree = buildMenuTree(user.getRole());
        permissions.put("menuTree", menuTree);

        // 路由权限配置
        Map<String, Object> routePermissions = new HashMap<>();
        routePermissions.put("canAccessUsers", "admin".equals(user.getRole()));
        routePermissions.put("canAccessRole", true);
        routePermissions.put("canAccessEquipment", true);
        routePermissions.put("canAccessFashion", true);
        routePermissions.put("canAccessEnhancement", true);
        routePermissions.put("canAccessEnchantment", true);
        routePermissions.put("canAccessBadge", true);
        routePermissions.put("canAccessTitle", true);
        routePermissions.put("canAccessPet", true);
        routePermissions.put("canAccessMist", true);

        permissions.put("routes", routePermissions);

        return ResponseEntity.ok(permissions);
    }

    /**
     * 构建菜单树
     */
    private List<Map<String, Object>> buildMenuTree(String role) {
        List<Map<String, Object>> menuTree = new ArrayList<>();

        // 首页
        menuTree.add(createMenuItem("/", "首页", "el-icon-s-home", "menu:home"));

        // 用户管理（仅管理员）
        if ("admin".equals(role)) {
            menuTree.add(createMenuItem("/users", "用户管理", "el-icon-user", "menu:users"));
        }

        // 角色管理
        menuTree.add(createMenuItem("/role", "角色管理", "el-icon-s-custom", "menu:role"));

        // 装备管理
        menuTree.add(createMenuItem("/equipment", "装备管理", "el-icon-suitcase", "menu:equipment"));

        // 时装管理
        menuTree.add(createMenuItem("/fashion", "时装管理", "el-icon-shopping-bag-2", "menu:fashion"));

        // 强化系统
        menuTree.add(createMenuItem("/enhancement", "强化系统", "el-icon-data-line", "menu:enhancement"));

        // 附魔系统
        menuTree.add(createMenuItem("/enchantment", "附魔系统", "el-icon-magic-stick", "menu:enchantment"));

        // 徽章系统
        menuTree.add(createMenuItem("/badge", "徽章系统", "el-icon-medal", "menu:badge"));

        // 称号系统
        menuTree.add(createMenuItem("/title", "称号系统", "el-icon-postcard", "menu:title"));

        // 宠物系统
        menuTree.add(createMenuItem("/pet", "宠物系统", "el-icon-cpu", "menu:pet"));

        // 迷雾大陆
        menuTree.add(createMenuItem("/mist", "迷雾大陆", "el-icon-cloudy", "menu:mist"));

        return menuTree;
    }

    /**
     * 创建菜单项
     */
    private Map<String, Object> createMenuItem(String path, String title, String icon, String permission) {
        Map<String, Object> menu = new HashMap<>();
        menu.put("path", path);
        menu.put("title", title);
        menu.put("icon", icon);
        menu.put("permission", permission);
        return menu;
    }

    /**
     * 更新用户角色（仅管理员）
     */
    @PutMapping("/users/{id}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable Long id,
            @Validated @RequestBody RoleUpdateDTO roleUpdateDTO) {
        try {
            authService.updateUserRole(id, roleUpdateDTO.getRole());

            Map<String, String> response = new HashMap<>();
            response.put("message", "角色更新成功");

            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(createErrorResponse(e.getMessage()));
        }
    }

    /**
     * 创建错误响应
     */
    private Map<String, String> createErrorResponse(String message) {
        Map<String, String> response = new HashMap<>();
        response.put("error", message);
        return response;
    }
}
