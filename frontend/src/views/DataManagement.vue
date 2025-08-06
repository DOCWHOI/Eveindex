<template>
  <div class="data-management">
    <!-- 数据源状态 -->
    <a-card title="数据源状态" :bordered="false">
      <a-row :gutter="16">
        <a-col :span="8" v-for="source in dataSources" :key="source.name">
          <a-card class="source-card" :class="{ 'error': source.status === 'error' }">
            <div class="source-header">
              <div class="source-info">
                <h4>{{ source.name }}</h4>
                <a-tag :color="getSourceStatusColor(source.status)">
                  {{ getSourceStatusText(source.status) }}
                </a-tag>
              </div>
              <a-button 
                type="primary" 
                size="small" 
                :loading="source.syncing"
                @click="syncDataSource(source)"
              >
                同步
              </a-button>
            </div>
            <div class="source-details">
              <div class="detail-item">
                <span>类型:</span>
                <span>{{ source.type }}</span>
              </div>
              <div class="detail-item">
                <span>同步周期:</span>
                <span>{{ source.syncCycle }}</span>
              </div>
              <div class="detail-item">
                <span>最后同步:</span>
                <span>{{ source.lastSyncTime }}</span>
              </div>
              <div class="detail-item">
                <span>数据量:</span>
                <span>{{ source.dataCount }}</span>
              </div>
            </div>
          </a-card>
        </a-col>
      </a-row>
    </a-card>

    <!-- 数据查询 -->
    <a-card title="数据查询" :bordered="false" style="margin-top: 24px">
      <a-form layout="inline" @finish="handleSearch">
        <a-form-item label="关键词">
          <a-input
            v-model:value="searchForm.keyword"
            placeholder="竞品名、HS编码、法规关键词"
            style="width: 300px"
          />
        </a-form-item>
        <a-form-item label="国家">
          <a-select
            v-model:value="searchForm.country"
            placeholder="选择国家"
            style="width: 150px"
            :options="countries"
            allow-clear
          />
        </a-form-item>
        <a-form-item label="数据类型">
          <a-select
            v-model:value="searchForm.dataType"
            placeholder="选择类型"
            style="width: 150px"
            :options="dataTypes"
            allow-clear
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="searching">
            搜索
          </a-button>
          <a-button style="margin-left: 8px" @click="handleReset">
            重置
          </a-button>
        </a-form-item>
      </a-form>

      <!-- 搜索结果 -->
      <a-table 
        :columns="searchColumns" 
        :data-source="searchResults" 
        :pagination="{ pageSize: 10 }"
        style="margin-top: 16px"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'source'">
            <a-tag :color="getSourceTagColor(record.source)">
              {{ record.source }}
            </a-tag>
          </template>
          <template v-else-if="column.key === 'action'">
            <a-button type="link" size="small" @click="viewDataDetail(record)">
              查看详情
            </a-button>
          </template>
        </template>
      </a-table>
    </a-card>

    <!-- 手动录入 -->
    <a-card title="手动录入" :bordered="false" style="margin-top: 24px">
      <a-form
        :model="manualForm"
        :rules="manualRules"
        ref="manualFormRef"
        layout="vertical"
        @finish="handleManualSubmit"
      >
        <a-row :gutter="24">
          <a-col :span="8">
            <a-form-item label="国家" name="country">
              <a-select
                v-model:value="manualForm.country"
                placeholder="选择国家"
                :options="countries"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="数据类型" name="dataType">
              <a-select
                v-model:value="manualForm.dataType"
                placeholder="选择数据类型"
                :options="manualDataTypes"
              />
            </a-form-item>
          </a-col>
          <a-col :span="8">
            <a-form-item label="竞品名称" name="competitor">
              <a-input
                v-model:value="manualForm.competitor"
                placeholder="请输入竞品名称"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row :gutter="24">
          <a-col :span="12">
            <a-form-item label="HS编码" name="hsCode">
              <a-input
                v-model:value="manualForm.hsCode"
                placeholder="请输入HS编码"
              />
            </a-form-item>
          </a-col>
          <a-col :span="12">
            <a-form-item label="数据来源" name="source">
              <a-input
                v-model:value="manualForm.source"
                placeholder="请输入数据来源"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="24">
            <a-form-item label="详细描述" name="description">
              <a-textarea
                v-model:value="manualForm.description"
                placeholder="请输入详细描述"
                :rows="4"
              />
            </a-form-item>
          </a-col>
        </a-row>

        <a-row>
          <a-col :span="24">
            <a-form-item>
              <a-button type="primary" html-type="submit" :loading="submitting">
                提交
              </a-button>
              <a-button style="margin-left: 8px" @click="handleManualReset">
                重置
              </a-button>
            </a-form-item>
          </a-col>
        </a-row>
      </a-form>
    </a-card>

    <!-- Excel导入导出 -->
    <a-card title="Excel导入导出" :bordered="false" style="margin-top: 24px">
      <a-row :gutter="16">
        <a-col :span="12">
          <a-upload
            :file-list="fileList"
            :before-upload="beforeUpload"
            :custom-request="handleUpload"
          >
            <a-button>
              <upload-outlined />
              选择Excel文件
            </a-button>
          </a-upload>
          <p style="margin-top: 8px; color: #8c8c8c;">
            支持.xlsx格式，请确保文件格式正确
          </p>
        </a-col>
        <a-col :span="12">
          <a-button @click="handleExport">
            <download-outlined />
            导出数据
          </a-button>
          <p style="margin-top: 8px; color: #8c8c8c;">
            导出当前搜索结果到Excel
          </p>
        </a-col>
      </a-row>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive } from 'vue'
