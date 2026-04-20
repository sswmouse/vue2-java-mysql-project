/**
 * @Description: 附魔系统API接口
 * @Author: Claude Code
 * @Date: 2026-04-20
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
 * 礼包类型列表
 */
export const PACKAGE_TYPES = ['金秋套', '春节套', '金秋', '春节', '其他']

/**
 * API接口定义
 */
export default {
    // 角色附魔配置
    characterEnchant: (characterId) => `/api/character-enchant/${characterId}`,
    characterEnchantByPart: (characterId, part) => `/api/character-enchant/${characterId}/${encodeURIComponent(part)}`,
    characterEnchantBatch: (characterId) => `/api/character-enchant/${characterId}/batch`,

    // 附魔选项（复用已有接口）
    enchantOptions: '/api/enchant-options',
    enchantOptionsById: (id) => `/api/enchant-options/${id}`,
    enchantOptionsByPart: (part) => `/api/enchant-options/part/${encodeURIComponent(part)}`,
    enchantOptionsGrouped: '/api/enchant-options/grouped',
    enchantOptionsParts: '/api/enchant-options/parts'
}
