/**
 * 商品相关接口定义
 */
export default {
    // 获取商品列表
    list: '/products',

    // 获取商品详情
    detail: '/products/:id',

    // 创建商品
    create: '/products',

    // 更新商品信息
    update: '/products/:id',

    // 删除商品
    delete: '/products/:id',

    // 商品上架
    onSale: '/products/:id/on-sale',

    // 商品下架
    offSale: '/products/:id/off-sale',

    // 更新库存
    updateStock: '/products/:id/stock'
}
