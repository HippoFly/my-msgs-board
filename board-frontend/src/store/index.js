import { createStore } from 'vuex'

export default createStore({
  state: {
    user: null,
    isLoggedIn: false,
    userRole: null
  },
  mutations: {
    setUser(state, userData) {
      state.user = userData
      state.isLoggedIn = true
      state.userRole = userData.role
    },
    clearUser(state) {
      state.user = null
      state.isLoggedIn = false
      state.userRole = null
    }
  },
  actions: {
    loginSuccess({ commit }, userData) {
      // 保存用户信息到 localStorage
      localStorage.setItem('user', JSON.stringify(userData))
      commit('setUser', userData)
    },
    logout({ commit }) {
      // 清除 localStorage
      localStorage.removeItem('user')
      commit('clearUser')
    },
    // 初始化时检查登录状态
    checkAuth({ commit }) {
      const userData = localStorage.getItem('user')
      if (userData) {
        commit('setUser', JSON.parse(userData))
      }
    }
  },
  getters: {
    isLoggedIn: state => state.isLoggedIn,
    userRole: state => state.userRole,
    currentUser: state => state.user
  }
}) 