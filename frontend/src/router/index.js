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

Vue.use(VueRouter)

const routes = [
    {
        path: '/',
        component: MainLayout,
        children: [
            {
                path: '',
                name: 'Home',
                component: Home
            },
            {
                path: 'users',
                name: 'Users',
                component: Users
            },
            {
                path: 'role',
                name: 'Role',
                component: Role
            },
            {
                path: 'equipment',
                name: 'Equipment',
                component: Equipment
            },
            {
                path: 'fashion',
                name: 'Fashion',
                component: Fashion
            },
            {
                path: 'enhancement',
                name: 'Enhancement',
                component: Enhancement
            },
            {
                path: 'enchantment',
                name: 'Enchantment',
                component: Enchantment
            },
            {
                path: 'badge',
                name: 'Badge',
                component: Badge
            },
            {
                path: 'title',
                name: 'Title',
                component: Title
            },
            {
                path: 'pet',
                name: 'Pet',
                component: Pet
            },
            {
                path: 'mist',
                name: 'Mist',
                component: Mist
            }
        ]
    }
]

const router = new VueRouter({
    mode: 'history',
    base: process.env.BASE_URL,
    routes
})

export default router