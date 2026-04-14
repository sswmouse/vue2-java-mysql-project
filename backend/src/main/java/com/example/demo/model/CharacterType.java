/*
 * @Author: yerun sswmouse@163.com
 * @Date: 2026-04-10 22:22:03
 * @LastEditors: yerun sswmouse@163.com
 * @LastEditTime: 2026-04-14 22:46:35
 * @FilePath: /vue2-java-mysql-project/backend/src/main/java/com/example/demo/model/CharacterType.java
 * @Description: 
 */
package com.example.demo.model;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

/**
 * 角色类型实体类
 * 对应游戏中的所有角色类型
 */
@Entity
@Table(name = "character_types")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 性别：男/女
     */
    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    /**
     * 职业分类：鬼剑士/魔法师/格斗家/神枪手/圣职者/暗夜使者/守护者/魔枪士/枪剑士
     */
    @Column(name = "class_name", nullable = false, length = 50)
    private String className;

    /**
     * 具体职业：剑魂/元素/散打等
     */
    @Column(name = "job_name", nullable = false, length = 50)
    private String jobName;

    /**
     * 职业性质：输出/奶系
     */
    @Column(name = "job_nature", nullable = false, length = 20)
    private String jobNature;

    /**
     * 职业属性：
     * 输出：物理百分比/魔法百分比/物理固伤/魔法固伤
     * 奶系：智力/精神/体力
     */
    @Column(name = "job_attribute", nullable = false, length = 50)
    private String jobAttribute;

    /**
     * 是否特殊职业（如驱魔需要手动选择职业属性）
     */
    @Column(name = "special_case", nullable = false)
    private Boolean specialCase;

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
