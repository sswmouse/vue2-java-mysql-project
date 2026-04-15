package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Spring Security配置
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;

    public SecurityConfig(JwtAuthFilter jwtAuthFilter,
                          CustomAuthenticationEntryPoint authenticationEntryPoint) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationEntryPoint = authenticationEntryPoint;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // 禁用CSRF
                .csrf().disable()
                // 配置CORS
                .cors().and()
                // 设置Session为无状态
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                // 配置请求授权
                .authorizeRequests()
                // 允许登录和注册接口公开访问
                .antMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                // 允许OPTIONS请求（CORS预检）
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                // 允许上传接口公开访问
                .antMatchers("/upload/**").permitAll()
                // 允许静态资源
                .antMatchers("/uploads/**").permitAll()
                // 允许health检查
                .antMatchers("/health").permitAll()
                // 允许迷雾系统API公开访问
                .antMatchers("/api/fogs/**").permitAll()
                // 允许装备套装API公开访问
                .antMatchers("/api/equipment-sets/**").permitAll()
                // 允许誓约配置API公开访问
                .antMatchers("/api/oath-configs/**").permitAll()
                // 允许角色API公开访问
                .antMatchers("/api/characters/**").permitAll()
                // 允许角色类型API公开访问
                .antMatchers("/api/character-types/**").permitAll()
                // 允许角色同步API公开访问
                .antMatchers("/api/character-sync/**").permitAll()
                // 允许访问前端静态资源
                .antMatchers("/*.html", "/css/**", "/js/**", "/images/**", "/favicon.ico").permitAll()
                // 修改用户角色需要管理员权限
                .antMatchers(HttpMethod.PUT, "/auth/users/*/role").hasRole("ADMIN")
                // 其他请求需要认证
                .anyRequest().authenticated()
                .and()
                // 配置认证失败处理
                .exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint)
                .and()
                // 添加JWT过滤器
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        // 禁用Spring Security默认的登录页面
        http.httpBasic().disable();
        http.formLogin().disable();

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(java.util.Collections.singletonList("*"));
        configuration.setAllowedMethods(java.util.Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(java.util.Collections.singletonList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
