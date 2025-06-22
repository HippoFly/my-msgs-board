interface ApiConfig {
  baseUrl: string;
}

interface ApiPaths {
  messages: string;
}

// API 配置
export const API_CONFIG: {
  development: ApiConfig;
  production: ApiConfig;
} = {
  development: {
    baseUrl: 'http://localhost:8085'
  },
  production: {
    baseUrl: 'https://api.yourdomain.com'
  }
};

// 根据环境获取当前配置
export const getApiConfig = (): ApiConfig => {
  return process.env.NODE_ENV === 'production' 
    ? API_CONFIG.production 
    : API_CONFIG.development;
};

// API 路径
export const API_PATHS: ApiPaths = {
  messages: '/api/messages'
};
