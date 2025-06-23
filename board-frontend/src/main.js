import './assets/main.css'
import { createApp } from 'vue'
import { createStore } from 'vuex'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import App from './App.vue'
import router from './router'
import store from './store'

// 创建应用实例
const app = createApp(App)

// 注册所有图标
for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

// 按顺序使用插件
app.use(store)
app.use(router)
app.use(ElementPlus)

// 挂载应用
app.mount('#app')

// 初始化时检查登录状态
store.dispatch('checkAuth')
