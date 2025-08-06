<template>
  <div class="warning-report">
    <!-- 预警统计 -->
    <a-row :gutter="24">
      <a-col :span="6" v-for="stat in warningStats" :key="stat.title">
        <a-card class="stat-card">
          <div class="stat-content">
            <div class="stat-icon" :style="{ backgroundColor: stat.color }">
              <component :is="stat.icon" />
            </div>
            <div class="stat-info">
              <div class="stat-number">{{ stat.value }}</div>
              <div class="stat-title">{{ stat.title }}</div>
            </div>
          </div>
        </a-card>
      </a-col>
    </a-row>

    <!-- 预警记录 -->
    <a-card title="预警记录" :bordered="false" style="margin-top: 24px">
      <a-table 
        :columns="warningColumns" 
        :data-source="warningRecords" 
        :pagination="{ pageSize: 10 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'warningType'">
            <a-tag :color="getWarningTypeColor(record.warningType)">
              {{ record.warningType }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'status'">
            <a-tag :color="getStatusColor(record.status)">
              {{ record.status }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="viewWarningDetail(record)">
              查看详情
            </a-button>
            <a-button 
              v-if="record.status === '未处理'" 
              type="link" 
              size="small" 
              @click="markAsProcessed(record)"
            >
              标记已处理
            </a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 报告生成 -->
    <a-card title="报告生成" :bordered="false" style="margin-top: 24px">
      <a-row :gutter="24">
        <a-col :span="12">
          <h3>自动报告</h3>
          <a-list :data-source="autoReports" size="small">
            <template #renderItem="{ item }">
              <a-list-item>
                <a-list-item-meta>
                  <template #avatar>
                    <a-avatar :style="{ backgroundColor: '#1890ff' }">
                      <file-text-outlined />
                    </a-avatar>
                  </template>
                  <template #title>
                    <span>{{ item.title }}</span>
                  </template>
                  <template #description>
                    <div>{{ item.description }}</div>
                    <div>下次生成: {{ item.nextGenerate }}</div>
                  </template>
                </a-list-item-meta>
                <template #actions>
                  <a-button type="link" size="small" @click="generateReport(item)">
                    立即生成
                  </a-button>
                </template>
              </a-list-item>
            </template>
          </a-list>
        </a-col>

        <a-col :span="12">
          <h3>自定义报告</h3>
          <a-form layout="vertical" @finish="handleCustomReport">
            <a-form-item label="报告类型">
              <a-select
                v-model:value="customReport.type"
                placeholder="选择报告类型"
                :options="reportTypes"
              />
            </a-form-item>
            <a-form-item label="时间范围">
              <a-range-picker
                v-model:value="customReport.dateRange"
                style="width: 100%"
              />
            </a-form-item>
            <a-form-item label="国家">
              <a-select
                v-model:value="customReport.countries"
                placeholder="选择国家"
                mode="multiple"
                :options="countries"
                allow-clear
              />
            </a-form-item>
            <a-form-item>
              <a-button type="primary" html-type="submit" :loading="generating">
                生成报告
              </a-button>
            </a-form-item>
          </a-form>
        </a-col>
      </a-row>
    </a-card>

    <!-- 邮件通知记录 -->
    <a-card title="邮件通知记录" :bordered="false" style="margin-top: 24px">
      <a-table 
        :columns="emailColumns" 
        :data-source="emailRecords" 
        :pagination="{ pageSize: 10 }"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === '成功' ? 'green' : 'red'">
              {{ record.status }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="resendEmail(record)">
              重新发送
            </a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 预警详情弹窗 -->
    <a-modal
      v-model:open="detailModalVisible"
      title="预警详情"
      width="800px"
      :footer="null"
    >
      <div v-if="selectedWarning">
        <a-descriptions :column="2" bordered>
          <a-descriptions-item label="预警类型">
            <a-tag :color="getWarningTypeColor(selectedWarning.warningType)">
              {{ selectedWarning.warningType }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="触发国家">
            {{ selectedWarning.country }}
          </a-descriptions-item>
          <a-descriptions-item label="触发时间">
            {{ selectedWarning.triggerTime }}
          </a-descriptions-item>
          <a-descriptions-item label="处理状态">
            <a-tag :color="getStatusColor(selectedWarning.status)">
              {{ selectedWarning.status }}
            </a-tag>
          </a-descriptions-item>
          <a-descriptions-item label="触发值">
            {{ selectedWarning.triggerValue }}
          </a-descriptions-item>
          <a-descriptions-item label="阈值">
            {{ selectedWarning.threshold }}
          </a-descriptions-item>
        </a-descriptions>

        <a-divider />

        <h4>预警描述</h4>
        <p>{{ selectedWarning.description }}</p>

        <a-divider />

        <h4>处理记录</h4>
        <a-timeline>
          <a-timeline-item v-for="log in selectedWarning.processLogs" :key="log.time">
            <template #dot>
              <a-avatar size="small" :style="{ backgroundColor: '#1890ff' }">
                <user-outlined />
              </a-avatar>
            </template>
            <div>
              <div><strong>{{ log.action }}</strong></div>
              <div>{{ log.time }}</div>
              <div>{{ log.operator }}</div>
              <div v-if="log.comment">{{ log.comment }}</div>
            </div>
          </a-timeline-item>
        </a-timeline>
      </div>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import {
  WarningOutlined,
  FileTextOutlined,
  MailOutlined,
  ClockCircleOutlined,
  UserOutlined
} from '@ant-design/icons-vue'

// 预警统计数据
const warningStats = ref([
  {
    title: '今日预警',
    value: '12',
    icon: WarningOutlined,
    color: '#ff4d4f'
  },
  {
    title: '未处理',
    value: '5',
    icon: ClockCircleOutlined,
    color: '#faad14'
  },
  {
    title: '已处理',
    value: '156',
    icon: FileTextOutlined,
    color: '#52c41a'
  },
  {
    title: '邮件通知',
    value: '89',
    icon: MailOutlined,
    color: '#1890ff'
  }
])

// 预警记录表格列定义
const warningColumns = [
  {
    title: '触发时间',
    dataIndex: 'triggerTime',
    key: 'triggerTime'
  },
  {
    title: '国家',
    dataIndex: 'country',
    key: 'country'
  },
  {
    title: '预警类型',
    dataIndex: 'warningType',
    key: 'warningType'
  },
  {
    title: '触发值',
    dataIndex: 'triggerValue',
    key: 'triggerValue'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '操作',
    key: 'action'
  }
]

// 预警记录数据
const warningRecords = ref([
  {
    triggerTime: '2024-01-15 14:30',
    country: '美国',
    warningType: 'HS编码异动',
    triggerValue: 'DAU下降20%',
    status: '未处理'
  },
  {
    triggerTime: '2024-01-15 13:15',
    country: '欧盟',
    warningType: '召回公告',
    triggerValue: '新增召回3起',
    status: '已处理'
  },
  {
    triggerTime: '2024-01-15 12:00',
    country: '中国',
    warningType: '投诉增加',
    triggerValue: '投诉量增加50%',
    status: '未处理'
  }
])

// 自动报告数据
const autoReports = ref([
  {
    title: '周报',
    description: '每周风险监控报告',
    nextGenerate: '2024-01-22 09:00'
  },
  {
    title: '月报',
    description: '月度风险评估报告',
    nextGenerate: '2024-02-01 09:00'
  },
  {
    title: '季度报告',
    description: '季度风险趋势分析',
    nextGenerate: '2024-04-01 09:00'
  }
])

// 自定义报告表单
const customReport = reactive({
  type: '',
  dateRange: [],
  countries: []
})

// 报告类型选项
const reportTypes = [
  { value: 'risk', label: '风险评估报告' },
  { value: 'warning', label: '预警分析报告' },
  { value: 'trend', label: '趋势分析报告' },
  { value: 'comparison', label: '对比分析报告' }
]

// 国家选项
const countries = [
  { value: 'US', label: '美国' },
  { value: 'EU', label: '欧盟' },
  { value: 'CN', label: '中国' },
  { value: 'JP', label: '日本' },
  { value: 'KR', label: '韩国' },
  { value: 'AU', label: '澳大利亚' },
  { value: 'BR', label: '巴西' }
]

// 邮件记录表格列定义
const emailColumns = [
  {
    title: '发送时间',
    dataIndex: 'sendTime',
    key: 'sendTime'
  },
  {
    title: '收件人',
    dataIndex: 'recipient',
    key: 'recipient'
  },
  {
    title: '主题',
    dataIndex: 'subject',
    key: 'subject'
  },
  {
    title: '状态',
    dataIndex: 'status',
    key: 'status'
  },
  {
    title: '操作',
    key: 'action'
  }
]

// 邮件记录数据
const emailRecords = ref([
  {
    sendTime: '2024-01-15 14:30',
    recipient: 'regulatory@company.com',
    subject: '美国HS编码异动预警',
    status: '成功'
  },
  {
    sendTime: '2024-01-15 13:15',
    recipient: 'legal@company.com',
    subject: '欧盟召回公告通知',
    status: '成功'
  },
  {
    sendTime: '2024-01-15 12:00',
    recipient: 'operations@company.com',
    subject: '中国投诉增加预警',
    status: '失败'
  }
])

// 弹窗状态
const detailModalVisible = ref(false)
const selectedWarning = ref<any>(null)
const generating = ref(false)

// 获取预警类型颜色
const getWarningTypeColor = (type: string) => {
  const colorMap: Record<string, string> = {
    'HS编码异动': 'blue',
    '召回公告': 'red',
    '投诉增加': 'orange',
    'DAU下降': 'purple'
  }
  return colorMap[type] || 'default'
}

// 获取状态颜色
const getStatusColor = (status: string) => {
  const colorMap: Record<string, string> = {
    '未处理': 'orange',
    '已处理': 'green',
    '处理中': 'blue'
  }
  return colorMap[status] || 'default'
}

// 查看预警详情
const viewWarningDetail = (record: any) => {
  selectedWarning.value = {
    ...record,
    threshold: 'DAU下降15%',
    description: '检测到设备日活跃用户显著下降，可能影响产品市场表现。',
    processLogs: [
      {
        action: '系统自动触发预警',
        time: '2024-01-15 14:30',
        operator: '系统',
        comment: 'DAU下降超过阈值'
      },
      {
        action: '邮件通知发送',
        time: '2024-01-15 14:31',
        operator: '系统',
        comment: '已发送预警邮件'
      }
    ]
  }
  detailModalVisible.value = true
}

// 标记为已处理
const markAsProcessed = (record: any) => {
  record.status = '已处理'
  message.success('已标记为处理完成')
}

// 生成报告
const generateReport = (report: any) => {
  message.success(`${report.title} 生成成功`)
}

// 自定义报告提交
const handleCustomReport = async () => {
  try {
    generating.value = true
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))
    message.success('自定义报告生成成功')
  } catch (error) {
    message.error('报告生成失败')
  } finally {
    generating.value = false
  }
}

// 重新发送邮件
const resendEmail = (record: any) => {
  record.status = '成功'
  message.success('邮件重新发送成功')
}
</script>

<style scoped>
.warning-report {
  padding: 0;
}

.stat-card {
  margin-bottom: 24px;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
}

.stat-icon {
  width: 48px;
  height: 48px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 24px;
}

.stat-info {
  flex: 1;
}

.stat-number {
  font-size: 24px;
  font-weight: bold;
  color: #262626;
}

.stat-title {
  font-size: 14px;
  color: #8c8c8c;
  margin-top: 4px;
}
</style> 