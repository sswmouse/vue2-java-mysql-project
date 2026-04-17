/**
 * 称号选项API接口
 */

export default {
    // 获取所有称号选项
    getAll: '/api/title-options',

    // 根据ID获取
    getById: (id) => `/api/title-options/${id}`,

    // 根据赛季获取
    getBySeason: (season) => `/api/title-options/season/${season}`,

    // 获取最新赛季选项
    getLatest: '/api/title-options/latest',

    // 获取所有赛季
    getSeasons: '/api/title-options/seasons',

    // 按赛季分组获取所有选项
    getGrouped: '/api/title-options/grouped',

    // 创建
    create: '/api/title-options',

    // 更新
    update: (id) => `/api/title-options/${id}`,

    // 删除
    delete: (id) => `/api/title-options/${id}`,

    // 批量创建
    batchCreate: '/api/title-options/batch'
}
