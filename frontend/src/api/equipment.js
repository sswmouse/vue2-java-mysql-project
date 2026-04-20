/**
 * @Description: 装备系统API接口
 * @Author: Claude Code
 * @Date: 2026-04-17
 * @LastEditTime: 2026-04-17
 * @FilePath: /vue2-java-mysql-project/frontend/src/api/equipment.js
 */

/**
 * 装备品级映射 - 前端与后端对应关系
 */
export const RARITY_MAP = {
    rare: '稀有',
    legendary: '神器',
    epic: '传说',
    mythic: '史诗',
    primal: '太初',
    blackEpic: '黑牙史诗',
    blackPrimal: '黑牙太初'
}

/**
 * 品级颜色映射
 * 稀有-紫色，神器-红色，传说-橙色，史诗-金色，太初-蓝色
 */
export const RARITY_COLORS = {
    rare: '#a855f7',        // 紫色
    legendary: '#ef4444',   // 红色
    epic: '#f97316',        // 橙色
    mythic: '#fbbf24',      // 金色
    primal: '#60a5fa',      // 太初 - 基础蓝色 (渐变在CSS中处理)
    blackEpic: '#dc2626',  // 黑牙史诗 - 深红色
    blackPrimal: '#1d4ed8' // 黑牙太初 - 深蓝色
}

/**
 * 太初蓝色渐变
 */
export const PRIMAL_GRADIENT = 'linear-gradient(135deg, #60a5fa 0%, #3b82f6 50%, #1d4ed8 100%)'

/**
 * 品级中文名到Key的映射
 */
export const RARITY_KEY_MAP = {
    '稀有': 'rare',
    '神器': 'legendary',
    '传说': 'epic',
    '史诗': 'mythic',
    '太初': 'primal',
    '黑牙史诗': 'blackEpic',
    '黑牙太初': 'blackPrimal'
}

/**
 * Key到品级中文名的映射
 */
export const KEY_TO_RARITY_MAP = {
    rare: '稀有',
    legendary: '神器',
    epic: '传说',
    mythic: '史诗',
    primal: '太初',
    blackEpic: '黑牙史诗',
    blackPrimal: '黑牙太初'
}

/**
 * 单件装备积分映射
 */
export const POINTS_MAP = {
    rare: 65,
    legendary: 115,
    epic: 165,
    mythic: 215,
    primal: 265,
    blackEpic: 165,
    blackPrimal: 215
}

/**
 * 单件装备名望映射
 */
export const FAME_MAP = {
    rare: 1000,
    legendary: 1100,
    epic: 1200,
    mythic: 1300,
    primal: 1400,
    blackEpic: 1500,
    blackPrimal: 1600
}

/**
 * API接口定义
 */
export default {
    // 获取所有套装列表
    list: '/api/equipment-sets',

    // 获取单个套装详情
    detail: (id) => `/api/equipment-sets/${id}`,

    // 计算套装积分和阶段
    calculate: '/api/equipment-sets/calculate',

    // 获取单件装备详情
    item: (id) => `/api/equipment-items/${id}`,

    // 根据套装ID获取所有装备
    itemsBySet: (setId) => `/api/equipment-items/set/${setId}`,

    // 获取所有阶段配置
    stages: '/api/stages'
}
