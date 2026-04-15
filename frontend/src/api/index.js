/**
 * API接口统一导出
 */

// 导入各模块接口
import userApi from './user'
import productApi from './product'
import orderApi from './order'
import characterApi from './character'
import authApi from './auth'
import mistApi from './mist'
import enhancement from './enhancement'
import card from './card'

// 统一导出
export default {
    user: userApi,
    product: productApi,
    order: orderApi,
    character: characterApi,
    auth: authApi,
    mist: mistApi,
    enhancement,
    card
}

// 单独导出各模块
export { userApi, productApi, orderApi, characterApi, authApi, mistApi, enhancement, card }
