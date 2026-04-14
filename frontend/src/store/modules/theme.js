/**
 * Vuex Theme Module
 * 处理主题切换和样式管理
 */

import request from '@/utils/http'
import api from '@/api'

// 主题定义
export const themes = {
    // DNF霓虹风格 - 默认
    dnf: {
        id: 'dnf',
        name: 'DNF霓虹',
        description: '深邃星空暗色系，霓虹光效',
        type: 'dark',
        colors: {
            primary: '#4a1090',
            secondary: '#7b2fff',
            accent: '#ffd700',
            bgDark: '#07091a',
            bgCard: '#0d1930',
            bgHover: '#152040',
            textPrimary: '#e8eaf6',
            textSecondary: '#90a4b8',
            border: '#1a2840',
            success: '#00e5a0',
            warning: '#ffaa00',
            danger: '#ff3d6e',
            info: '#00b4ff'
        }
    },
    // 赛博朋克风格
    cyberpunk: {
        id: 'cyberpunk',
        name: '赛博朋克',
        description: '科技感霓虹，粉紫渐变',
        type: 'dark',
        colors: {
            primary: '#ff006e',
            secondary: '#8338ec',
            accent: '#3a86ff',
            bgDark: '#0a0a0f',
            bgCard: '#12121a',
            bgHover: '#1a1a2e',
            textPrimary: '#f0f0f5',
            textSecondary: '#a0a0b0',
            border: '#2a2a3a',
            success: '#00ff88',
            warning: '#ffbe0b',
            danger: '#ff0044',
            info: '#00d4ff'
        }
    },
    // 简约优雅风格
    elegant: {
        id: 'elegant',
        name: '简约优雅',
        description: '现代商务风格，低调奢华',
        type: 'dark',
        colors: {
            primary: '#2c3e50',
            secondary: '#34495e',
            accent: '#e74c3c',
            bgDark: '#1a1a2e',
            bgCard: '#16213e',
            bgHover: '#1f2b42',
            textPrimary: '#ecf0f1',
            textSecondary: '#bdc3c7',
            border: '#2a3a50',
            success: '#27ae60',
            warning: '#f39c12',
            danger: '#c0392b',
            info: '#3498db'
        }
    },
    // 极光幻彩风格
    aurora: {
        id: 'aurora',
        name: '极光幻彩',
        description: '自然极光色彩，绚丽梦幻',
        type: 'dark',
        colors: {
            primary: '#00c6ff',
            secondary: '#0072ff',
            accent: '#f72585',
            bgDark: '#0f0f23',
            bgCard: '#1a1a3e',
            bgHover: '#222250',
            textPrimary: '#e8e8f0',
            textSecondary: '#9090b0',
            border: '#2a2a50',
            success: '#00ff9f',
            warning: '#ffcc00',
            danger: '#ff4757',
            info: '#00d2ff'
        }
    },
    // 明亮清爽风格
    bright: {
        id: 'bright',
        name: '明亮清爽',
        description: '清新明亮的浅色主题',
        type: 'light',
        colors: {
            primary: '#3498db',
            secondary: '#2980b9',
            accent: '#e74c3c',
            bgDark: '#f5f7fa',
            bgCard: '#ffffff',
            bgHover: '#ecf0f1',
            textPrimary: '#2c3e50',
            textSecondary: '#7f8c8d',
            border: '#dfe6e9',
            success: '#27ae60',
            warning: '#f39c12',
            danger: '#e74c3c',
            info: '#3498db'
        }
    },
    // 粉嫩可爱风格
    pink: {
        id: 'pink',
        name: '粉嫩可爱',
        description: '粉色少女心，卡通可爱',
        type: 'light',
        colors: {
            primary: '#ff69b4',
            secondary: '#ff1493',
            accent: '#ff6b9d',
            bgDark: '#fff0f5',
            bgCard: '#ffffff',
            bgHover: '#ffe4ec',
            textPrimary: '#d63384',
            textSecondary: '#a94442',
            border: '#ffc0cb',
            success: '#28a745',
            warning: '#ffc107',
            danger: '#dc3545',
            info: '#17a2b8'
        }
    },
    // 森林自然风格
    forest: {
        id: 'forest',
        name: '森林自然',
        description: '清新绿色系，自然环保',
        type: 'light',
        colors: {
            primary: '#2ecc71',
            secondary: '#27ae60',
            accent: '#f39c12',
            bgDark: '#f0f8f0',
            bgCard: '#ffffff',
            bgHover: '#e8f5e9',
            textPrimary: '#1e7e34',
            textSecondary: '#5a8f5a',
            border: '#a8d5a2',
            success: '#2ecc71',
            warning: '#f1c40f',
            danger: '#e74c3c',
            info: '#3498db'
        }
    },
    // 星空深邃风格
    galaxy: {
        id: 'galaxy',
        name: '星空深邃',
        description: '浩瀚星空，宇宙神秘',
        type: 'dark',
        colors: {
            primary: '#9b59b6',
            secondary: '#8e44ad',
            accent: '#00cec9',
            bgDark: '#0c0c1e',
            bgCard: '#151530',
            bgHover: '#1f1f40',
            textPrimary: '#dfe6e9',
            textSecondary: '#a0a0c0',
            border: '#2d2d50',
            success: '#00b894',
            warning: '#fdcb6e',
            danger: '#ff7675',
            info: '#74b9ff'
        }
    },
    // 暗夜血红风格
    blood: {
        id: 'blood',
        name: '暗夜血红',
        description: '暗黑哥特，血色浪漫',
        type: 'dark',
        colors: {
            primary: '#8b0000',
            secondary: '#dc143c',
            accent: '#ffd700',
            bgDark: '#0a0a0a',
            bgCard: '#1a0a0a',
            bgHover: '#2a1010',
            textPrimary: '#f5f5f5',
            textSecondary: '#c0c0c0',
            border: '#3a1a1a',
            success: '#228b22',
            warning: '#ffa500',
            danger: '#dc143c',
            info: '#4169e1'
        }
    },
    // 小熊软糖风格
    gummy: {
        id: 'gummy',
        name: '小熊软糖',
        description: '彩色糖果，软萌可爱',
        type: 'light',
        colors: {
            primary: '#ff9ff3',
            secondary: '#feca57',
            accent: '#48dbfb',
            bgDark: '#fff9ff',
            bgCard: '#ffffff',
            bgHover: '#fff5fc',
            textPrimary: '#5f27cd',
            textSecondary: '#9b59b6',
            border: '#ffeaa7',
            success: '#00d2d3',
            warning: '#ff9f43',
            danger: '#ff6b6b',
            info: '#54a0ff'
        }
    }
}

