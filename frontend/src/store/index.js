import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'
import theme from './modules/theme'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        auth,
        theme
    },
    state: {
        users: []
    },
    mutations: {
        SET_USERS(state, users) {
            state.users = users
        }
    },
    actions: {
        setUsers({ commit }, users) {
            commit('SET_USERS', users)
        }
    },
    getters: {
        getUsers: state => state.users,
        // 用户ID - 代理到auth模块的userId getter
        userId: (state, getters, rootState, rootGetters) => rootGetters['auth/userId']
    }
})
