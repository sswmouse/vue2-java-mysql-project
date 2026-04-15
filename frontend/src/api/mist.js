/**
 * 迷雾系统相关API
 */

export default {
    // 获取所有跨区的迷雾数据
    getAllFogData: (userId) => `/api/fogs?userId=${userId}`,

    // 获取指定跨区的迷雾数据
    getFogByRegion: (region, userId) => `/api/fogs/${region}?userId=${userId}`,

    // 更新指定跨区的迷雾等级
    updateFogLevel: (region) => `/api/fogs/${region}`,

    // 批量初始化所有跨区
    initAllRegions: (userId) => `/api/fogs/init?userId=${userId}`,

    // 获取所有跨区列表
    getRegions: '/api/fogs/regions'
}
