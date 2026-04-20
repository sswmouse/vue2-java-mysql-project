package com.example.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.nio.charset.StandardCharsets;
import java.util.List;

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

                // 映射前端静态资源 - Vue dist目录
                String frontendDistPath = new java.io.File("../frontend/dist/").getAbsolutePath();
                // 使用绝对路径，确保路径格式正确
                String cssPath = "file:" + frontendDistPath + "/css/";
                String jsPath = "file:" + frontendDistPath + "/js/";
                String imgPath = "file:" + frontendDistPath + "/img/";
                String fontsPath = "file:" + frontendDistPath + "/fonts/";
                String faviconPath = "file:" + frontendDistPath + "/";
                String indexPath = "file:" + frontendDistPath + "/";

                registry.addResourceHandler("/css/**").addResourceLocations(cssPath);
                registry.addResourceHandler("/js/**").addResourceLocations(jsPath);
                registry.addResourceHandler("/img/**").addResourceLocations(imgPath);
                registry.addResourceHandler("/fonts/**").addResourceLocations(fontsPath);
                registry.addResourceHandler("/favicon.ico").addResourceLocations(faviconPath);
                // index.html 作为默认文件
                registry.addResourceHandler("/index.html").addResourceLocations(indexPath + "index.html");
            }

            @Override
            public void extendMessageConverters(List<HttpMessageConverter<?>> converters) {
                // 配置JSON转换器使用UTF-8编码
                for (HttpMessageConverter<?> converter : converters) {
                    if (converter instanceof MappingJackson2HttpMessageConverter) {
                        MappingJackson2HttpMessageConverter jacksonConverter = (MappingJackson2HttpMessageConverter) converter;
                        jacksonConverter.setObjectMapper(objectMapper());
                        jacksonConverter.setSupportedMediaTypes(List.of(
                                new org.springframework.http.MediaType("application", "json", StandardCharsets.UTF_8)
                        ));
                    }
                }
            }
        };
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        // 保持驼峰命名策略
        mapper.setPropertyNamingStrategy(PropertyNamingStrategy.LOWER_CAMEL_CASE);
        return mapper;
    }
}