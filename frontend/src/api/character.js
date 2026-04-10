/**
 * 角色相关API
 */

export default {
    // 角色类型相关
    characterType: {
        list: '/character-types',
        detail: (id) => `/character-types/${id}`,
        cascade: '/character-types/cascade'
    },

    // 角色相关
    list: (userId) => `/characters/user/${userId}`,
    detail: (id) => `/characters/${id}`,
    create: '/characters',
    update: (id) => `/characters/${id}`,
    delete: (id) => `/characters/${id}`
}
