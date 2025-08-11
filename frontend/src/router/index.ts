import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    name: 'Layout',
    component: () => import('@/layouts/MainLayout.vue'),
    children: [
      {
        path: '',
        name: 'Dashboard',
        component: () => import('@/views/Dashboard.vue'),
        meta: { title: '首页' }
      },
      {
        path: '/pre-market',
        name: 'PreMarket',
        component: () => import('@/views/PreMarket.vue'),
        meta: { title: 'Pre-market风险评估' }
      },
      {
        path: '/post-market',
        name: 'PostMarket',
        component: () => import('@/views/PostMarket.vue'),
        meta: { title: 'Post-market动态监控' }
      },
      {
        path: '/data-management',
        name: 'DataManagement',
        component: () => import('@/views/DataManagement.vue'),
        meta: { title: '数据查询与管理' }
      },
      {
        path: '/warning-report',
        name: 'WarningReport',
        component: () => import('@/views/WarningReport.vue'),
        meta: { title: '预警与报告中心' }
      },
      {
        path:'/certificationDisplay',
        name: 'CertificationDisplay',
        component: () => import('@/components/CertificationDisplay.vue'),
        meta: { title: '认证展示' }
      },
      {
        path: '/riskareaMonitor',
        name: 'RiskAreaMonitor',
        component: () => import('@/components/RiskAreaMonitor.vue'),
        meta: { title: '风险区域监控' }
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router 