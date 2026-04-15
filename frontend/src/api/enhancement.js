/**
 * 打造系统API
 */

export default {
    // 获取角色打造数据
    getEnhancementData: (characterId) => `/api/characters/${characterId}/enhancement`,

    // 更新增幅
    updateAmplify: (characterId) => `/api/characters/${characterId}/enhancement/amplify`,

    // 更新附魔
    updateEnchant: (characterId) => `/api/characters/${characterId}/enhancement/enchant`,

    // 更新称号
    updateTitle: (characterId) => `/api/characters/${characterId}/enhancement/title`,

    // 更新宠物
    updatePet: (characterId) => `/api/characters/${characterId}/enhancement/pet`,

    // 更新装备
    updateEquipment: (characterId) => `/api/characters/${characterId}/enhancement/equipment`,

    // 更新徽章
    updateEmblem: (characterId) => `/api/characters/${characterId}/enhancement/emblem`
}
