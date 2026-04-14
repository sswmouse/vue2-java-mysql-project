/**
 * 认证API接口
 */

export default {
    // 用户登录
    login: '/auth/login',

    // 用户注册
    register: '/auth/register',

    // 获取当前用户信息
    me: '/auth/me',

    // 获取用户权限树
    permissions: '/auth/permissions',

    // 更新用户角色
    updateRole: '/auth/users'
}
