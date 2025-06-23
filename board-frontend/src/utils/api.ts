import axios from 'axios';
import { getApiConfig, API_PATHS } from '../config/api.config';
import store from '../store';

// 创建 axios 实例
const api = axios.create({
  baseURL: getApiConfig().baseUrl,
  timeout: 10000,
  headers: {
    'Content-Type': 'application/json',
  },
});

// 请求拦截器
api.interceptors.request.use(
  (config) => {
    // 从 localStorage 获取用户信息
    const user = localStorage.getItem('user');
    if (user) {
      const userData = JSON.parse(user);
      // 如果有用户信息，添加到请求头
      config.headers['Authorization'] = `Bearer ${userData.token}`;
    }
    return config;
  },
  (error) => {
    console.error('请求错误:', error);
    return Promise.reject(error);
  }
);

// 响应拦截器
api.interceptors.response.use(
  (response) => {
    return response;
  },
  (error) => {
    // 处理 401 未授权错误
    if (error.response && error.response.status === 401) {
      // 清除登录状态
      store.dispatch('logout');
    }
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

// API 接口定义
export const apiService = {
  // 获取当前 API 配置
  getApiConfig: getApiConfig,
  
  // 用户相关接口
  async login(data: any) {
    return api.post(`${API_PATHS.messages}/login`, data);
  },
  
  // 消息相关接口
  async getMessages() {
    return api.get(API_PATHS.messages);
  },
  async addMessage(data: any) {
    return api.post(API_PATHS.messages, data);
  },
  
  // 管理员接口
  async hideMessage(id: number) {
    return api.put(`${API_PATHS.messages}/${id}/hide`);
  },
  async showMessage(id: number) {
    return api.put(`${API_PATHS.messages}/${id}/show`);
  },
  async getAllMessages() {
    return api.get(`${API_PATHS.messages}/all`);
  }
};