import { message } from 'ant-design-vue'
import { UploadOutlined, DownloadOutlined } from '@ant-design/icons-vue'

// 数据源状态
const dataSources = ref([
  {
    name: '美国FDA',
    status: 'normal',
    type: 'API',
    syncCycle: '每日',
    lastSyncTime: '2024-01-15 15:30',
    dataCount: '12,456',
    syncing: false
  },
  {
    name: '欧盟EUDAMED',
    status: 'normal',
    type: 'CSV下载',
    syncCycle: '每周',
    lastSyncTime: '2024-01-14 10:00',
    dataCount: '8,932',
    syncing: false
  },
  {
    name: '中国NMPA',
    status: 'error',
    type: '爬虫',
    syncCycle: '每周',
    lastSyncTime: '2024-01-13 14:20',
    dataCount: '15,678',
    syncing: false
  },
  {
    name: '日本PMDA',
    status: 'normal',
    type: 'API',
    syncCycle: '每日',
    lastSyncTime: '2024-01-15 16:45',
    dataCount: '6,234',
    syncing: false
  },
  {
    name: '韩国MFDS',
    status: 'warning',
    type: '爬虫',
    syncCycle: '每周',
    lastSyncTime: '2024-01-12 09:15',
    dataCount: '4,567',
    syncing: false
  },
  {
    name: '澳大利亚TGA',
    status: 'normal',
    type: 'CSV下载',
    syncCycle: '每月',
    lastSyncTime: '2024-01-10 11:30',
    dataCount: '3,890',
    syncing: false
  }
])

// 搜索表单
const searchForm = reactive({
  keyword: '',
  country: undefined,
  dataType: undefined
})

// 手动录入表单
const manualForm = reactive({
  country: '',
  dataType: '',
  competitor: '',
  hsCode: '',
  source: '',
  description: ''
})

