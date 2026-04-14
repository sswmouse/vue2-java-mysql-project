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
 * 检查是否正在跳转到登录页，避免重复跳转
 */
let isRedirecting = false

/**
 * 跳转到登录页并清除登录状态
 */
function redirectToLogin(message) {
    if (isRedirecting) return
    isRedirecting = true

    // 清除登录状态
    localStorage.removeItem('token')
    localStorage.removeItem('user')

    // 显示提示
    if (message) {
        Message.warning(message)
    }

    // 延迟跳转，让提示先显示
    setTimeout(() => {
        window.location.href = '/login'
        // 重置标记，1秒后允许再次跳转
        setTimeout(() => {
            isRedirecting = false
        }, 1000)
    }, 500)
}

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
            // 成功响应
            if (res.code === 200 || res.code === '200') {
                return res.data !== undefined ? res.data : res
            }

            // 未登录或登录过期
            if (res.code === 401 || res.code === '401') {
                redirectToLogin(res.message || '登录已过期，请重新登录')
                return Promise.reject(new Error(res.message || '未登录'))
            }

            // 其他错误，提示消息
            if (res.message) {
                Message.error(res.message)
            }

            return Promise.reject(new Error(res.message || '请求失败'))
        }

        // 处理后端错误响应格式 { error: "message" }
        if (res.error) {
            return Promise.reject(new Error(res.error))
        }

        // 处理登录成功响应 (AuthResponse: token, userId, username, phone, role)
        if (res.token !== undefined) {
            return res
        }

        return res
    },
    error => {
        let message = '请求失败'

        if (error.response) {
            // 处理后端返回的标准错误格式 { code, message, data }
            if (error.response.data && error.response.data.code !== undefined) {
                const errorData = error.response.data

                // 未登录或登录过期
                if (errorData.code === 401) {
                    redirectToLogin(errorData.message || '登录已过期，请重新登录')
                    return Promise.reject(new Error(errorData.message || '未登录'))
                }

                message = errorData.message || '请求失败'
            }
            // 处理后端返回的错误信息 { error: "message" }
            else if (error.response.data && error.response.data.error) {
                message = error.response.data.error
            } else {
                const statusMap = {
                    400: '请求参数错误',
                    401: '登录已过期，请重新登录',
                    403: '拒绝访问',
                    404: '请求的资源不存在',
                    500: '服务器内部错误',
                    502: '网关错误',
                    503: '服务不可用',
                    504: '网关超时'
                }
                message = statusMap[error.response.status] || `请求失败(${error.response.status})`

                // 401状态码特殊处理
                if (error.response.status === 401) {
                    redirectToLogin(message)
                    return Promise.reject(new Error(message))
                }
            }
        } else if (error.code === 'ECONNABORTED') {
            message = '请求超时'
        }

        // 创建带message的错误对象，保留给调用方处理
        const err = new Error(message)
        err.noMessage = true  // 标记为不需要再次显示message
        if (!err.noMessage) {
            Message.error(message)
        }
        return Promise.reject(err)
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
