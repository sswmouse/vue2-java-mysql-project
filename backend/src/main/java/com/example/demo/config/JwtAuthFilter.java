package com.example.demo.config;

import com.example.demo.utils.JwtUtil;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * JWT认证过滤器
 * 验证请求中的JWT Token
 */
@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // 获取请求头中的Authorization
        String authHeader = request.getHeader("Authorization");

        // 如果没有Token或不是Bearer Token，继续过滤链
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);

        try {
            // 验证Token是否过期
            if (JwtUtil.isTokenExpired(token)) {
                filterChain.doFilter(request, response);
                return;
            }

            // 解析Token获取用户信息
            String username = JwtUtil.getUsername(token);
            Long userId = JwtUtil.getUserId(token);
            String role = JwtUtil.getRole(token);

            // 创建认证对象
            UserPrincipal principal = new UserPrincipal(userId, username, role);
            UsernamePasswordAuthenticationToken authentication =
                    new UsernamePasswordAuthenticationToken(
                            principal,
                            null,
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.toUpperCase()))
                    );

            // 设置认证信息到SecurityContext
            SecurityContextHolder.getContext().setAuthentication(authentication);

        } catch (Exception e) {
            // Token解析失败，继续过滤链（后续Spring Security会处理）
            logger.error("JWT Token解析失败: " + e.getMessage());
        }

        filterChain.doFilter(request, response);
    }

    /**
     * 用户主体信息类
     */
    public static class UserPrincipal {
        private final Long userId;
        private final String username;
        private final String role;

        public UserPrincipal(Long userId, String username, String role) {
            this.userId = userId;
            this.username = username;
            this.role = role;
        }

        public Long getUserId() {
            return userId;
        }

        public String getUsername() {
            return username;
        }

        public String getRole() {
            return role;
        }
    }
}
