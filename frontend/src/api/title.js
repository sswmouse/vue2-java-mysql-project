/**
 * @Description: 称号系统API接口
 * @Author: Claude Code
 * @Date: 2026-04-21
 */

/**
 * 称号类型
 */
export const TITLE_TYPES = {
    SPRING: '春节',
    MAY: '五一',
    AUTUMN: '金秋'
}

export default {
    // 获取所有称号
    getAll: '/api/titles',
    // 获取单个称号
    getById: (id) => `/api/titles/${id}`,
    // 根据年份获取
    getByYear: (year) => `/api/titles/year/${year}`,
    // 根据类型获取
    getByType: (type) => `/api/titles/type/${encodeURIComponent(type)}`,
    // 获取所有年份
    getYears: '/api/titles/years',
    // 搜索
    search: '/api/titles/search'
}