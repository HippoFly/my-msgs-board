<template>
  <el-container class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside width="200px">
      <el-menu
        :default-active="activeMenu"
        class="admin-menu"
        @select="handleSelect"
      >
        <el-menu-item index="dashboard">
          <el-icon><DataLine /></el-icon>
          <span>仪表盘</span>
        </el-menu-item>
        <el-menu-item index="messages">
          <el-icon><ChatDotRound /></el-icon>
          <span>留言管理</span>
        </el-menu-item>
        <el-menu-item index="users">
          <el-icon><User /></el-icon>
          <span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="settings">
          <el-icon><Setting /></el-icon>
          <span>系统设置</span>
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 主要内容区 -->
    <el-container class="main-container">
      <!-- 顶部导航 -->
      <el-header>
        <div class="header-content">
          <div class="breadcrumb">
            <el-breadcrumb>
              <el-breadcrumb-item>首页</el-breadcrumb-item>
              <el-breadcrumb-item>{{ menuTitles[activeMenu] }}</el-breadcrumb-item>
            </el-breadcrumb>
          </div>
          <div class="user-info">
            <el-dropdown @command="handleCommand">
              <span class="el-dropdown-link">
                <el-avatar :size="32" :icon="UserFilled" />
                {{ currentUser?.username }}
                <el-icon class="el-icon--right"><arrow-down /></el-icon>
              </span>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item command="profile">个人信息</el-dropdown-item>
                  <el-dropdown-item command="logout" divided>退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </div>
        </div>
      </el-header>

      <!-- 内容区 -->
      <el-main>
        <!-- 仪表盘 -->
        <div v-if="activeMenu === 'dashboard'" class="dashboard">
          <el-row :gutter="20">
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>总留言数</span>
                    <el-icon><ChatDotRound /></el-icon>
                  </div>
                </template>
                <div class="card-value">{{ statistics.totalMessages }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>今日新增</span>
                    <el-icon><Timer /></el-icon>
                  </div>
                </template>
                <div class="card-value">{{ statistics.todayMessages }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>待审核</span>
                    <el-icon><Warning /></el-icon>
                  </div>
                </template>
                <div class="card-value">{{ statistics.pendingMessages }}</div>
              </el-card>
            </el-col>
            <el-col :span="6">
              <el-card shadow="hover">
                <template #header>
                  <div class="card-header">
                    <span>用户数</span>
                    <el-icon><User /></el-icon>
                  </div>
                </template>
                <div class="card-value">{{ statistics.totalUsers }}</div>
              </el-card>
            </el-col>
          </el-row>
        </div>

        <!-- 留言管理 -->
        <div v-if="activeMenu === 'messages'" class="messages">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>留言列表</span>
                <el-button type="primary" @click="refreshMessages">
                  <el-icon><Refresh /></el-icon>刷新
                </el-button>
              </div>
            </template>
            
            <!-- 搜索栏 -->
            <el-form :inline="true" class="search-form">
              <el-form-item label="关键词">
                <el-input v-model="searchQuery.keyword" placeholder="搜索内容/昵称" />
              </el-form-item>
              <el-form-item label="状态">
                <el-select v-model="searchQuery.status" placeholder="选择状态">
                  <el-option label="全部" value="" />
                  <el-option label="显示" value="visible" />
                  <el-option label="隐藏" value="hidden" />
                </el-select>
              </el-form-item>
              <el-form-item>
                <el-button type="primary" @click="searchMessages">搜索</el-button>
                <el-button @click="resetSearch">重置</el-button>
              </el-form-item>
            </el-form>

            <!-- 留言表格 -->
            <el-table :data="messages" style="width: 100%" v-loading="loading">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="content" label="内容" show-overflow-tooltip />
              <el-table-column prop="nickname" label="昵称" width="120">
                <template #default="scope">
                  {{ scope.row.nickname || '匿名' }}
                </template>
              </el-table-column>
              <el-table-column prop="ip" label="IP" width="140" />
              <el-table-column prop="time" label="时间" width="180">
                <template #default="scope">
                  {{ formatTime(scope.row.time) }}
                </template>
              </el-table-column>
              <el-table-column prop="visible" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.visible ? 'success' : 'danger'">
                    {{ scope.row.visible ? '显示' : '隐藏' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column label="操作" width="150" fixed="right">
                <template #default="scope">
                  <el-button
                    v-if="scope.row.visible"
                    type="danger"
                    size="small"
                    @click="hideMessage(scope.row.id)"
                  >
                    隐藏
                  </el-button>
                  <el-button
                    v-else
                    type="success"
                    size="small"
                    @click="showMessage(scope.row.id)"
                  >
                    显示
                  </el-button>
                  <el-button
                    type="danger"
                    size="small"
                    @click="deleteMessage(scope.row.id)"
                  >
                    删除
                  </el-button>
                </template>
              </el-table-column>
            </el-table>

            <!-- 分页 -->
            <div class="pagination">
              <el-pagination
                v-model:current-page="currentPage"
                v-model:page-size="pageSize"
                :page-sizes="[10, 20, 50, 100]"
                layout="total, sizes, prev, pager, next, jumper"
                :total="total"
                @size-change="handleSizeChange"
                @current-change="handleCurrentChange"
              />
            </div>
          </el-card>
        </div>

        <!-- 用户管理 -->
        <div v-if="activeMenu === 'users'" class="users">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>用户管理</span>
                <el-button type="primary">
                  <el-icon><Plus /></el-icon>添加用户
                </el-button>
              </div>
            </template>

            <!-- 用户列表 -->
            <el-table :data="users" style="width: 100%" v-loading="loading">
              <el-table-column prop="id" label="ID" width="80" />
              <el-table-column prop="username" label="用户名" width="120" />
              <el-table-column prop="email" label="邮箱" width="180" />
              <el-table-column prop="role" label="角色" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.role === 'ADMIN' ? 'danger' : 'info'">
                    {{ scope.row.role }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="active" label="状态" width="100">
                <template #default="scope">
                  <el-tag :type="scope.row.active ? 'success' : 'danger'">
                    {{ scope.row.active ? '启用' : '禁用' }}
                  </el-tag>
                </template>
              </el-table-column>
              <el-table-column prop="createdAt" label="创建时间" width="180">
                <template #default="scope">
                  {{ formatTime(scope.row.createdAt) }}
                </template>
              </el-table-column>
              <el-table-column label="操作" width="200" fixed="right">
                <template #default="scope">
                  <el-button
                    :type="scope.row.active ? 'danger' : 'success'"
                    size="small"
                    @click="handleUserStatusChange(scope.row)"
                  >
                    {{ scope.row.active ? '禁用' : '启用' }}
                  </el-button>
                  <el-button
                    type="primary"
                    size="small"
                    @click="handleEditUser(scope.row)"
                  >
                    编辑
                  </el-button>
                </template>
              </el-table-column>
            </el-table>
          </el-card>
        </div>

        <!-- 系统设置 -->
        <div v-if="activeMenu === 'settings'" class="settings">
          <el-card>
            <template #header>
              <div class="card-header">
                <span>系统设置</span>
              </div>
            </template>
            <el-empty description="系统设置功能开发中...">
              <template #image>
                <el-icon :size="60"><Setting /></el-icon>
              </template>
            </el-empty>
          </el-card>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import { ref, computed, onMounted } from 'vue'
import { useStore } from 'vuex'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { apiService } from '../utils/api'
import {
  User,
  UserFilled,
  Setting,
  ChatDotRound,
  DataLine,
  Timer,
  Warning,
  Refresh,
  Plus
} from '@element-plus/icons-vue'

export default {
  name: 'AdminView',
  setup() {
    const store = useStore()
    const router = useRouter()
    const currentUser = computed(() => store.getters.currentUser)

    // 如果不是管理员，重定向到首页
    if (!store.getters.isLoggedIn || currentUser.value?.role !== 'ADMIN') {
      router.push('/')
      return
    }

    return {
      currentUser,
      User,
      UserFilled,
      Setting,
      ChatDotRound,
      DataLine,
      Timer,
      Warning,
      Refresh,
      Plus
    }
  },
  data() {
    return {
      activeMenu: 'dashboard',
      menuTitles: {
        dashboard: '仪表盘',
        messages: '留言管理',
        users: '用户管理',
        settings: '系统设置'
      },
      messages: [],
      loading: false,
      currentPage: 1,
      pageSize: 10,
      total: 0,
      searchQuery: {
        keyword: '',
        status: ''
      },
      statistics: {
        totalMessages: 0,
        todayMessages: 0,
        pendingMessages: 0,
        totalUsers: 0
      },
      users: []
    }
  },
  methods: {
    formatTime(time) {
      return new Date(time).toLocaleString()
    },
    handleSelect(key) {
      this.activeMenu = key
      if (key === 'users') {
        this.loadUsers()
      }
    },
    handleCommand(command) {
      if (command === 'logout') {
        this.handleLogout()
      } else if (command === 'profile') {
        ElMessage.info('个人信息功能开发中...')
      }
    },
    async handleLogout() {
      try {
        await ElMessageBox.confirm(
          '确定要退出登录吗？',
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        this.$store.dispatch('logout')
        this.$router.push('/')
      } catch {
        // 用户取消退出
      }
    },
    async loadMessages() {
      this.loading = true;
      try {
        const response = await apiService.getAllMessages(); // 使用 getAllMessages 而不是 getMessages
        this.messages = response.data;
        this.total = response.data.length; // 实际项目中应该从后端获取总数
      } catch (error) {
        ElMessage.error('加载留言失败');
        console.error('加载留言失败:', error);
      } finally {
        this.loading = false;
      }
    },
    async hideMessage(id) {
      try {
        await apiService.hideMessage(id)
        ElMessage.success('留言已隐藏')
        await this.loadMessages()
      } catch (error) {
        ElMessage.error('操作失败')
        console.error('隐藏留言失败:', error)
      }
    },
    async showMessage(id) {
      try {
        await apiService.showMessage(id)
        ElMessage.success('留言已显示')
        await this.loadMessages()
      } catch (error) {
        ElMessage.error('操作失败')
        console.error('显示留言失败:', error)
      }
    },
    async deleteMessage(id) {
      try {
        await ElMessageBox.confirm(
          '确定要删除这条留言吗？此操作不可恢复！',
          '警告',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        )
        // TODO: 实现删除API
        ElMessage.success('留言已删除')
        await this.loadMessages()
      } catch {
        // 用户取消删除
      }
    },
    refreshMessages() {
      this.loadMessages()
    },
    searchMessages() {
      // TODO: 实现搜索功能
      this.loadMessages()
    },
    resetSearch() {
      this.searchQuery = {
        keyword: '',
        status: ''
      }
      this.loadMessages()
    },
    handleSizeChange(val) {
      this.pageSize = val
      this.loadMessages()
    },
    handleCurrentChange(val) {
      this.currentPage = val
      this.loadMessages()
    },
    async loadStatistics() {
      // TODO: 实现统计API
      // 临时模拟数据
      this.statistics = {
        totalMessages: this.messages.length,
        todayMessages: Math.floor(Math.random() * 10),
        pendingMessages: Math.floor(Math.random() * 5),
        totalUsers: Math.floor(Math.random() * 100)
      }
    },
    async loadUsers() {
      this.loading = true;
      try {
        const response = await apiService.getAllUsers();
        this.users = response.data;
      } catch (error) {
        ElMessage.error('加载用户列表失败');
        console.error('加载用户列表失败:', error);
      } finally {
        this.loading = false;
      }
    },
    async handleUserStatusChange(user) {
      try {
        await ElMessageBox.confirm(
          `确定要${user.active ? '禁用' : '启用'}该用户吗？`,
          '提示',
          {
            confirmButtonText: '确定',
            cancelButtonText: '取消',
            type: 'warning'
          }
        );
        await apiService.updateUserStatus(user.id, !user.active);
        ElMessage.success(`${user.active ? '禁用' : '启用'}成功`);
        await this.loadUsers();
      } catch (error) {
        if (error !== 'cancel') {
          ElMessage.error('操作失败');
          console.error('操作失败:', error);
        }
      }
    },
    handleEditUser(user) {
      ElMessage.info('用户编辑功能开发中...');
    }
  },
  mounted() {
    this.loadMessages()
    this.loadStatistics()
    this.loadUsers()
  }
}
</script>

<style scoped>
.admin-layout {
  height: 100vh;
  width: 100vw;
  display: flex;
  overflow-x: hidden; /* 防止横向滚动 */
}

.main-container {
  flex: 1;
  min-width: 1000px;
  max-width: 1400px;
  width: 100%;
  margin: 0; /* 移除自动边距 */
}

.admin-menu {
  height: 100%;
  border-right: none;
}

.el-header {
  background-color: white;
  border-bottom: 1px solid #e6e6e6;
  padding: 0 20px;
}

.header-content {
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.el-aside {
  flex-shrink: 0; /* 防止侧边栏被压缩 */
  background-color: #304156;
  color: white;
}

.el-menu {
  border-right: none;
  background-color: #304156;
}

.el-menu-item {
  color: #bfcbd9;
}

.el-menu-item:hover {
  color: white;
}

.el-menu-item.is-active {
  color: #409EFF;
  background-color: #263445;
}

.user-info {
  display: flex;
  align-items: center;
}

.el-dropdown-link {
  display: flex;
  align-items: center;
  cursor: pointer;
  color: #606266;
}

.el-dropdown-link:hover {
  color: #409EFF;
}

.dashboard .el-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.card-value {
  font-size: 24px;
  font-weight: bold;
  color: #409EFF;
  text-align: center;
  margin-top: 10px;
}

.search-form {
  margin-bottom: 20px;
}

.pagination {
  margin-top: 20px;
  display: flex;
  justify-content: flex-end;
}

.breadcrumb {
  font-size: 14px;
}

:deep(.el-menu-item) {
  display: flex;
  align-items: center;
}

:deep(.el-menu-item .el-icon) {
  margin-right: 8px;
}

.el-main {
  padding: 20px;
  background-color: #f0f2f5;
  width: 100%;
  box-sizing: border-box;
}

.el-card {
  margin-bottom: 20px;
}

.users .el-empty,
.settings .el-empty {
  padding: 60px 0;
}

.dashboard .el-row {
  width: 100%;
  margin: 0;
}

.messages .el-card,
.users .el-card,
.settings .el-card {
  width: 100%;
  min-width: 800px;
}
</style> 