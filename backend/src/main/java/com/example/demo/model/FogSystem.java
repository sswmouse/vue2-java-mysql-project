package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 迷雾融合系统实体类
 * 跨区生效，每个用户在每个跨区有独立的迷雾数据
 */
@Entity
@Table(name = "fog_systems", uniqueConstraints = {
    @UniqueConstraint(columnNames = {"user_id", "region"})
})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FogSystem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 跨区名称
     * 跨一大区、跨二大区、跨三大区A、跨三大区B、跨四大区、跨五大区、跨六大区、跨七
     */
    @Column(name = "region", nullable = false, length = 50)
    private String region;

    /**
     * 迷雾融合等级 (1-999)
     */
    @Column(name = "fog_level")
    private Integer fogLevel = 1;

    /**
     * 当前经验值
     */
    @Column(name = "current_exp")
    private Integer currentExp = 0;

    /**
     * 升级所需经验值
     */
    @Column(name = "max_exp")
    private Integer maxExp = 10000;

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
