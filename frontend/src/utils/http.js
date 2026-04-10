import axios from 'axios'
import { Message } from 'element-ui'

/**
 * 创建axios实例
 */
const service = axios.create({
    baseURL: '/api',
    timeout: 15000,
    headers: {
        'Content-Type': 'application/json;charset=UTF-8'
    }
})

/**
 * 请求拦截器
 */
service.interceptors.request.use(
    config => {
        // 添加token
        const token = localStorage.getItem('token')
        if (token) {
            config.headers['Authorization'] = `Bearer ${token}`
        }
        return config
    },
    error => {
        return Promise.reject(error)
    }
)

/**
 * 响应拦截器
 */
service.interceptors.response.use(
    response => {
        const res = response.data

        // 处理标准响应格式 { code, message, data }
        if (res.code !== undefined) {
            if (res.code === 200 || res.code === '200') {
                return res.data
            }

            Message.error(res.message || '请求失败')

            // 401处理
            if (res.code === 401 || res.code === '401') {
                localStorage.removeItem('token')
                window.location.href = '/login'
            }

            return Promise.reject(new Error(res.message || '请求失败'))
        }

        return res
    },
    error => {
        let message = '请求失败'

        if (error.response) {
            const statusMap = {
                400: '请求参数错误',
                401: '未授权，请登录',
                403: '拒绝访问',
                404: '请求的资源不存在',
                500: '服务器内部错误',
                502: '网关错误',
                503: '服务不可用',
                504: '网关超时'
            }
            message = statusMap[error.response.status] || `请求失败(${error.response.status})`
            if (error.response.status === 401) {
                localStorage.removeItem('token')
                window.location.href = '/login'
            }
        } else if (error.code === 'ECONNABORTED') {
            message = '请求超时'
        }

        Message.error(message)
        return Promise.reject(error)
    }
)

/**
 * 将$request挂载到Vue原型上的插件
 */
export const HttpPlugin = {
    install(Vue) {
        Vue.prototype.$request = service
    }
}

export default service
