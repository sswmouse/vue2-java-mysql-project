import Vue from 'vue'
import App from './App.vue'
import router, { setRouterStore } from './router'
import store from './store'
import ElementUI from 'element-ui'
import { HttpPlugin } from '@/utils/http'

// 导入全局样式（必须在Element UI样式之前导入，以便覆盖变量）
import '@/assets/styles/index.less'
// 保留Element UI基础样式，但主题色已被覆盖
import 'element-ui/lib/theme-chalk/index.css'

Vue.config.productionTip = false

Vue.use(ElementUI)
Vue.use(HttpPlugin)

// 设置store到router（用于路由守卫访问Vuex）
setRouterStore(store)

// 应用启动时初始化认证状态
store.dispatch('auth/initAuth')

// 添加全局动画配置
Vue.prototype.$animate = (element, animation, options = {}) => {
    import('@/utils/animations').then(module => {
        if (module[animation]) {
            module[animation](element, options)
        }
    })
}

new Vue({
    router,
    store,
    mounted() {
        // 页面加载完成后的动画
        document.body.classList.add('page-loaded')
    },
    render: h => h(App)
}).$mount('#app')