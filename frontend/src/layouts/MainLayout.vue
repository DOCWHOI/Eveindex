<template>
  <a-layout class="main-layout">
    <a-layout-sider v-model:collapsed="collapsed" collapsible
                    :style="{ overflow: 'auto', height: '100vh' }">
      <div class="logo">
        <h2>风险监控系统</h2>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        theme="dark"
        mode="inline"
        @click="handleMenuClick"
      >
        <a-menu-item key="dashboard">
          <dashboard-outlined />
          <span>首页</span>
        </a-menu-item>
        <a-menu-item key="pre-market">
          <safety-outlined />
          <span>Pre-market风险评估</span>
        </a-menu-item>
        <a-menu-item key="post-market">
          <monitor-outlined />
          <span>Post-market动态监控</span>
        </a-menu-item>
        <a-menu-item key="data-management">
          <database-outlined />
          <span>数据查询与管理</span>
        </a-menu-item>
        <a-menu-item key="warning-report">
          <warning-outlined />
          <span>预警与报告中心</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="header">
        <div class="header-content">
          <h1>{{ currentPageTitle }}</h1>
          <div class="user-info">
            <a-avatar icon="user" />
            <span>管理员</span>
          </div>
        </div>
      </a-layout-header>
      <a-layout-content class="content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, computed } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import {
  DashboardOutlined,
  SafetyOutlined,
  MonitorOutlined,
  DatabaseOutlined,
  WarningOutlined
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()

const collapsed = ref(false)
const selectedKeys = ref<string[]>(['dashboard'])

const currentPageTitle = computed(() => {
  return route.meta?.title || '首页'
})

const handleMenuClick = ({ key }: { key: string }) => {
  const routeMap: Record<string, string> = {
    dashboard: '/',
    'pre-market': '/pre-market',
    'post-market': '/post-market',
    'data-management': '/data-management',
    'warning-report': '/warning-report'
  }
  
  if (routeMap[key]) {
    router.push(routeMap[key])
  }
}

// 根据当前路由设置选中的菜单项
const updateSelectedKeys = () => {
  const path = route.path
  if (path === '/') {
    selectedKeys.value = ['dashboard']
  } else {
    const key = path.substring(1)
    selectedKeys.value = [key]
  }
}

// 监听路由变化
import { watch } from 'vue'
watch(() => route.path, updateSelectedKeys, { immediate: true })
</script>

<style scoped>
.main-layout {
  height: 100vh;
  background: #cf1322;
  display: flex;
}

.logo {
  height: 64px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  background: rgba(255, 255, 255, 0.1);
}

.logo h2 {
  color: white;
  margin: 0;
  font-size: 16px;
}

.header {
  background: #fff;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
}

.header-content {
  margin-left: 200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 100%;
  box-shadow: 0 2px 8px rgba(0,0,0,0.12);
}

.header-content h1 {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 8px;
}

.content {
  //margin: 24px;
  margin-top: 24px;
  margin-left: 150px;
  padding: 24px;
  background: #fff;
  border-radius: 6px;
  min-height: calc(100vh - 112px);
}

:deep(.ant-layout-sider) {
  position: fixed;
  left: 0;
  top: 0;
  z-index: 100;
  width: 100px;
}
</style>
