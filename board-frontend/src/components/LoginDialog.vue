<template>
  <teleport to="body">
    <div v-if="visible" class="login-dialog-overlay">
      <div class="login-dialog">
        <div class="dialog-header">
          <h3>用户登录</h3>
          <button @click="close">×</button>
        </div>
        <div class="dialog-content">
          <form @submit.prevent="handleLogin">
            <div class="form-group">
              <label for="username">用户名</label>
              <input
                type="text"
                id="username"
                v-model="username"
                required
                placeholder="请输入用户名"
              />
            </div>
            <div class="form-group">
              <label for="password">密码</label>
              <input
                type="password"
                id="password"
                v-model="password"
                required
                placeholder="请输入密码"
              />
            </div>
            <div v-if="error" class="error-message">
              {{ error }}
            </div>
            <div class="form-group">
              <button type="submit" :disabled="loading">
                {{ loading ? '登录中...' : '登录' }}
              </button>
            </div>
          </form>
        </div>
      </div>
    </div>
  </teleport>
</template>

<script>
import { apiService } from '../utils/api'
import { useStore } from 'vuex'

export default {
  name: 'LoginDialog',
  data() {
    return {
      visible: false,
      username: '',
      password: '',
      loading: false,
      error: ''
    }
  },
  methods: {
    show() {
      this.visible = true
      this.error = ''
    },
    close() {
      this.visible = false
      this.username = ''
      this.password = ''
      this.error = ''
    },
    async handleLogin() {
      this.loading = true
      this.error = ''
      
      try {
        const response = await apiService.login({
          username: this.username,
          password: this.password
        })
        
        if (response.data.success) {
          // 保存用户信息到 Vuex
          this.$store.dispatch('loginSuccess', {
            username: this.username,
            role: response.data.role
          })
          this.$emit('login-success')
          this.close()
        } else {
          this.error = '用户名或密码错误'
        }
      } catch (error) {
        console.error('登录失败:', error)
        this.error = '登录失败，请稍后重试'
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-dialog-overlay {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: rgba(0, 0, 0, 0.5);
  display: flex;
  justify-content: center;
  align-items: center;
  z-index: 1000;
}

.login-dialog {
  background: white;
  padding: 20px;
  border-radius: 8px;
  width: 300px;
}

.dialog-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.dialog-header button {
  background: none;
  border: none;
  font-size: 24px;
  cursor: pointer;
}

.form-group {
  margin-bottom: 15px;
}

label {
  display: block;
  margin-bottom: 5px;
}

input {
  width: 100%;
  padding: 8px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

button[type="submit"] {
  width: 100%;
  padding: 10px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button[type="submit"]:disabled {
  background: #a8d5c2;
  cursor: not-allowed;
}

button[type="submit"]:not(:disabled):hover {
  background: #35946a;
}

.error-message {
  color: #ff4444;
  margin-bottom: 15px;
  font-size: 14px;
  text-align: center;
}
</style>
