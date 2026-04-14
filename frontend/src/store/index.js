import Vue from 'vue'
import Vuex from 'vuex'
import auth from './modules/auth'

Vue.use(Vuex)

export default new Vuex.Store({
    modules: {
        auth
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
        getUsers: state => state.users
    }
})
