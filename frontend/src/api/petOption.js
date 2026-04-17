/**
 * 宠物选项API接口
 */

export default {
    // 获取所有宠物选项
    getAll: '/api/pet-options',

    // 根据ID获取
    getById: (id) => `/api/pet-options/${id}`,

    // 根据赛季获取
    getBySeason: (season) => `/api/pet-options/season/${season}`,

    // 获取最新赛季选项
    getLatest: '/api/pet-options/latest',

    // 获取所有赛季
    getSeasons: '/api/pet-options/seasons',

    // 按赛季分组获取所有选项
    getGrouped: '/api/pet-options/grouped',

    // 创建
    create: '/api/pet-options',

    // 更新
    update: (id) => `/api/pet-options/${id}`,

    // 删除
    delete: (id) => `/api/pet-options/${id}`,

    // 批量创建
    batchCreate: '/api/pet-options/batch'
}
