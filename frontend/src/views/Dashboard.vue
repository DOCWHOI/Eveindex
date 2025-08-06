<template>
  <div class="dashboard">
    <a-row :gutter="16">
      <!-- 全球风险概览 -->
      <a-col :span="16">
        <a-card title="全球风险概览" :bordered="false">
          <div class="map-container">
            <div class="world-map">
              <!-- 这里可以集成地图组件 -->
              <div class="map-placeholder">
                <a-empty description="地图组件待集成" />
              </div>
            </div>
          </div>
        </a-card>
      </a-col>
      
      <!-- TOP3 预警事件 -->
      <a-col :span="8">
        <a-card title="TOP3 预警事件" :bordered="false">
          <a-list :data-source="topWarnings" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>
                    <span :class="`warning-level-${item.level}`">{{ item.title }}</span>
                  </template>
                  <template #description>
                    <div>{{ item.country }} - {{ item.time }}</div>
                    <div class="warning-desc">{{ item.description }}</div>
                  </template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <!-- 本周风险趋势 -->
      <a-col :span="24">
        <a-card title="本周风险趋势" :bordered="false">
          <div class="chart-container">
            <div class="chart-placeholder">
              <a-empty description="图表组件待集成" />
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <!-- 快速统计 -->
      <a-col :span="6">
        <a-card :bordered="false">
          <a-statistic title="今日预警" :value="todayWarnings" :value-style="{ color: '#cf1322' }">
            <template #suffix>
              <warning-outlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false">
          <a-statistic title="本周评估" :value="weeklyAssessments" :value-style="{ color: '#3f8600' }">
            <template #suffix>
              <safety-outlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false">
          <a-statistic title="监控国家" :value="monitoredCountries" :value-style="{ color: '#1890ff' }">
            <template #suffix>
              <global-outlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card :bordered="false">
          <a-statistic title="数据源" :value="dataSources" :value-style="{ color: '#722ed1' }">
            <template #suffix>
              <database-outlined />
            </template>
          </a-statistic>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { 
  WarningOutlined, 
  SafetyOutlined, 
  GlobalOutlined, 
  DatabaseOutlined 
} from '@ant-design/icons-vue'

// 响应式数据
const topWarnings = ref([
  {
    id: 1,
    title: 'FDA召回公告',
    country: '美国',
    time: '2小时前',
    level: 'high',
    description: '医疗器械召回，影响范围较大'
  },
  {
    id: 2,
    title: 'HS编码变更',
    country: '欧盟',
    time: '4小时前',
    level: 'medium',
    description: '医疗器械分类标准更新'
  },
  {
    id: 3,
    title: 'DAU异常下降',
    country: '中国',
    time: '6小时前',
    level: 'low',
    description: '设备活跃度下降20%'
  }
])

const todayWarnings = ref(12)
const weeklyAssessments = ref(156)
const monitoredCountries = ref(8)
const dataSources = ref(15)

// 生命周期
onMounted(() => {
  // 初始化仪表盘数据
  console.log('Dashboard mounted')
})
</script>

<style scoped>
.dashboard {
  padding: 24px;
}

.map-container {
  height: 400px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.map-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}

.chart-container {
  height: 300px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.chart-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
  border: 1px dashed #d9d9d9;
  border-radius: 6px;
}

.warning-level-high {
  color: #cf1322;
  font-weight: bold;
}

.warning-level-medium {
  color: #fa8c16;
  font-weight: bold;
}

.warning-level-low {
  color: #52c41a;
  font-weight: bold;
}

.warning-desc {
  font-size: 12px;
  color: #666;
  margin-top: 4px;
}
</style>