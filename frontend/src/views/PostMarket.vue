<template>
  <div class="post-market">
    <a-row :gutter="16">
      <!-- 监控状态概览 -->
      <a-col :span="24">
        <a-card title="监控状态概览" :bordered="false">
          <a-row :gutter="16">
            <a-col :span="6" v-for="country in monitoredCountries" :key="country.code">
              <a-card 
                :bordered="false" 
                :class="`country-card ${country.status}`"
                @click="showCountryDetail(country)"
              >
                <div class="country-info">
                  <div class="country-name">{{ country.name }}</div>
                  <div class="country-status">
                    <a-tag :color="getStatusColor(country.status)">
                      {{ getStatusText(country.status) }}
                    </a-tag>
                  </div>
                  <div class="country-stats">
                    <div class="stat-item">
                      <span class="stat-label">DAU:</span>
                      <span class="stat-value">{{ country.dau }}</span>
                    </div>
                    <div class="stat-item">
                      <span class="stat-label">投诉:</span>
                      <span class="stat-value">{{ country.complaints }}</span>
                    </div>
                  </div>
                </div>
              </a-card>
            </a-col>
          </a-row>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <!-- 实时监控数据 -->
      <a-col :span="12">
        <a-card title="DAU趋势" :bordered="false">
          <div class="chart-container">
            <div class="chart-placeholder">
              <a-empty description="DAU趋势图表待集成" />
            </div>
          </div>
        </a-card>
      </a-col>
      
      <a-col :span="12">
        <a-card title="投诉变化趋势" :bordered="false">
          <div class="chart-container">
            <div class="chart-placeholder">
              <a-empty description="投诉趋势图表待集成" />
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <a-row :gutter="16" style="margin-top: 16px;">
      <!-- 预警详情 -->
      <a-col :span="24">
        <a-card title="实时预警" :bordered="false">
          <a-table 
            :columns="warningColumns" 
            :data-source="warningData" 
            :pagination="{ pageSize: 10 }"
            size="small"
          >
            <template #bodyCell="{ column, record }">
              <template v-if="column.key === 'level'">
                <a-tag :color="getWarningColor(record.level)">
                  {{ record.level }}
                </a-tag>
              </template>
              <template v-else-if="column.key === 'action'">
                <a-space>
                  <a-button type="link" size="small" @click="viewWarningDetail(record)">
                    查看详情
                  </a-button>
                  <a-button type="link" size="small" @click="processWarning(record)">
                    处理
                  </a-button>
                </a-space>
              </template>
            </template>
          </a-table>
        </a-card>
      </a-col>
    </a-row>

    <!-- 国家详情模态框 -->
    <a-modal
      v-model:open="countryDetailVisible"
      :title="`${selectedCountry?.name}监控详情`"
      width="800px"
      :footer="null"
    >
      <div v-if="selectedCountry" class="country-detail">
        <a-row :gutter="16">
          <a-col :span="12">
            <a-statistic title="设备活跃度" :value="selectedCountry.dau" suffix="台">
              <template #suffix>
                <span :style="{ color: selectedCountry.dauChange >= 0 ? '#3f8600' : '#cf1322' }">
                  {{ selectedCountry.dauChange >= 0 ? '+' : '' }}{{ selectedCountry.dauChange }}%
                </span>
              </template>
            </a-statistic>
          </a-col>
          <a-col :span="12">
            <a-statistic title="投诉数量" :value="selectedCountry.complaints" suffix="件">
              <template #suffix>
                <span :style="{ color: selectedCountry.complaintChange >= 0 ? '#cf1322' : '#3f8600' }">
                  {{ selectedCountry.complaintChange >= 0 ? '+' : '' }}{{ selectedCountry.complaintChange }}%
                </span>
              </template>
            </a-statistic>
          </a-col>
        </a-row>
        
        <a-divider />
        
        <div class="detail-section">
          <h4>召回公告</h4>
          <a-list :data-source="selectedCountry.recalls" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>{{ item.title }}</template>
                  <template #description>{{ item.date }}</template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </div>
        
        <div class="detail-section">
          <h4>HS编码变更</h4>
          <a-list :data-source="selectedCountry.hsChanges" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #title>{{ item.oldCode }} → {{ item.newCode }}</template>
                  <template #description>{{ item.description }}</template>
                </a-list-item-meta>
              </a-list-item>
            </template>
          </a-list>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'

