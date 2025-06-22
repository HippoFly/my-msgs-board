import axios from 'axios';
import { getApiConfig, API_PATHS } from '../config/api.config';

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
    // 可以在这里添加全局请求头
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
    console.error('响应错误:', error);
    return Promise.reject(error);
  }
);

// API 接口定义
export const apiService = {
  // 获取当前 API 配置
  getApiConfig: getApiConfig,
  
  // 消息相关接口
  async getMessages() {
    return api.get(API_PATHS.messages);
  },
  async addMessage(data: any) {
    return api.post(API_PATHS.messages, data);
  }
};
