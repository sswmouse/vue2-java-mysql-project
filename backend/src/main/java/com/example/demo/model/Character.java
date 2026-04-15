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
     * 名望值
     */
    @Column(name = "fame_value")
    private Integer fameValue;

    /**
     * 区服名称
     */
    @Column(name = "server_name", length = 50)
    private String serverName;

    /**
     * 角色等级
     */
    @Column(name = "level")
    private Integer level;

    /**
     * 冒险团名称
     */
    @Column(name = "adventure_name", length = 50)
    private String adventureName;

    /**
     * 装备概要JSON
     */
    @Column(name = "equipment_summary", columnDefinition = "TEXT")
    private String equipmentSummary;

    /**
     * 最后同步时间
     */
    @Column(name = "last_sync_time")
    private LocalDateTime lastSyncTime;

    /**
     * 同步状态
     */
    @Column(name = "sync_status", length = 20)
    private String syncStatus;

    /**
     * 同步错误信息
     */
    @Column(name = "sync_error_msg", columnDefinition = "TEXT")
    private String syncErrorMsg;

    /**
     * 角色外观图片URL
     */
    @Column(name = "avatar_url", columnDefinition = "TEXT")
    private String avatarUrl;

    // ============================================
    // 核心输出属性（技攻/攻击强化）
    // ============================================

    /**
     * 技能攻击力（技攻%）
     */
    @Column(name = "technique_attack")
    private Float techniqueAttack;

    /**
     * 攻击强化
     */
    @Column(name = "attack_bonus")
    private Integer attackBonus;

    /**
     * 增益量（奶系BUFF三攻加成）
     */
    @Column(name = "buff_bonus")
    private Integer buffBonus;

    // ============================================
    // 模拟伤害和站街三攻
    // ============================================

    /**
     * 模拟伤害（修炼场沙袋）
     */
    @Column(name = "simulate_damage")
    private Integer simulateDamage;

    /**
     * 站街三攻（奶系BUFF加成量）
     */
    @Column(name = "stat_attack")
    private Integer statAttack;

    // ============================================
    // 伤害词缀
    // ============================================

    /**
     * 最终伤害
     */
    @Column(name = "final_damage")
    private Float finalDamage;

    /**
     * 白字附加伤害%
     */
    @Column(name = "white_crit_damage")
    private Float whiteCritDamage;

    /**
     * 黄字追加伤害%
     */
    @Column(name = "yellow_crit_damage")
    private Float yellowCritDamage;

    /**
     * 爆伤追加%
     */
    @Column(name = "crit_damage_bonus")
    private Float critDamageBonus;

    /**
     * 技攻Lv加成
     */
    @Column(name = "skill_lv_bonus")
    private Integer skillLvBonus;

    /**
     * 技攻角色攻击力（特定技能攻击力提升）
     */
    @Column(name = "skill_attack_bonus")
    private Float skillAttackBonus;

    // ============================================
    // 属性抗性
    // ============================================

    /**
     * 光属性抗性
     */
    @Column(name = "light_resistance")
    private Integer lightResistance;

    /**
     * 火属性抗性
     */
    @Column(name = "fire_resistance")
    private Integer fireResistance;

    /**
     * 冰属性抗性
     */
    @Column(name = "ice_resistance")
    private Integer iceResistance;

    /**
     * 暗属性抗性
     */
    @Column(name = "dark_resistance")
    private Integer darkResistance;

    // ============================================
    // 奶系BUFF字段
    // ============================================

    /**
     * BUFF换装等级
     */
    @Column(name = "buff_level")
    private Integer buffLevel;

    /**
     * BUFF增加的力量/体力
     */
    @Column(name = "buff_strength")
    private Integer buffStrength;

    /**
     * BUFF增加的智力/精神
     */
    @Column(name = "buff_intelligence")
    private Integer buffIntelligence;

    // ============================================
    // HP/MP
    // ============================================

    /**
     * HP最大值
     */
    @Column(name = "hp_max")
    private Integer hpMax;

    /**
     * MP最大值
     */
    @Column(name = "mp_max")
    private Integer mpMax;

    // ============================================
    // 防御
    // ============================================

    /**
     * 物理防御力
     */
    @Column(name = "physical_defense")
    private Integer physicalDefense;

    /**
     * 魔法防御力
     */
    @Column(name = "magical_defense")
    private Integer magicalDefense;

    // ============================================
    // 暴击/命中/回避
    // ============================================

    /**
     * 暴击率%
     */
    @Column(name = "crit_rate")
    private Integer critRate;

    /**
     * 命中率%
     */
    @Column(name = "hit_rate")
    private Integer hitRate;

    /**
     * 回避率%
     */
    @Column(name = "avoidance")
    private Integer avoidance;

    // ============================================
    // 三速
    // ============================================

    /**
     * 攻击速度%
     */
    @Column(name = "attack_speed")
    private Integer attackSpeed;

    /**
     * 施放速度%
     */
    @Column(name = "casting_speed")
    private Integer castingSpeed;

    /**
     * 移动速度%
     */
    @Column(name = "move_speed")
    private Integer moveSpeed;

    // ============================================
    // 恢复量
    // ============================================

    /**
     * HP恢复量
     */
    @Column(name = "hp_recovery")
    private Integer hpRecovery;

    /**
     * MP恢复量
     */
    @Column(name = "mp_recovery")
    private Integer mpRecovery;

    // ============================================
    // 最高属强（计算字段）
    // ============================================

    /**
     * 最高属强
     */
    @Column(name = "max_element")
    private Integer maxElement;

    // ============================================
    // 物理/魔法暴击率
    // ============================================

    /**
     * 物理暴击率%
     */
    @Column(name = "physical_crit_rate")
    private Integer physicalCritRate;

    /**
     * 魔法暴击率%
     */
    @Column(name = "magical_crit_rate")
    private Integer magicalCritRate;

    // ============================================
    // 装备和誓约系统
    // ============================================

    /**
     * 装备信息JSON（12套装备+部位装备信息）
     */
    @Column(name = "equipment_data", columnDefinition = "TEXT")
    private String equipmentData;

    /**
     * 誓约模式：BASIC/MODE1/MODE2
     */
    @Column(name = "oath_mode", length = 20)
    private String oathMode;

    /**
     * 当前激活的誓约属性
     */
    @Column(name = "oath_attribute", length = 100)
    private String oathAttribute;

    /**
     * 打造信息JSON（增幅/附魔/称号/宠物/换装）
     */
    @Column(name = "enhancement_data", columnDefinition = "TEXT")
    private String enhancementData;

    // ============================================
    // 打造系统字段（6个模块独立JSON）
    // ============================================

    /**
     * 增幅数据JSON
     */
    @Column(name = "amplify_data", columnDefinition = "TEXT")
    private String amplifyData;

    /**
     * 附魔数据JSON
     */
    @Column(name = "enchant_data", columnDefinition = "TEXT")
    private String enchantData;

    /**
     * 称号数据JSON
     */
    @Column(name = "title_data", columnDefinition = "TEXT")
    private String titleData;

    /**
     * 宠物数据JSON
     */
    @Column(name = "pet_data", columnDefinition = "TEXT")
    private String petData;

    /**
     * 徽章数据JSON
     */
    @Column(name = "emblem_data", columnDefinition = "TEXT")
    private String emblemData;

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
