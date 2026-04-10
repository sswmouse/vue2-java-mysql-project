/**
 * 用户相关接口定义
 */
export default {
    // 获取用户列表
    list: '/users',

    // 获取用户详情
    detail: '/users/:id',

    // 创建用户
    create: '/users',

    // 更新用户信息
    update: '/users/:id',

    // 删除用户
    delete: '/users/:id',

    // 用户登录
    login: '/auth/login',

    // 用户登出
    logout: '/auth/logout',

    // 用户注册
    register: '/auth/register',

    // 修改密码
    changePassword: '/users/:id/password',

    // 重置密码
    resetPassword: '/users/:id/reset-password'
}
