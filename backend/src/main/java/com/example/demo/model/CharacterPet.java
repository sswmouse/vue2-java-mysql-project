package com.example.demo.model;

import lombok.Data;
import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色宠物关联实体类
 */
@Data
@Entity
@Table(name = "character_pet")
public class CharacterPet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** 角色ID */
    @Column(nullable = false)
    private Long characterId;

    /** 宠物ID */
    @Column(nullable = false)
    private Long petId;

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