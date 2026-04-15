/**
 * 角色相关API
 */

export default {
    // 角色类型相关
    characterType: {
        list: '/api/character-types',
        detail: (id) => `/api/character-types/${id}`,
        cascade: '/api/character-types/cascade'
    },

    // 角色相关
    list: (userId) => `/api/characters/user/${userId}`,
    detail: (id) => `/api/characters/${id}`,
    create: '/api/characters',
    update: (id) => `/api/characters/${id}`,
    delete: (id) => `/api/characters/${id}`,

    // DNF角色同步相关
    sync: {
        // 同步单个角色
        single: (characterId) => `/api/character-sync/sync/${characterId}`,
        // 同步用户所有角色
        all: '/api/character-sync/sync-all',
        // 根据冒险团查询
        query: '/api/character-sync/query',
        // 获取同步状态
        status: (characterId) => `/api/character-sync/status/${characterId}`
    }
}
