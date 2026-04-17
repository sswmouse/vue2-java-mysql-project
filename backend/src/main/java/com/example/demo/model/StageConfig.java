package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 套装阶段配置实体类
 * 存储21个阶段配置信息，包括触发积分和额外名望
 *
 * @author Claude Code
 * @version 1.0
 * @since 2026-04-17
 */
@Entity
@Table(name = "stage_configs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StageConfig {

    /**
     * 主键ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 阶段名称：稀有1-5/神器1-5/传说1-5/史诗1-5/太初
     */
    @Column(name = "stage", nullable = false, length = 20)
    private String stage;

    /**
     * 阶段顺序(1-21)
     */
    @Column(name = "stage_order", nullable = false)
    private Integer stageOrder;

    /**
     * 触发积分阈值
     */
    @Column(name = "threshold", nullable = false)
    private Integer threshold;

    /**
     * 阶段额外名望
     */
    @Column(name = "fame_bonus", nullable = false)
    private Integer fameBonus;

    /**
     * 累计名望
     */
    @Column(name = "cumulative_fame", nullable = false)
    private Integer cumulativeFame;

    /**
     * 阶段类型：minor-小阶段/major-大阶段
     */
    @Column(name = "stage_type", length = 20)
    private String stageType;

    /**
     * 创建时间
     */
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /**
     * 更新时间
     */
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * 持久化前设置创建和更新时间
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * 更新前设置更新时间
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    /**
     * 阶段类型常量
     */
    public static final String STAGE_TYPE_MINOR = "minor";
    public static final String STAGE_TYPE_MAJOR = "major";

    /**
     * 品级常量
     */
    public static final String STAGE_RARE = "稀有";
    public static final String STAGE_LEGENDARY = "神器";
    public static final String STAGE_EPIC = "传说";
    public static final String STAGE_MYTHIC = "史诗";
    public static final String STAGE_PRIMAL = "太初";

    /**
     * 获取品级名称（去掉数字后缀）
     */
    public String getRarity() {
        if (stage == null) return null;
        if (stage.startsWith(STAGE_RARE)) return STAGE_RARE;
        if (stage.startsWith(STAGE_LEGENDARY)) return STAGE_LEGENDARY;
        if (stage.startsWith(STAGE_EPIC)) return STAGE_EPIC;
        if (stage.startsWith(STAGE_MYTHIC)) return STAGE_MYTHIC;
        if (stage.startsWith(STAGE_PRIMAL)) return STAGE_PRIMAL;
        return stage;
    }

    /**
     * 获取小阶段序号（1-5）
     */
    public Integer getSubStage() {
        if (stage == null) return null;
        if (stage.length() > 2) {
            try {
                return Integer.parseInt(stage.substring(stage.length() - 1));
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    /**
     * 判断是否为最后一个阶段（太初）
     */
    public boolean isLastStage() {
        return STAGE_PRIMAL.equals(stage);
    }
}