// 表单验证规则
const manualRules = {
  country: [{ required: true, message: '请选择国家' }],
  dataType: [{ required: true, message: '请选择数据类型' }],
  competitor: [{ required: true, message: '请输入竞品名称' }]
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

// 数据类型选项
const dataTypes = [
  { value: 'competitor', label: '竞品信息' },
  { value: 'hsCode', label: 'HS编码' },
  { value: 'regulation', label: '法规信息' },
  { value: 'violation', label: '违规记录' }
]

// 手动录入数据类型选项
const manualDataTypes = [
  { value: 'competitor', label: '竞品信息' },
  { value: 'hsCode', label: 'HS编码' },
  { value: 'regulation', label: '法规信息' },
  { value: 'violation', label: '违规记录' },
  { value: 'custom', label: '自定义数据' }
]

// 搜索状态
const searching = ref(false)
const submitting = ref(false)

// 搜索结果表格列定义
const searchColumns = [
  {
    title: '国家',
    dataIndex: 'country',
    key: 'country'
  },
  {
    title: '数据类型',
    dataIndex: 'dataType',
    key: 'dataType'
  },
  {
    title: '竞品/编码',
    dataIndex: 'name',
    key: 'name'
  },
  {
    title: '数据来源',
    dataIndex: 'source',
    key: 'source'
  },
  {
    title: '更新时间',
    dataIndex: 'updateTime',
    key: 'updateTime'
  },
  {
    title: '操作',
    key: 'action'
  }
]

// 搜索结果数据
const searchResults = ref([
  {
    country: '美国',
    dataType: '竞品信息',
    name: 'Visia',
    source: 'FDA API',
    updateTime: '2024-01-15 15:30'
  },
  {
    country: '欧盟',
    dataType: 'HS编码',
    name: '9018.11',
    source: 'EUDAMED',
    updateTime: '2024-01-14 10:00'
  },
  {
    country: '中国',
    dataType: '法规信息',
    name: '医疗器械监督管理条例',
    source: 'NMPA爬虫',
    updateTime: '2024-01-13 14:20'
  }
])

// 文件上传相关
const fileList = ref([])

// 表单引用
const manualFormRef = ref()

// 获取数据源状态颜色
const getSourceStatusColor = (status: string) => {
  const colorMap: Record<string, string> = {
    normal: 'green',
    warning: 'orange',
    error: 'red'
  }
  return colorMap[status] || 'default'
}

// 获取数据源状态文本
const getSourceStatusText = (status: string) => {
  const textMap: Record<string, string> = {
    normal: '正常',
    warning: '警告',
    error: '错误'
  }
  return textMap[status] || '未知'
}

// 获取来源标签颜色
const getSourceTagColor = (source: string) => {
  const colorMap: Record<string, string> = {
    'FDA API': 'blue',
    'EUDAMED': 'green',
    'NMPA爬虫': 'orange',
    '手动录入': 'purple'
  }
  return colorMap[source] || 'default'
}

// 同步数据源
const syncDataSource = async (source: any) => {
  try {
    source.syncing = true
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 2000))
    source.lastSyncTime = new Date().toLocaleString()
    source.status = 'normal'
    message.success(`${source.name} 数据同步成功`)
  } catch (error) {
    message.error(`${source.name} 数据同步失败`)
  } finally {
    source.syncing = false
  }
}

// 搜索数据
const handleSearch = async () => {
  try {
    searching.value = true
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    message.success('搜索完成')
  } catch (error) {
    message.error('搜索失败')
  } finally {
    searching.value = false
  }
}

// 重置搜索
const handleReset = () => {
  searchForm.keyword = ''
  searchForm.country = undefined
  searchForm.dataType = undefined
}

// 手动录入提交
const handleManualSubmit = async () => {
  try {
    submitting.value = true
    // 模拟API调用
    await new Promise(resolve => setTimeout(resolve, 1000))
    message.success('数据录入成功')
    handleManualReset()
  } catch (error) {
    message.error('数据录入失败')
  } finally {
    submitting.value = false
  }
}

// 重置手动录入表单
const handleManualReset = () => {
  manualFormRef.value?.resetFields()
}

// 查看数据详情
const viewDataDetail = (record: any) => {
  console.log('查看数据详情:', record)
}

// 文件上传前检查
const beforeUpload = (file: File) => {
  const isExcel = file.type === 'application/vnd.openxmlformats-officedocument.spreadsheetml.sheet' ||
                  file.type === 'application/vnd.ms-excel'
  if (!isExcel) {
    message.error('只能上传Excel文件!')
    return false
  }
  const isLt10M = file.size / 1024 / 1024 < 10
  if (!isLt10M) {
    message.error('文件大小不能超过10MB!')
    return false
  }
  return true
}

// 处理文件上传
const handleUpload = async (options: any) => {
  try {
    // 模拟文件上传
    await new Promise(resolve => setTimeout(resolve, 2000))
    message.success('文件上传成功')
    options.onSuccess()
  } catch (error) {
    message.error('文件上传失败')
    options.onError()
  }
}

// 导出数据
const handleExport = () => {
  message.success('数据导出成功')
}
</script>

<style scoped>
.data-management {
  padding: 0;
}

.source-card {
  margin-bottom: 16px;
}

.source-card.error {
  border-color: #ff4d4f;
}

.source-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.source-info h4 {
  margin: 0 0 8px 0;
  font-size: 16px;
}

.source-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  display: flex;
  justify-content: space-between;
  font-size: 14px;
  color: #8c8c8c;
}
</style> 