/**
 * 附魔宝珠选项API接口
 */

export default {
    // 获取所有附魔选项
    getAll: '/api/enchant-options',

    // 根据ID获取
    getById: (id) => `/api/enchant-options/${id}`,

    // 根据装备部位获取
    getByPart: (part) => `/api/enchant-options/part/${part}`,

    // 根据礼包类型获取
    getByPackage: (packageType) => `/api/enchant-options/package/${packageType}`,

    // 获取最新赛季选项
    getLatest: '/api/enchant-options/latest',

    // 获取所有部位
    getParts: '/api/enchant-options/parts',

    // 获取所有礼包类型
    getPackages: '/api/enchant-options/packages',

    // 按部位分组获取所有选项
    getGrouped: '/api/enchant-options/grouped',

    // 创建
    create: '/api/enchant-options',

    // 更新
    update: (id) => `/api/enchant-options/${id}`,

    // 删除
    delete: (id) => `/api/enchant-options/${id}`,

    // 批量创建
    batchCreate: '/api/enchant-options/batch'
}
