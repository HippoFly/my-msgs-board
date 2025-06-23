<template>
  <div class="message-board">
    <div class="header">
      <h2>留言板</h2>
      <button v-if="!isLoggedIn" @click="showLogin">登录</button>
      <span v-else>已登录：{{ username }}</span>
    </div>
    <ul class="messages">
      <li v-for="(msg, idx) in messages" :key="idx">
        <span class="nickname">{{ msg.nickname || '匿名用户' }}</span>
        <span class="content">{{ msg.content }}</span>
        <span class="time">{{ msg.time }}</span>
      </li>
    </ul>
    <div class="input-area">
      <input v-model="nickname" placeholder="昵称（可选）" style="width: 150px" />
      <input v-model="input" @keyup.enter="submitMessage" placeholder="请输入留言..." />
      <button @click="submitMessage">提交</button>
    </div>
    <LoginDialog @login-success="handleLoginSuccess" ref="loginDialog" />
  </div>
</template>

<script>
import { apiService } from '../utils/api'
import LoginDialog from './LoginDialog.vue'

export default {
  name: 'MessageBoard',
  components: {
    LoginDialog
  },
  data() {
    return {
      nickname: '',
      input: '',
      messages: [],
      isLoggedIn: false,
      username: ''
    }
  },
  methods: {
    showLogin() {
      this.$refs.loginDialog.show()
    },
    handleLoginSuccess() {
      this.isLoggedIn = true
      this.username = localStorage.getItem('username')
    },
    async submitMessage() {
      if (!this.input.trim()) return;
      
      try {
        const response = await apiService.addMessage({
          nickname: this.nickname.trim() || undefined,
          content: this.input
        });
        this.messages.unshift(response.data);
        this.input = '';
      } catch (error) {
        console.error('提交留言失败:', error);
      }
    },
    async loadMessages() {
      try {
        const response = await apiService.getMessages();
        this.messages = response.data;
      } catch (error) {
        console.error('加载留言失败:', error);
      }
    }
  },
  mounted() {
    this.loadMessages()
  }
}
</script>

<style scoped>
.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

button {
  padding: 5px 10px;
  background: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background: #35946a;
}

.message-board {
  width: 100%;
  max-width: 600px;
  margin: 0 auto;
  padding: 1rem;
  box-sizing: border-box;
}

.messages {
  list-style: none;
  padding: 0;
  margin: 1rem 0;
}

.messages li {
  display: flex;
  align-items: center;
  padding: 0.5rem;
  border-bottom: 1px solid #eee;
  flex-wrap: wrap;
}

.nickname {
  color: #666;
  margin-right: 0.5rem;
  white-space: nowrap;
}

.content {
  flex: 1;
  margin-right: 1rem;
  word-break: break-word;
}

.time {
  color: #999;
  font-size: 0.9rem;
  white-space: nowrap;
}

.input-area {
  display: flex;
  gap: 0.5rem;
  margin-top: 1rem;
  flex-wrap: wrap;
}

.input-area input {
  flex: 1;
  padding: 0.5rem;
  border: 1px solid #ddd;
  border-radius: 4px;
  box-sizing: border-box;
}

.input-area button {
  padding: 0.5rem 1rem;
  background-color: #42b983;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.2s;
  box-sizing: border-box;
}

.input-area button:hover {
  background-color: #359469;
}

@media (max-width: 480px) {
  .message-board {
    padding: 0.5rem;
  }
  
  .input-area {
    flex-direction: column;
  }
  
  .input-area input {
    width: 100%;
  }
  
  .messages li {
    flex-direction: column;
    gap: 0.5rem;
  }
  
  .nickname, .content, .time {
    width: 100%;
  }
}
</style>

<style scoped>
.message-board {
  max-width: 400px;
  margin: 40px auto;
  padding: 24px;
  border-radius: 8px;
}

/* 默认白天模式样式 */
.message-board {
  border: 1px solid #eee;
  background: #fafbfc;
}
.messages {
  list-style: none;
  padding: 0;
  margin-bottom: 16px;
  max-height: 240px;
  overflow-y: auto;
  border: 1px solid #eee;
}
.messages li {
  padding: 8px 0;
  border-bottom: 1px solid #f0f0f0;
  display: flex;
  justify-content: space-between;
  font-size: 15px;
  color: #2c3e50;
}
.nickname {
  font-weight: 500;
  color: #42b983;
  margin-right: 8px;
}
.input-area {
  display: flex;
  gap: 8px;
}
input {
  flex: 1;
  padding: 6px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background: #fff;
  color: #2c3e50;
}
button {
  padding: 6px 16px;
  background: #42b983;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button:hover {
  background: #369870;
}
.time {
  color: #aaa;
  font-size: 12px;
  margin-left: 12px;
}

/* 黑夜模式样式 */
@media (prefers-color-scheme: dark) {
  .message-board {
    border: 1px solid #333;
    background: #1a1a1a;
  }
  .messages {
    border: 1px solid #333;
  }
  .messages li {
    border-bottom: 1px solid #333;
    color: #fff;
  }
  .input-area {
    color: #fff;
  }
  input {
    background: #2d2d2d;
    border: 1px solid #333;
    color: #fff;
  }
  .time {
    color: #888;
  }
}
</style> 