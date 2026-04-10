/**
 * API接口统一导出
 */

// 导入各模块接口
import userApi from './user'
import productApi from './product'
import orderApi from './order'
import characterApi from './character'

// 统一导出
export default {
    user: userApi,
    product: productApi,
    order: orderApi,
    character: characterApi
}

// 单独导出各模块
export { userApi, productApi, orderApi, characterApi }
