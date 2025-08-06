<template>
  <div class="pre-market">
    <a-card title="Pre-market风险评估" :bordered="false">
      <a-form
        :model="formData"
        :rules="rules"
        ref="formRef"
        layout="vertical"
        @finish="handleSubmit"
      >
        <a-row :gutter="24">
          <!-- 国家选择 -->
          <a-col :span="8">
            <a-form-item label="选择国家" name="country">
              <a-select
                v-model:value="formData.country"
                placeholder="请选择国家"
                show-search
                :options="countries"
                @change="handleCountryChange"
              />
            </a-form-item>
          </a-col>

          <!-- 竞品名称 -->
          <a-col :span="8">
            <a-form-item label="竞品名称" name="competitor">
              <a-input
                v-model:value="formData.competitor"
                placeholder="请输入竞品名称"
                @change="handleCompetitorChange"
              />
            </a-form-item>
          </a-col>

          <!-- HS编码 -->
          <a-col :span="8">
            <a-form-item label="HS编码" name="hsCode">
              <a-input
                v-model:value="formData.hsCode"
                placeholder="请输入HS编码"
                @change="handleHsCodeChange"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <!-- 法规关键词 -->
          <a-col :span="12">
            <a-form-item label="法规关键词" name="regulatoryKeywords">
              <a-textarea
                v-model:value="formData.regulatoryKeywords"
                placeholder="请输入法规关键词，多个关键词用逗号分隔"
                :rows="3"
                @change="handleRegulatoryChange"
              />
            </a-form-item>
          </a-col>

          <!-- 违规记录 -->
          <a-col :span="12">
            <a-form-item label="违规记录" name="violationRecords">
              <a-textarea
                v-model:value="formData.violationRecords"
                placeholder="请输入违规记录信息"
                :rows="3"
                @change="handleViolationChange"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="24">
            <a-form-item>
              <a-button type="primary" html-type="submit" :loading="calculating">
                计算风险评分
              </a-button>
              <a-button style="margin-left: 8px" @click="handleReset">
                重置
              </a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <!-- 风险评估结果 -->
    <a-card title="风险评估结果" :bordered="false" style="margin-top: 24px" v-if="showResult">
      <a-row :gutter="24">
        <!-- 评分详情 -->
        <a-col :span="12">
          <h3>评分详情</h3>
          <a-progress
            :percent="result.competitorScore"
            :stroke-color="getScoreColor(result.competitorScore)"
            :format="(percent) => `竞品注册分: ${percent}%`"
            style="margin-bottom: 16px"
          />
          <a-progress
            :percent="result.hsCodeScore"
            :stroke-color="getScoreColor(result.hsCodeScore)"
            :format="(percent) => `HS编码分: ${percent}%`"
            style="margin-bottom: 16px"
          />
          <a-progress
            :percent="result.regulatoryScore"
            :stroke-color="getScoreColor(result.regulatoryScore)"
            :format="(percent) => `法规语义分: ${percent}%`"
            style="margin-bottom: 16px"
          />
          <a-progress
            :percent="result.violationScore"
            :stroke-color="getScoreColor(result.violationScore)"
            :format="(percent) => `违规记录分: ${percent}%`"
            style="margin-bottom: 16px"
          />
          <a-divider />
          <a-progress
            :percent="result.totalScore"
            :stroke-color="getRiskLevelColor(result.riskLevel)"
            :format="(percent) => `总风险分: ${percent}%`"
            :stroke-width="20"
          />
        </a-col>

        <!-- 风险等级和决策建议 -->
        <a-col :span="12">
          <h3>风险等级</h3>
          <a-alert
            :message="result.riskLevel"
            :type="getAlertType(result.riskLevel)"
            show-icon
            style="margin-bottom: 16px"
          />
          
          <h3>决策建议</h3>
          <a-card :bordered="false" :body-style="{ backgroundColor: getDecisionColor(result.decision) }">
            <h4>{{ result.decision }}</h4>
            <p>{{ getDecisionDescription(result.decision) }}</p>
          </a-card>

          <div style="margin-top: 16px">
            <a-button type="primary" @click="handleSave">
              保存评估结果
            </a-button>
            <a-button style="margin-left: 8px" @click="handleExport">
              导出报告
            </a-button>
          </div>
        </a-col>
      </a-row>
    </a-card>

    <!-- 历史评估记录 -->
    <a-card title="历史评估记录" :bordered="false" style="margin-top: 24px">
      <a-table :columns="historyColumns" :data-source="historyData" :pagination="{ pageSize: 10 }">
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'riskLevel'">
            <a-tag :color="getRiskLevelColor(record.riskLevel)">
              {{ record.riskLevel }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="viewHistoryDetail(record)">
              查看详情
            </a-button>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'

// 表单数据
const formData = reactive({
  country: '',
  competitor: '',
  hsCode: '',
  regulatoryKeywords: '',
  violationRecords: ''
})

// 表单验证规则
const rules = {
  country: [{ required: true, message: '请选择国家' }],
  competitor: [{ required: true, message: '请输入竞品名称' }],
  hsCode: [{ required: true, message: '请输入HS编码' }]
}

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

// 计算状态
const calculating = ref(false)
const showResult = ref(false)

// 评估结果
const result = reactive({
  competitorScore: 0,
  hsCodeScore: 0,
  regulatoryScore: 0,
  violationScore: 0,
  totalScore: 0,
  riskLevel: '',
  decision: ''
})

// 历史记录表格列定义
const historyColumns = [
  {
    title: '评估时间',
    dataIndex: 'time',
    key: 'time'
  },
  {
    title: '国家',
    dataIndex: 'country',
    key: 'country'
  },
  {
    title: '竞品',
    dataIndex: 'competitor',
    key: 'competitor'
  },
  {
    title: '风险等级',
    dataIndex: 'riskLevel',
    key: 'riskLevel'
  },
  {
    title: '决策建议',
    dataIndex: 'decision',
    key: 'decision'
  },
  {
    title: '操作',
    key: 'action'
  }
]

// 历史记录数据
const historyData = ref([
  {
    time: '2024-01-15 14:30',
    country: '美国',
    competitor: 'Visia',
    riskLevel: 'high',
    decision: '不出口'
  },
  {
    time: '2024-01-15 13:45',
    country: '欧盟',
    competitor: 'MedTech Pro',
    riskLevel: 'medium',
    decision: '申请预审核'
  }
])

// 表单引用
const formRef = ref()

// 处理国家变化
const handleCountryChange = (value: string) => {
  console.log('选择国家:', value)
}

// 处理竞品变化
const handleCompetitorChange = (e: any) => {
  console.log('竞品名称:', e.target.value)
}

// 处理HS编码变化
const handleHsCodeChange = (e: any) => {
  console.log('HS编码:', e.target.value)
}

// 处理法规关键词变化
const handleRegulatoryChange = (e: any) => {
  console.log('法规关键词:', e.target.value)
}

// 处理违规记录变化
const handleViolationChange = (e: any) => {
  console.log('违规记录:', e.target.value)
}

// 提交表单
const handleSubmit = async () => {
  try {
    calculating.value = true
    
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))
    
    // 模拟风险计算
    result.competitorScore = Math.floor(Math.random() * 100)
    result.hsCodeScore = Math.floor(Math.random() * 100)
    result.regulatoryScore = Math.floor(Math.random() * 100)
    result.violationScore = Math.floor(Math.random() * 100)
    
    // 计算总分（权重：竞品30% + HS编码25% + 法规语义25% + 违规记录20%）
    result.totalScore = Math.round(
      result.competitorScore * 0.3 +
      result.hsCodeScore * 0.25 +
      result.regulatoryScore * 0.25 +
      result.violationScore * 0.2
    )
    
    // 确定风险等级
    if (result.totalScore >= 80) {
      result.riskLevel = 'high'
      result.decision = '不出口'
    } else if (result.totalScore >= 50) {
      result.riskLevel = 'medium'
      result.decision = '申请预审核'
    } else {
      result.riskLevel = 'low'
      result.decision = '不动作'
    }
    
    showResult.value = true
    message.success('风险评估完成')
  } catch (error) {
    message.error('评估失败，请重试')
  } finally {
    calculating.value = false
  }
}

