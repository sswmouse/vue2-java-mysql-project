package com.example.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // CORS配置已移至SecurityConfig
                // 此处仅保留资源处理器配置
            }

            @Override
            public void addResourceHandlers(ResourceHandlerRegistry registry) {
                // 映射上传目录为静态资源 (classpath)
                registry.addResourceHandler("/uploads/**")
                        .addResourceLocations("classpath:/static/uploads/");
            }
        };
    }
}