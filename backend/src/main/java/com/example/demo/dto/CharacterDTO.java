package com.example.demo.dto;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * 角色DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CharacterDTO {

    private Long id;

    private Long userId;

    private Long characterTypeId;

    /**
     * 角色类型信息（用于前端展示）
     */
    private CharacterTypeDTO characterType;

    private String characterName;

    private String avatarUrl;

    // ============================================
    // 名望和基础信息（可选）
    // ============================================
    private Integer fameValue;
    private String serverName;
    private Integer level;
    private String adventureName;

    /**
     * 最高属强（用于展示）
     */
    private Integer maxElement;

    // ============================================
    // 四维属性（可选）
    // ============================================
    private Integer strength;
    private Integer intelligence;
    private Integer spirit;
    private Integer vitality;

    // ============================================
    // 攻击属性（可选）
    // ============================================
    private Integer physicalAttack;
    private Integer magicalAttack;
    private Integer independentAttack;

    // ============================================
    // 属强（可选）
    // ============================================
    private Integer lightElement;
    private Integer fireElement;
    private Integer iceElement;
    private Integer darkElement;

    // ============================================
    // 职业信息（可选）
    // ============================================
    private String jobNature;
    private String jobAttribute;

    // ============================================
    // 装备概要
    // ============================================
    private String equipmentSummary;
    private String lastSyncTime;
    private String syncStatus;
    private String syncErrorMsg;

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
