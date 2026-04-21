/**
 * @Description: 增幅系统API接口
 * @Author: Claude Code
 * @Date: 2026-04-21
 */

/**
 * 12个装备部位列表
 */
export const EQUIPMENT_PARTS = [
    '武器', '头肩', '上衣', '下装',
    '鞋', '腰带', '项链', '手镯',
    '戒指', '辅助装备', '魔法石', '耳环'
]

/**
 * 增幅类型列表
 */
export const AMPLIFY_TYPES = ['增幅', '强化']

/**
 * 增幅名望加成表
 * 增幅等级 -> { 武器名望, 防具/首饰/特殊装备名望 }
 */
export const FAME_TABLE = {
    1:  { weapon: 13,  other: 5  },
    2:  { weapon: 26,  other: 9  },
    3:  { weapon: 38,  other: 13 },
    4:  { weapon: 51,  other: 17 },
    5:  { weapon: 64,  other: 21 },
    6:  { weapon: 76,  other: 25 },
    7:  { weapon: 89,  other: 29 },
    8:  { weapon: 152, other: 49 },
    9:  { weapon: 171, other: 55 },
    10: { weapon: 240, other: 77 },
    11: { weapon: 472, other: 152 },
    12: { weapon: 757, other: 243 },
    13: { weapon: 820, other: 263 },
    14: { weapon: 883, other: 284 },
    15: { weapon: 947,  other: 304 },
    16: { weapon: 1011, other: 325 },
    17: { weapon: 1076, other: 346 },
    18: { weapon: 1141, other: 367 },
    19: { weapon: 1206, other: 388 },
    20: { weapon: 1272, other: 410 }
}

/**
 * 根据增幅等级和部位类型计算名望
 * @param {number} level - 增幅等级 (1-15)
 * @param {string} part - 部位名称
 * @returns {number} 名望值
 */
export function calculateFame(level, part) {
    if (!level || level < 1) return 0
    const fameData = FAME_TABLE[level]
    if (!fameData) return 0

    // 武器名望单独计算
    if (part === '武器') {
        return fameData.weapon
    }
    // 其他装备使用同一套名望
    return fameData.other
}

/**
 * 计算增幅总名望
 * @param {Array} amplifyList - 增幅配置列表
 * @returns {number} 总名望
 */
export function calculateTotalFame(amplifyList) {
    if (!amplifyList || amplifyList.length === 0) return 0
    return amplifyList.reduce((total, item) => {
        return total + calculateFame(item.amplifyLevel, item.equipmentPart)
    }, 0)
}

/**
 * API接口定义
 */
export default {
    // 角色增幅配置
    characterAmplify: (characterId) => `/api/character-amplify/${characterId}`,
    characterAmplifyByPart: (characterId, part) => `/api/character-amplify/${characterId}/${encodeURIComponent(part)}`,
    characterAmplifyBatch: (characterId) => `/api/character-amplify/${characterId}/batch`
}
