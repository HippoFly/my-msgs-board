# Message Board Frontend

基于 Vue 3 和 Vite 的留言板前端项目

## 必需工具

- Node.js (建议使用 v16 或更高版本)
- 包管理工具：
  - 推荐使用 pnpm (项目默认配置)
  - 也可以使用 npm 或 yarn (可能需要调整配置)

## 安装步骤

1. 克隆项目
```bash
git clone <repo-url>
cd board-frontend
```

2. 安装 pnpm
```bash
npm install -g pnpm
```

3. 安装项目依赖
```bash
pnpm install
```

## 开发环境

### 启动开发服务器
```bash
pnpm dev
```

### 构建生产版本
```bash
pnpm build
```

## 技术栈

- Vue 3
- Vite
- pnpm
- Axios (HTTP 客户端)

## 注意事项

- 虽然项目默认使用 pnpm，但其他包管理工具也可以使用
- 如果使用 npm 或 yarn，请注意可能需要调整依赖版本
- 建议使用 pnpm 以保持与项目维护者的一致性
- 项目依赖通过 `package.json` 管理，`pnpm-lock.yaml` 会自动生成
- 建议使用 VSCode + Volar 作为开发环境

## 包管理工具说明

### 为什么推荐使用 pnpm？
- 更好的依赖管理
- 更快的安装速度
- 更小的磁盘占用
- 更好的缓存机制

### 如果使用其他包管理工具
- 可能需要调整依赖版本
- 可能需要更新锁定文件
- 可能需要修改构建配置
- 请确保不影响其他开发者使用 pnpm

```sh
npm run dev
```

### Compile and Minify for Production

```sh
npm run build
```
