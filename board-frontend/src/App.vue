<script setup>
import { onMounted } from 'vue'
import { useStore } from 'vuex'

const store = useStore()

// 在组件挂载时检查登录状态
onMounted(() => {
  store.dispatch('checkAuth')
})
</script>

<template>
  <main>
    <router-view></router-view>
  </main>
</template>

<style>
/* 重置默认样式 */
* {
  margin: 0;
  padding: 0;
  box-sizing: border-box;
}

body {
  min-height: 100vh;
  background: #f5f5f5;
}
</style>

<style scoped>
main {
  min-height: 100vh;
  width: 100%;
  background: #f5f5f5;
}

@media (min-width: 1024px) {
  main {
    padding: 40px;
  }
}
</style>
