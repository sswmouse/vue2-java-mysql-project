import Vue from 'vue'
import VueRouter from 'vue-router'
import MainLayout from '../layout/MainLayout.vue'
import Home from '../views/Home.vue'
import Users from '../views/Users.vue'
import Role from '../views/Role.vue'
import Equipment from '../views/Equipment.vue'
import Fashion from '../views/Fashion.vue'
import Enhancement from '../views/Enhancement.vue'
import Enchantment from '../views/Enchantment.vue'
import Badge from '../views/Badge.vue'
import Title from '../views/Title.vue'
import Pet from '../views/Pet.vue'
import Mist from '../views/Mist.vue'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import NoPermission from '../views/NoPermission.vue'
import Profile from '../views/Profile.vue'
import Setting from '../views/Setting.vue'

Vue.use(VueRouter)

const routes = [
    // 公开路由
    {
        path: '/login',
        name: 'Login',
        component: Login,
        meta: { requiresAuth: false }
    },
    {
        path: '/register',
        name: 'Register',
        component: Register,
        meta: { requiresAuth: false }
    },
    {
        path: '/no-permission',
        name: 'NoPermission',
        component: NoPermission,
        meta: { requiresAuth: false }
    },
    // 主布局路由
    {
        path: '/',
        component: MainLayout,
        meta: { requiresAuth: true },
        children: [
            {
                path: '',
                name: 'Home',
                component: Home,
                meta: { requiresAuth: true, permission: 'menu:home' }
            },
            {
                path: 'profile',
                name: 'Profile',
                component: Profile,
                meta: { requiresAuth: true, permission: 'menu:profile' }
            },
            {
                path: 'setting',
                name: 'Setting',
                component: Setting,
                meta: { requiresAuth: true, permission: 'menu:setting' }
            },
            {
                path: 'users',
                name: 'Users',
                component: Users,
                meta: { requiresAuth: true, requiresAdmin: true, permission: 'menu:users' }
            },
            {
                path: 'role',
                name: 'Role',
                component: Role,
                meta: { requiresAuth: true, permission: 'menu:role' }
            },
            {
                path: 'equipment',
                name: 'Equipment',
                component: Equipment,
                meta: { requiresAuth: true, permission: 'menu:equipment' }
            },
            {
                path: 'fashion',
                name: 'Fashion',
                component: Fashion,
                meta: { requiresAuth: true, permission: 'menu:fashion' }
            },
            {
                path: 'enhancement',
                name: 'Enhancement',
                component: Enhancement,
                meta: { requiresAuth: true, permission: 'menu:enhancement' }
            },
            {
                path: 'enchantment',
                name: 'Enchantment',
                component: Enchantment,
                meta: { requiresAuth: true, permission: 'menu:enchantment' }
            },
            {
                path: 'badge',
                name: 'Badge',
                component: Badge,
                meta: { requiresAuth: true, permission: 'menu:badge' }
            },
            {
                path: 'title',
                name: 'Title',
                component: Title,
                meta: { requiresAuth: true, permission: 'menu:title' }
            },
            {
                path: 'pet',
                name: 'Pet',
                component: Pet,
                meta: { requiresAuth: true, permission: 'menu:pet' }
            },
            {
                path: 'mist',
                name: 'Mist',
                component: Mist,
                meta: { requiresAuth: true, permission: 'menu:mist' }
            }
        ]
    },
    // 404 通配符路由 - 必须放在最后
    {
        path: '*',
        name: 'NotFound',
        component: () => import('../views/NotFound.vue'),
        meta: { requiresAuth: false }
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

// 路由白名单（不需要权限检查的路径）
const whiteList = ['/login', '/register', '/no-permission']

// 路由守卫
router.beforeEach(async (to, from, next) => {
    // 公开路由直接放行
    if (!to.meta.requiresAuth || whiteList.includes(to.path)) {
        // 如果已登录且访问登录/注册页，跳转到首页
        if (store.state.auth.token && (to.path === '/login' || to.path === '/register')) {
            next('/')
        } else {
            next()
        }
        return
    }

    // 需要认证的页面
    const token = store.state.auth.token
    const isAuthenticated = !!token

    // 没有token，直接跳转登录
    if (!isAuthenticated) {
        next('/login')
        return
    }

    // 有token但没有用户信息，需要从后端加载
    if (!store.state.auth.user || !store.state.auth.isLoaded) {
        try {
            // 加载用户信息和权限
            await store.dispatch('auth/loadCurrentUser')
            await store.dispatch('auth/loadPermissions')

            // 重新检查权限
            if (!store.state.auth.user) {
                next('/login')
                return
            }
        } catch (error) {
            console.error('加载用户信息失败:', error)
            next('/login')
            return
        }
    }

    // 检查管理员权限
    if (to.meta.requiresAdmin) {
        if (!store.getters['auth/isAdmin']) {
            next('/no-permission')
            return
        }
    }

    // 检查菜单权限
    // 首页、个人中心和设置总是可访问的（所有登录用户都能访问）
    if (to.meta.permission) {
        // 首页和个人中心、设置不需要权限检查
        if (to.path === '/' || to.path === '/profile' || to.path === '/setting') {
            next()
            return
        }

        const permissions = store.state.auth.permissions
        // 如果权限还没加载，等待加载
        if (!permissions) {
            try {
                await store.dispatch('auth/loadPermissions')
            } catch (error) {
                console.error('加载权限失败:', error)
            }
        }

        // 再次检查权限
        if (permissions || store.state.auth.permissions) {
            const hasPermission = store.getters['auth/hasPermission'](to.meta.permission)
            if (!hasPermission) {
                next('/no-permission')
                return
            }
        }
    }

    next()
})

// 将store注入到router中
let store = null
export function setRouterStore(s) {
    store = s
}

export default router
