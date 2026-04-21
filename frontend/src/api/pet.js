/**
 * @Description: 宠物系统API接口
 * @Author: Claude Code
 * @Date: 2026-04-21
 */

export default {
    // 获取所有宠物
    getAll: '/api/pets',
    // 获取单个宠物
    getById: (id) => `/api/pets/${id}`,
    // 根据年份获取
    getByYear: (year) => `/api/pets/year/${year}`,
    // 根据类型获取
    getByType: (type) => `/api/pets/type/${encodeURIComponent(type)}`,
    // 获取所有年份
    getYears: '/api/pets/years',
    // 搜索
    search: '/api/pets/search'
}