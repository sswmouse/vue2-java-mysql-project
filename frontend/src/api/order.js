/**
 * 订单相关接口定义
 */
export default {
    // 获取订单列表
    list: '/orders',

    // 获取订单详情
    detail: '/orders/:id',

    // 创建订单
    create: '/orders',

    // 取消订单
    cancel: '/orders/:id/cancel',

    // 确认收货
    confirm: '/orders/:id/confirm',

    // 申请退款
    refund: '/orders/:id/refund',

    // 订单支付
    pay: '/orders/:id/pay',

    // 查询物流
    logistics: '/orders/:id/logistics'
}
