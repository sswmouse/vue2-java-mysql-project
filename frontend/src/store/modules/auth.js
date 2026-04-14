/**
 * Vuex Auth Module
 * 处理用户认证、权限管理和会话状态
 */

import request from '@/utils/http'
import api from '@/api'

// 从localStorage恢复状态
const getDefaultState = () => {
    return {
        user: JSON.parse(localStorage.getItem('user') || 'null'),
        token: localStorage.getItem('token') || null,
        permissions: JSON.parse(localStorage.getItem('permissions') || 'null'),
        isLoaded: false,  // 是否已从后端加载过用户信息
        loading: false
    }
}

const state = getDefaultState()

const mutations = {
    SET_USER(state, user) {
        state.user = user
        if (user) {
            localStorage.setItem('user', JSON.stringify(user))
        } else {
            localStorage.removeItem('user')
        }
    },

    SET_TOKEN(state, token) {
        state.token = token
        if (token) {
            localStorage.setItem('token', token)
        } else {
            localStorage.removeItem('token')
        }
    },

    SET_PERMISSIONS(state, permissions) {
        state.permissions = permissions
        if (permissions) {
            localStorage.setItem('permissions', JSON.stringify(permissions))
        } else {
            localStorage.removeItem('permissions')
        }
    },

    SET_LOADED(state, loaded) {
        state.isLoaded = loaded
    },

    SET_LOADING(state, loading) {
        state.loading = loading
    },

    CLEAR_AUTH(state) {
        state.user = null
        state.token = null
        state.permissions = null
        state.isLoaded = false
        state.loading = false
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        localStorage.removeItem('permissions')
    },

    // 重置状态
    RESET_STATE(state) {
        Object.assign(state, getDefaultState())
    }
}

const actions = {
    /**
     * 登录
     */
    async login({ commit, dispatch }, { token, user }) {
        commit('SET_TOKEN', token)
        commit('SET_USER', user)
        commit('SET_LOADED', true)
        // 登录后异步加载权限树（不阻塞登录流程）
        dispatch('loadPermissions').catch(err => {
            console.warn('权限加载失败，可后续重试:', err)
        })
    },

    /**
     * 登出
     */
    logout({ commit }) {
        commit('CLEAR_AUTH')
    },

    /**
     * 加载当前用户信息
     * 用于路由切换时检查登录状态
     */
    async loadCurrentUser({ commit, state }) {
        if (state.loading) return null
        if (!state.token) return null

        commit('SET_LOADING', true)
        try {
            const user = await request.get(api.auth.me)
            commit('SET_USER', user)
            commit('SET_LOADED', true)
            return user
        } catch (error) {
            // 如果获取用户信息失败，清除认证状态
            if (error.message.includes('未登录') || error.message.includes('401')) {
                commit('CLEAR_AUTH')
            }
            return null
        } finally {
            commit('SET_LOADING', false)
        }
    },

    /**
     * 加载权限树
     * 获取用户可访问的菜单和权限
     */
    async loadPermissions({ commit, state }) {
        if (!state.token) return null

        try {
            // 调用权限接口获取权限树
            const permissions = await request.get(api.auth.permissions)
            commit('SET_PERMISSIONS', permissions)
            return permissions
        } catch (error) {
            console.error('加载权限失败:', error)
            return null
        }
    },

    /**
     * 初始化认证状态
     * 应用启动时调用，检查登录状态
     */
    async initAuth({ dispatch, state }) {
        // 如果有token但没有用户信息，先加载用户信息
        if (state.token && !state.user) {
            await dispatch('loadCurrentUser')
        }
        // 如果有token，加载权限树
        if (state.token) {
            await dispatch('loadPermissions')
        }
    },

    /**
     * 检查是否有指定角色
     */
    hasRole({ state }, role) {
        if (!state.user) return false
        return state.user.role === role
    },

    /**
     * 检查是否有任意一个指定角色
     */
    hasAnyRole({ state }, roles) {
        if (!state.user) return false
        return roles.includes(state.user.role)
    },

    /**
     * 清除认证状态
     */
    clearAuth({ commit }) {
        commit('CLEAR_AUTH')
    }
}

const getters = {
    // 用户信息
    user: state => state.user,

    // Token
    token: state => state.token,

    // 权限列表
    permissions: state => state.permissions,

    // 是否已加载过用户信息
    isLoaded: state => state.isLoaded,

    // 是否正在加载
    isLoading: state => state.loading,

    // 是否已登录
    isAuthenticated: state => !!state.token && !!state.user,

    // 是否是管理员
    isAdmin: state => state.user && state.user.role === 'admin',

    // 用户角色
    userRole: state => state.user ? state.user.role : null,

    // 用户名
    username: state => state.user ? state.user.username : null,

    // 用户ID
    userId: state => state.user ? state.user.id : null,

    // 可访问的菜单路径列表
    accessibleMenus: state => {
        if (!state.permissions || !state.permissions.menuTree) return []
        return state.permissions.menuTree.map(menu => menu.path)
    },

    // 权限树
    menuTree: state => state.permissions ? state.permissions.menuTree : null,

    // 路由权限配置
    routePermissions: state => state.permissions ? state.permissions.routes : null,

    // 检查是否有指定权限（返回函数）
    hasPermission: (state) => (permission) => {
        if (!state.permissions) return false
        if (Array.isArray(state.permissions) && state.permissions.includes(permission)) {
            return true
        }
        if (state.permissions.menuPermissions && state.permissions.menuPermissions.includes(permission)) {
            return true
        }
        return false
    }
}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}
