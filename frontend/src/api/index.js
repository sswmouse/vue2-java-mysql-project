import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json'
  }
})

// 请求拦截器
api.interceptors.request.use(
  config => {
    // 可以在这里添加token等
    return config
  },
  error => {
    return Promise.reject(error)
  }
)

// 响应拦截器
api.interceptors.response.use(
  response => {
    return response.data
  },
  error => {
    console.error('API请求错误:', error)
    return Promise.reject(error)
  }
)

// 用户相关API
export const userApi = {
  // 获取用户列表
  getUsers() {
    return api.get('/users')
  },
  // 获取单个用户
  getUser(id) {
    return api.get(`/users/${id}`)
  },
  // 创建用户
  createUser(userData) {
    return api.post('/users', userData)
  },
  // 更新用户
  updateUser(id, userData) {
    return api.put(`/users/${id}`, userData)
  },
  // 删除用户
  deleteUser(id) {
    return api.delete(`/users/${id}`)
  }
}

export default api