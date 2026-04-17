package com.example.demo.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

/**
 * 数据库配置类
 * 确保MySQL连接使用UTF-8编码
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Configuration
public class DatabaseConfig {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${spring.datasource.driver-class-name}")
    private String driverClassName;

    /**
     * 创建自定义HikariCP数据源
     * 在连接初始化时执行SET NAMES utf8mb4
     */
    @Bean
    @Primary
    public DataSource dataSource() {
        HikariConfig config = new HikariConfig();
        // 重新构建JDBC URL，添加正确的字符集参数
        StringBuilder urlBuilder = new StringBuilder(jdbcUrl);
        if (!jdbcUrl.contains("character_set_client")) {
            urlBuilder.append("&character_set_client=utf8mb4");
        }
        if (!jdbcUrl.contains("character_set_connection")) {
            urlBuilder.append("&character_set_connection=utf8mb4");
        }
        if (!jdbcUrl.contains("character_set_results")) {
            urlBuilder.append("&character_set_results=utf8mb4");
        }

        config.setJdbcUrl(urlBuilder.toString());
        config.setUsername(username);
        config.setPassword(password);
        config.setDriverClassName(driverClassName);
        config.setMaximumPoolSize(5);
        config.setMinimumIdle(1);
        config.setConnectionTimeout(30000);
        config.setIdleTimeout(600000);
        config.setMaxLifetime(1800000);

        // 设置连接属性，确保UTF-8
        Properties dsProperties = new Properties();
        dsProperties.setProperty("characterEncoding", "UTF-8");
        dsProperties.setProperty("characterSetResults", "UTF-8");
        dsProperties.setProperty("useUnicode", "true");
        dsProperties.setProperty("connectionCollation", "utf8mb4_unicode_ci");
        config.setDataSourceProperties(dsProperties);

        // 设置连接初始化SQL，确保每次建立连接时都执行SET NAMES utf8mb4
        config.setConnectionInitSql("SET NAMES utf8mb4");

        HikariDataSource dataSource = new HikariDataSource(config);

        // 测试连接并打印字符集状态
        try (Connection conn = dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                stmt.execute("SET NAMES utf8mb4");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to initialize connection with UTF-8", e);
        }

        return dataSource;
    }
}
