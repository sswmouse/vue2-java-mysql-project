package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 增幅选项实体类
 * 存储增幅等级和增幅属性选项数据
 */
@Entity
@Table(name = "amplify_options")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AmplifyOption {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 增幅等级：+10/+11/+12/+13/+14/+15/+16/+17
     */
    @Column(name = "level", nullable = false, length = 10)
    private String level;

    /**
     * 增幅类型：增幅/强化
     */
    @Column(name = "amplify_type", nullable = false, length = 20)
    private String amplifyType;

    /**
     * 增幅属性：智力/力量/体力/精神
     */
    @Column(name = "attribute", length = 50)
    private String attribute;

    /**
     * 属性值
     */
    @Column(name = "attribute_value")
    private Integer attributeValue;

    /**
     * 备注说明
     */
    @Column(name = "remark", columnDefinition = "TEXT")
    private String remark;

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
