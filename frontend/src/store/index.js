import Vue from 'vue'
import Vuex from 'vuex'

Vue.use(Vuex)

export default new Vuex.Store({
  state: {
    user: null,
    users: []
  },
  mutations: {
    SET_USER(state, user) {
      state.user = user
    },
    SET_USERS(state, users) {
      state.users = users
    }
  },
  actions: {
    setUser({ commit }, user) {
      commit('SET_USER', user)
    },
    setUsers({ commit }, users) {
      commit('SET_USERS', users)
    }
  },
  getters: {
    getUser: state => state.user,
    getUsers: state => state.users
  }
})