// 响应式数据
const countryDetailVisible = ref(false)
const selectedCountry = ref(null)

const monitoredCountries = ref([
  {
    code: 'US',
    name: '美国',
    status: 'normal',
    dau: 1250,
    complaints: 3,
    dauChange: -5,
    complaintChange: 0,
    recalls: [
      { title: 'FDA召回公告2024-001', date: '2024-01-15' }
    ],
    hsChanges: [
      { oldCode: '9018.90.00', newCode: '9018.90.10', description: '医疗器械分类更新' }
    ]
  },
  {
    code: 'EU',
    name: '欧盟',
    status: 'warning',
    dau: 980,
    complaints: 8,
    dauChange: -15,
    complaintChange: 25,
    recalls: [],
    hsChanges: [
      { oldCode: '9018.90.00', newCode: '9018.90.20', description: 'EUDAMED分类调整' }
    ]
  },
  {
    code: 'CN',
    name: '中国',
    status: 'high',
    dau: 2100,
    complaints: 15,
    dauChange: -20,
    complaintChange: 50,
    recalls: [
      { title: 'NMPA召回公告2024-003', date: '2024-01-10' }
    ],
    hsChanges: []
  },
  {
    code: 'JP',
    name: '日本',
    status: 'normal',
    dau: 650,
    complaints: 2,
    dauChange: 5,
    complaintChange: -10,
    recalls: [],
    hsChanges: []
  }
])

const warningColumns = [
  {
    title: '时间',
    dataIndex: 'time',
    key: 'time',
    width: 120
  },
  {
    title: '国家',
    dataIndex: 'country',
    key: 'country',
    width: 80
  },
  {
    title: '预警类型',
    dataIndex: 'type',
    key: 'type',
    width: 120
  },
  {
    title: '预警内容',
    dataIndex: 'content',
    key: 'content'
  },
  {
    title: '风险等级',
    dataIndex: 'level',
    key: 'level',
    width: 80
  },
  {
    title: '操作',
    key: 'action',
    width: 120
  }
]

const warningData = ref([
  {
    id: 1,
    time: '2024-01-15 14:30',
    country: '中国',
    type: 'DAU异常',
    content: '设备活跃度下降20%，超过预警阈值',
    level: 'high'
  },
  {
    id: 2,
    time: '2024-01-15 12:15',
    country: '欧盟',
    type: '投诉激增',
    content: '投诉数量增加25%，需要关注',
    level: 'medium'
  },
  {
    id: 3,
    time: '2024-01-15 10:00',
    country: '美国',
    type: '召回公告',
    content: 'FDA发布医疗器械召回公告',
    level: 'high'
  }
])

// 方法
const getStatusColor = (status: string) => {
  const colors = {
    normal: 'green',
    warning: 'orange',
    high: 'red'
  }
  return colors[status] || 'default'
}

const getStatusText = (status: string) => {
  const texts = {
    normal: '正常',
    warning: '预警',
    high: '高风险'
  }
  return texts[status] || '未知'
}

const getWarningColor = (level: string) => {
  const colors = {
    high: 'red',
    medium: 'orange',
    low: 'green'
  }
  return colors[level] || 'default'
}

const showCountryDetail = (country: any) => {
  selectedCountry.value = country
  countryDetailVisible.value = true
}

const viewWarningDetail = (warning: any) => {
  message.info(`查看预警详情: ${warning.content}`)
}

const processWarning = (warning: any) => {
  message.success(`预警已处理: ${warning.content}`)
}
</script>

<style scoped>
.post-market {
  padding: 24px;
}

.country-card {
  cursor: pointer;
  transition: all 0.3s;
  margin-bottom: 16px;
}

.country-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.country-card.normal {
  border-left: 4px solid #52c41a;
}

.country-card.warning {
  border-left: 4px solid #fa8c16;
}

.country-card.high {
  border-left: 4px solid #cf1322;
}

.country-info {
  text-align: center;
}

.country-name {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 8px;
}

.country-status {
  margin-bottom: 12px;
}

.country-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.stat-label {
  font-size: 12px;
  color: #666;
}

.stat-value {
  font-size: 16px;
  font-weight: bold;
  color: #1890ff;
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

.country-detail {
  padding: 16px 0;
}

.detail-section {
  margin-top: 16px;
}

.detail-section h4 {
  margin-bottom: 12px;
  color: #1890ff;
}
</style>