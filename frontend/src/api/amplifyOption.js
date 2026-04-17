/**
 * 增幅选项API接口
 */

export default {
    // 获取所有增幅选项
    getAll: '/api/amplify-options',

    // 根据ID获取
    getById: (id) => `/api/amplify-options/${id}`,

    // 根据增幅类型获取
    getByType: (type) => `/api/amplify-options/type/${type}`,

    // 创建
    create: '/api/amplify-options',

    // 更新
    update: (id) => `/api/amplify-options/${id}`,

    // 删除
    delete: (id) => `/api/amplify-options/${id}`,

    // 批量创建
    batchCreate: '/api/amplify-options/batch'
}
