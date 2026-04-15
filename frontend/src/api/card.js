/**
 * 卡片系统API
 */

export default {
    // 获取模板列表
    templates: '/api/cards/templates',

    // 获取角色卡片列表
    getCharacterCards: (characterId) => `/api/characters/${characterId}/cards`,

    // 创建卡片
    createCard: (characterId) => `/api/characters/${characterId}/cards`,

    // 更新卡片
    updateCard: (cardId) => `/api/cards/${cardId}`,

    // 删除卡片
    deleteCard: (cardId) => `/api/cards/${cardId}`,

    // 设为默认
    setDefault: (cardId) => `/api/cards/${cardId}/default`
}