// 重置表单
const handleReset = () => {
  formRef.value?.resetFields()
  showResult.value = false
}

// 保存评估结果
const handleSave = () => {
  message.success('评估结果已保存')
}

// 导出报告
const handleExport = () => {
  message.success('报告导出成功')
}

// 查看历史详情
const viewHistoryDetail = (record: any) => {
  console.log('查看历史详情:', record)
}

// 获取评分颜色
const getScoreColor = (score: number) => {
  if (score >= 80) return '#ff4d4f'
  if (score >= 50) return '#faad14'
  return '#52c41a'
}

// 获取风险等级颜色
const getRiskLevelColor = (level: string) => {
  const colorMap: Record<string, string> = {
    high: 'red',
    medium: 'orange',
    low: 'green'
  }
  return colorMap[level] || 'default'
}

// 获取Alert类型
const getAlertType = (level: string) => {
  const typeMap: Record<string, string> = {
    high: 'error',
    medium: 'warning',
    low: 'success'
  }
  return typeMap[level] || 'info'
}

// 获取决策建议颜色
const getDecisionColor = (decision: string) => {
  const colorMap: Record<string, string> = {
    '不出口': '#fff2f0',
    '申请预审核': '#fff7e6',
    '不动作': '#f6ffed',
    '完成认证': '#e6f7ff'
  }
  return colorMap[decision] || '#f5f5f5'
}

// 获取决策描述
const getDecisionDescription = (decision: string) => {
  const descMap: Record<string, string> = {
    '不出口': '风险过高，建议暂停出口计划',
    '申请预审核': '需要进一步审核，建议申请预审核',
    '不动作': '风险较低，可以正常进行',
    '完成认证': '风险很低，可以完成认证流程'
  }
  return descMap[decision] || ''
}
</script>

<style scoped>
.pre-market {
  padding: 0;
}
</style> 