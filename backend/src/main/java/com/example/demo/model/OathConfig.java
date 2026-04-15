package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 誓约配置实体类
 * 存储誓约系统的三种模式配置
 */
@Entity
@Table(name = "oath_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OathConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 誓约模式
     */
    @Column(name = "oath_mode", nullable = false, length = 20)
    private String oathMode;

    /**
     * 誓约名称
     */
    @Column(name = "oath_name", nullable = false, length = 50)
    private String oathName;

    /**
     * 誓约属性描述
     */
    @Column(name = "oath_attribute", nullable = false, length = 100)
    private String oathAttribute;

    /**
     * 模式效果描述
     */
    @Column(name = "description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
