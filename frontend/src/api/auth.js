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
    updateRole: '/auth/users',

    // 更新个人资料
    updateProfile: '/auth/profile',

    // 获取用户主题偏好
    getTheme: '/auth/theme',

    // 更新用户主题偏好
    updateTheme: '/auth/theme',

    // 文件上传
    upload: '/upload/upload'
}