// 从localStorage获取保存的主题
const getSavedTheme = () => {
    return localStorage.getItem('theme') || 'dnf'
}

// 默认状态
const state = () => ({
    currentTheme: getSavedTheme(),
    themes: themes,
    isLoaded: false
})

const mutations = {
    SET_THEME(state, themeId) {
        state.currentTheme = themeId
        localStorage.setItem('theme', themeId)
    },
    SET_LOADED(state, loaded) {
        state.isLoaded = loaded
    }
}

const actions = {
    /**
     * 设置主题并保存到服务器
     */
    async setTheme({ commit, dispatch }, themeId) {
        if (!themes[themeId]) return

        commit('SET_THEME', themeId)
        dispatch('applyTheme', themeId)

        // 保存到服务器
        dispatch('saveThemeToServer', themeId)
    },

    /**
     * 应用主题样式到文档
     */
    applyTheme(_, themeId) {
        const theme = themes[themeId]
        if (!theme) return

        const root = document.documentElement
        const { colors } = theme

        // 设置 CSS 变量（应用样式）
        root.style.setProperty('--theme-primary', colors.primary)
        root.style.setProperty('--theme-secondary', colors.secondary)
        root.style.setProperty('--theme-accent', colors.accent)
        root.style.setProperty('--theme-bg-dark', colors.bgDark)
        root.style.setProperty('--theme-bg-card', colors.bgCard)
        root.style.setProperty('--theme-bg-hover', colors.bgHover)
        root.style.setProperty('--theme-text-primary', colors.textPrimary)
        root.style.setProperty('--theme-text-secondary', colors.textSecondary)
        root.style.setProperty('--theme-border', colors.border)
        root.style.setProperty('--theme-success', colors.success)
        root.style.setProperty('--theme-warning', colors.warning)
        root.style.setProperty('--theme-danger', colors.danger)
        root.style.setProperty('--theme-info', colors.info)

        // 设置 Element UI CSS 变量（覆盖 Element UI 默认样式）
        root.style.setProperty('--color-primary', colors.primary)
        root.style.setProperty('--color-success', colors.success)
        root.style.setProperty('--color-warning', colors.warning)
        root.style.setProperty('--color-danger', colors.danger)
        root.style.setProperty('--color-info', colors.info)
        root.style.setProperty('--color-text-primary', colors.textPrimary)
        root.style.setProperty('--color-text-regular', colors.textSecondary)
        root.style.setProperty('--color-text-secondary', colors.textSecondary)
        root.style.setProperty('--border-color-base', colors.border)
        root.style.setProperty('--border-color-light', colors.border)
        root.style.setProperty('--border-color-lighter', colors.border)
        root.style.setProperty('--background-color-base', colors.bgCard)
        root.style.setProperty('--fill-base', colors.bgCard)
        root.style.setProperty('--fill-light', colors.bgHover)

        // 设置主题类型（用于深色/浅色模式适配）
        root.setAttribute('data-theme-type', theme.type)

        // 触发自定义事件通知组件更新
        window.dispatchEvent(new CustomEvent('theme-change', { detail: { theme: themeId, colors } }))
    },

    /**
     * 从服务器加载主题
     */
    async loadThemeFromServer({ commit, dispatch }) {
        try {
            const response = await request.get(api.auth.getTheme)
            if (response && response.theme && themes[response.theme]) {
                commit('SET_THEME', response.theme)
                dispatch('applyTheme', response.theme)
            }
        } catch (error) {
            console.warn('从服务器加载主题失败，使用本地主题:', error)
        }
        commit('SET_LOADED', true)
    },

    /**
     * 保存主题到服务器
     */
    async saveThemeToServer(_, themeId) {
        try {
            await request.put(api.auth.updateTheme, { theme: themeId })
        } catch (error) {
            console.warn('保存主题到服务器失败:', error)
        }
    }
}

const getters = {
    currentTheme: state => state.currentTheme,
    currentThemeData: state => themes[state.currentTheme] || themes.dnf,
    allThemes: state => Object.values(state.themes),
    darkThemes: state => Object.values(state.themes).filter(t => t.type === 'dark'),
    lightThemes: state => Object.values(state.themes).filter(t => t.type === 'light'),
    isLoaded: state => state.isLoaded
}

export default {
    namespaced: true,
    state,
    mutations,
    actions,
    getters
}
