package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 角色DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTO {

    private Long id;

    @NotNull(message = "用户ID不能为空")
    private Long userId;

    @NotNull(message = "角色类型ID不能为空")
    private Long characterTypeId;

    /**
     * 角色类型信息（用于前端展示）
     */
    private CharacterTypeDTO characterType;

    @NotBlank(message = "角色名称不能为空")
    private String characterName;

    @NotBlank(message = "职业性质不能为空")
    private String jobNature;

    @NotBlank(message = "职业属性不能为空")
    private String jobAttribute;

    private Integer strength;
    private Integer intelligence;
    private Integer spirit;
    private Integer vitality;

    private Integer physicalAttack;
    private Integer magicalAttack;
    private Integer independentAttack;

    private Integer lightElement;
    private Integer fireElement;
    private Integer iceElement;
    private Integer darkElement;

    /**
     * 最高属强（用于展示）
     */
    private Integer maxElement;

    /**
     * 名望值
     */
    private Integer fameValue;

    /**
     * 区服名称
     */
    private String serverName;

    /**
     * 角色等级
     */
    private Integer level;

    /**
     * 冒险团名称
     */
    private String adventureName;

    /**
     * 装备概要JSON
     */
    private String equipmentSummary;

    /**
     * 最后同步时间
     */
    private String lastSyncTime;

    /**
     * 同步状态
     */
    private String syncStatus;

    /**
     * 同步错误信息
     */
    private String syncErrorMsg;

    private String avatarUrl;

    // ============================================
    // 核心输出属性（技攻/攻击强化）
    // ============================================
    private Float techniqueAttack;
    private Integer attackBonus;
    private Integer buffBonus;

    // ============================================
    // 模拟伤害和站街三攻
    // ============================================
    private Integer simulateDamage;
    private Integer statAttack;

    // ============================================
    // 伤害词缀
    // ============================================
    private Float finalDamage;
    private Float whiteCritDamage;
    private Float yellowCritDamage;
    private Float critDamageBonus;
    private Integer skillLvBonus;
    private Float skillAttackBonus;

    // ============================================
    // 属性抗性
    // ============================================
    private Integer lightResistance;
    private Integer fireResistance;
    private Integer iceResistance;
    private Integer darkResistance;

    // ============================================
    // 奶系BUFF字段
    // ============================================
    private Integer buffLevel;
    private Integer buffStrength;
    private Integer buffIntelligence;

    // ============================================
    // HP/MP
    // ============================================
    private Integer hpMax;
    private Integer mpMax;

    // ============================================
    // 防御
    // ============================================
    private Integer physicalDefense;
    private Integer magicalDefense;

    // ============================================
    // 暴击/命中/回避
    // ============================================
    private Integer critRate;
    private Integer hitRate;
    private Integer avoidance;

    // ============================================
    // 三速
    // ============================================
    private Integer attackSpeed;
    private Integer castingSpeed;
    private Integer moveSpeed;

    // ============================================
    // 恢复量
    // ============================================
    private Integer hpRecovery;
    private Integer mpRecovery;

    // ============================================
    // 物理/魔法暴击率
    // ============================================
    private Integer physicalCritRate;
    private Integer magicalCritRate;

    // ============================================
    // 装备和誓约系统
    // ============================================
    private String equipmentData;
    private String oathMode;
    private String oathAttribute;
    private String enhancementData;
}
