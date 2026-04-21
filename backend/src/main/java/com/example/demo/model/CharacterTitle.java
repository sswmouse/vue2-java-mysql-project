package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色称号关联实体类
 */
@Data
@Entity
@Table(name = "character_title")
public class CharacterTitle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 角色ID */
    @Column(nullable = false)
    private Long characterId;

    /** 称号ID */
    @Column(nullable = false)
    private Long titleId;

    /** 是否已穿戴 */
    @Column
    private Boolean isEquipped = true;

    /** 创建时间 */
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}