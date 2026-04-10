package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色实体类
 * 用户创建的游戏角色
 */
@Entity
@Table(name = "characters")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Character {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 所属用户ID
     */
    @Column(name = "user_id", nullable = false)
    private Long userId;

    /**
     * 角色类型ID
     */
    @Column(name = "character_type_id", nullable = false)
    private Long characterTypeId;

    /**
     * 角色名称
     */
    @Column(name = "character_name", nullable = false, length = 50)
    private String characterName;

    /**
     * 职业性质：输出/奶系
     */
    @Column(name = "job_nature", nullable = false, length = 20)
    private String jobNature;

    /**
     * 职业属性
     */
    @Column(name = "job_attribute", nullable = false, length = 50)
    private String jobAttribute;

    /**
     * 力量
     */
    @Column(name = "strength")
    private Integer strength;

    /**
     * 智力
     */
    @Column(name = "intelligence")
    private Integer intelligence;

    /**
     * 精神
     */
    @Column(name = "spirit")
    private Integer spirit;

    /**
     * 体力
     */
    @Column(name = "vitality")
    private Integer vitality;

    /**
     * 物理攻击力
     */
    @Column(name = "physical_attack")
    private Integer physicalAttack;

    /**
     * 魔法攻击力
     */
    @Column(name = "magical_attack")
    private Integer magicalAttack;

    /**
     * 独立攻击力
     */
    @Column(name = "independent_attack")
    private Integer independentAttack;

    /**
     * 光属强
     */
    @Column(name = "light_element")
    private Integer lightElement;

    /**
     * 火属强
     */
    @Column(name = "fire_element")
    private Integer fireElement;

    /**
     * 冰属强
     */
    @Column(name = "ice_element")
    private Integer iceElement;

    /**
     * 暗属强
     */
    @Column(name = "dark_element")
    private Integer darkElement;

    /**
     * 角色外观图片URL
     */
    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

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
