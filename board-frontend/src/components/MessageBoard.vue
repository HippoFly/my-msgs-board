<template>
  <div class="message-board">
    <h2>留言板</h2>
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
  </div>
</template>

<script>
import axios from 'axios'

export default {
  name: 'MessageBoard',
  data() {
    return {
      nickname: '',
      input: '',
      messages: []
    }
  },
  methods: {
    async submitMessage() {
      if (!this.input.trim()) return;
      
      try {
        const response = await axios.post('http://localhost:8080/api/messages', {
          nickname: this.nickname.trim() || undefined,
          content: this.input
        })
        this.messages.unshift(response.data)
        this.input = ''
      } catch (error) {
        console.error('提交留言失败:', error)
      }
    },
    async loadMessages() {
      try {
        const response = await axios.get('http://localhost:8080/api/messages')
        this.messages = response.data
      } catch (error) {
        console.error('加载留言失败:', error)
      }
    }
  },
  mounted() {
    this.loadMessages()
  }
}
</script>

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