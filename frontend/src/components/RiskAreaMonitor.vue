<template>
  <div class="risk-monitor-container">
    <!-- 标题与控制区 -->
    <div class="header">
      <h1 class="title">地区风险监控面板</h1>
      <div class="controls">
        <a-date-picker
            v-model:value="selectedDate"
            format="YYYY-MM-DD"
            @change="handleDateChange"
        />
        <a-select
            v-model:value="timeRange"
            style="margin-left: 16px"
            @change="handleTimeRangeChange"
        >
          <a-select-option value="day">今日</a-select-option>
          <a-select-option value="week">本周</a-select-option>
          <a-select-option value="month">本月</a-select-option>
        </a-select>
        <a-button type="primary" style="margin-left: 16px" @click="refreshData">
          <a-icon type="sync" /> 刷新数据
        </a-button>
      </div>
    </div>

    <!-- 统计概览 -->
    <div class="stats-container">
      <a-card class="stat-card high-risk-stat">
        <div class="stat-content">
          <div class="stat-label">高风险地区总数</div>
          <div class="stat-value">{{ highRiskStats.total }}</div>
          <div class="stat-trend">
            <a-icon type="arrow-up" />
            <span>{{ highRiskStats.increaseRate }}%</span>
            <span class="trend-text">较上期</span>
          </div>
        </div>
      </a-card>

      <a-card class="stat-card medium-risk-stat">
        <div class="stat-content">
          <div class="stat-label">中风险地区总数</div>
          <div class="stat-value">{{ mediumRiskStats.total }}</div>
          <div class="stat-trend">
            <a-icon type="arrow-up" />
            <span>{{ mediumRiskStats.increaseRate }}%</span>
            <span class="trend-text">较上期</span>
          </div>
        </div>
      </a-card>

      <a-card class="stat-card low-risk-stat">
        <div class="stat-content">
          <div class="stat-label">低风险地区总数</div>
          <div class="stat-value">{{ lowRiskStats.total }}</div>
          <div class="stat-trend">
            <a-icon type="arrow-down" />
            <span>{{ lowRiskStats.increaseRate }}%</span>
            <span class="trend-text">较上期</span>
          </div>
        </div>
      </a-card>

      <a-card class="stat-card total-stat">
        <div class="stat-content">
          <div class="stat-label">监控地区总数</div>
          <div class="stat-value">{{ totalRegions }}</div>
          <div class="stat-trend">
            <a-icon type="arrow-right" />
            <span>{{ totalRegionsChange }}</span>
            <span class="trend-text">较上期</span>
          </div>
        </div>
      </a-card>
    </div>

    <!-- 主内容区 - 左右分栏 -->
    <div class="main-content">
      <!-- 左侧：高风险地区 -->
      <div class="risk-panel high-risk-panel">
        <div class="panel-header">
          <a-icon type="exclamation-circle" />
          <h2 class="panel-title">高风险地区</h2>
          <a-select
              v-model:value="highRiskSort"
              class="sort-select"
              @change="sortHighRiskAreas"
          >
            <a-select-option value="riskDesc">风险指数 ↓</a-select-option>
            <a-select-option value="riskAsc">风险指数 ↑</a-select-option>
            <a-select-option value="name">名称排序</a-select-option>
          </a-select>
        </div>

        <!-- 高风险地区图表 -->
        <div class="chart-container">
          <a-card>
            <div class="chart-title">高风险地区分布</div>
            <div class="chart-wrapper">
              <canvas id="highRiskChart"></canvas>
            </div>
          </a-card>
        </div>

        <!-- 高风险地区列表 -->
        <div class="region-list">
          <a-card
              class="region-card"
              v-for="(region, index) in highRiskRegions"
              :key="index"
              hoverable
              @click="showRegionDetail(region)"
          >
            <div class="region-header">
              <div class="region-name">{{ region.name }}</div>
              <div class="risk-level" :style="{ backgroundColor: getRiskColor(region.riskLevel) }">
                {{ region.riskLevel }}
              </div>
            </div>
            <div class="region-stats">
              <div class="region-stat-item">
                <div class="stat-item-label">风险指数</div>
                <div class="stat-item-value">{{ region.riskScore }}</div>
              </div>
              <div class="region-stat-item">
                <div class="stat-item-label">主要风险</div>
                <div class="stat-item-value">{{ region.mainRisk }}</div>
              </div>
              <div class="region-stat-item">
                <div class="stat-item-label">影响范围</div>
                <div class="stat-item-value">{{ region.impactRange }}</div>
              </div>
            </div>
            <div class="region-progress">
              <a-progress
                  :percent="region.riskScore"
                  :status="region.riskScore > 70 ? 'exception' : 'active'"
              />
            </div>
            <div class="region-update-time">
              最后更新: {{ formatDate(region.updateTime) }}
            </div>
          </a-card>
        </div>
      </div>

      <!-- 右侧：低风险地区 -->
      <div class="risk-panel low-risk-panel">
        <div class="panel-header">
          <a-icon type="check-circle" />
          <h2 class="panel-title">低风险地区</h2>
          <a-select
              v-model:value="lowRiskSort"
              class="sort-select"
              @change="sortLowRiskAreas"
          >
            <a-select-option value="riskDesc">风险指数 ↓</a-select-option>
            <a-select-option value="riskAsc">风险指数 ↑</a-select-option>
            <a-select-option value="name">名称排序</a-select-option>
          </a-select>
        </div>

        <!-- 低风险地区图表 -->
        <div class="chart-container">
          <a-card>
            <div class="chart-title">低风险地区分布</div>
            <div class="chart-wrapper">
              <canvas id="lowRiskChart"></canvas>
            </div>
          </a-card>
        </div>

        <!-- 低风险地区列表 -->
        <div class="region-list">
          <a-card
              class="region-card"
              v-for="(region, index) in lowRiskRegions"
              :key="index"
              hoverable
              @click="showRegionDetail(region)"
          >
            <div class="region-header">
              <div class="region-name">{{ region.name }}</div>
              <div class="risk-level" :style="{ backgroundColor: getRiskColor(region.riskLevel) }">
                {{ region.riskLevel }}
              </div>
            </div>
            <div class="region-stats">
              <div class="region-stat-item">
                <div class="stat-item-label">风险指数</div>
                <div class="stat-item-value">{{ region.riskScore }}</div>
              </div>
              <div class="region-stat-item">
                <div class="stat-item-label">主要风险</div>
                <div class="stat-item-value">{{ region.mainRisk || '无显著风险' }}</div>
              </div>
              <div class="region-stat-item">
                <div class="stat-item-label">影响范围</div>
                <div class="stat-item-value">{{ region.impactRange || '极小' }}</div>
              </div>
            </div>
            <div class="region-progress">
              <a-progress
                  :percent="region.riskScore"
                  status="success"
              />
            </div>
            <div class="region-update-time">
              最后更新: {{ formatDate(region.updateTime) }}
            </div>
          </a-card>
        </div>
      </div>
    </div>

    <!-- 地区详情模态框 -->
    <a-modal
        v-model:visible="detailVisible"
        :title="currentRegion ? currentRegion.name + ' 风险详情' : '地区风险详情'"
        :width="700"
        @cancel="handleCancel"
    >
      <div v-if="currentRegion" class="region-detail">
        <div class="detail-section">
          <h3 class="section-title">基本信息</h3>
          <div class="info-grid">
            <div class="info-item">
              <span class="info-label">地区名称:</span>
              <span class="info-value">{{ currentRegion.name }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">风险等级:</span>
              <span class="info-value" :style="{ color: getRiskColor(currentRegion.riskLevel) }">
                {{ currentRegion.riskLevel }}
              </span>
            </div>
            <div class="info-item">
              <span class="info-label">风险指数:</span>
              <span class="info-value">{{ currentRegion.riskScore }}</span>
            </div>
            <div class="info-item">
              <span class="info-label">上次评估时间:</span>
              <span class="info-value">{{ formatDate(currentRegion.updateTime) }}</span>
            </div>
          </div>
        </div>

        <div class="detail-section">
          <h3 class="section-title">风险构成</h3>
          <div class="risk-composition">
            <canvas id="riskCompositionChart"></canvas>
          </div>
        </div>

        <div class="detail-section">
          <h3 class="section-title">风险详情</h3>
          <a-descriptions column="1" bordered>
            <a-descriptions-item label="主要风险点">
              <div v-for="(risk, i) in currentRegion.riskDetails" :key="i" class="risk-point">
                <a-badge :status="risk.severity === '高' ? 'error' : risk.severity === '中' ? 'warning' : 'success'" />
                {{ risk.description }}
              </div>
            </a-descriptions-item>
            <a-descriptions-item label="影响范围">
              {{ currentRegion.impactRange }}
            </a-descriptions-item>
            <a-descriptions-item label="趋势预测">
              <a-tag :color="currentRegion.trend === '上升' ? 'red' : currentRegion.trend === '下降' ? 'green' : 'blue'">
                {{ currentRegion.trend }}
              </a-tag>
            </a-descriptions-item>
            <a-descriptions-item label="建议措施">
              <div v-for="(measure, i) in currentRegion.recommendations" :key="i" class="recommendation-item">
                {{ i + 1 }}. {{ measure }}
              </div>
            </a-descriptions-item>
          </a-descriptions>
        </div>
      </div>
    </a-modal>
  </div>
</template>

<script>
import { defineComponent, reactive, toRefs, onMounted, ref, watch } from 'vue';
import {
  DatePicker, Select, Button, Card, Progress, Modal,
  Badge, Tag, Descriptions
} from 'ant-design-vue';
import Chart from 'chart.js/auto';
import dayjs from 'dayjs';
import {
  SyncOutlined,
  ArrowUpOutlined,
  ArrowDownOutlined,
  ArrowRightOutlined,
  ExclamationCircleOutlined,
  CheckCircleOutlined
} from '@ant-design/icons-vue';

export default defineComponent({
  components: {
    aDatePicker: DatePicker,
    aSelect: Select,
    aSelectOption: Select.Option,
    aButton: Button,
    SyncOutlined,
    ArrowUpOutlined,
    ArrowDownOutlined,
    ArrowRightOutlined,
    ExclamationCircleOutlined,
    CheckCircleOutlined,
    aCard: Card,
    aProgress: Progress,
    aModal: Modal,
    aBadge: Badge,
    aTag: Tag,
    aDescriptions: Descriptions,
    aDescriptionsItem: Descriptions.Item
  },
  setup() {
    // 状态管理
    const state = reactive({
      selectedDate: dayjs(),
      timeRange: 'week',
      highRiskSort: 'riskDesc',
      lowRiskSort: 'riskAsc',
      highRiskRegions: [],
      lowRiskRegions: [],
      mediumRiskStats: {
        total: 0,
        increaseRate: 0
      },
      highRiskStats: {
        total: 0,
        increaseRate: 0
      },
      lowRiskStats: {
        total: 0,
        increaseRate: 0
      },
      totalRegions: 0,
      totalRegionsChange: 0,
      detailVisible: false,
      currentRegion: null,
      highRiskChart: null,
      lowRiskChart: null,
      riskCompositionChart: null
    });

    // 模拟数据
    const generateMockData = () => {
      // 高风险地区
      const highRiskAreas = [
        {
          name: '美国-加利福尼亚州',
          riskLevel: '高风险',
          riskScore: 92,
          mainRisk: '法规变动',
          impactRange: '广泛',
          updateTime: new Date(Date.now() - 3600000 * 5),
          trend: '上升',
          riskDetails: [
            { severity: '高', description: '医疗器械分类法规更新，影响产品准入' },
            { severity: '高', description: '进口抽检率提升至50%' },
            { severity: '中', description: '竞品注册数量增加30%' }
          ],
          recommendations: [
            '立即启动医疗器械认证流程',
            '调整产品宣传话术，避免医疗宣称',
            '增加合规团队人员配置'
          ]
        },
        {
          name: '欧盟-德国',
          riskLevel: '高风险',
          riskScore: 87,
          mainRisk: 'HS编码调整',
          impactRange: '广泛',
          updateTime: new Date(Date.now() - 3600000 * 12),
          trend: '稳定',
          riskDetails: [
            { severity: '高', description: 'HS编码9018归类调整，增加进口成本' },
            { severity: '中', description: '近期3起同类产品进口被罚案例' },
            { severity: '中', description: '行业协会发布风险预警' }
          ],
          recommendations: [
            '申请新HS编码评估',
            '与当地海关提前沟通产品归类',
            '准备应对可能的罚款风险'
          ]
        },
        {
          name: '韩国-首尔',
          riskLevel: '高风险',
          riskScore: 81,
          mainRisk: '召回记录',
          impactRange: '中等',
          updateTime: new Date(Date.now() - 3600000 * 24),
          trend: '上升',
          riskDetails: [
            { severity: '高', description: '同类产品因未注册被召回' },
            { severity: '中', description: '监管机构加强市场巡查' },
            { severity: '低', description: '消费者投诉量上升' }
          ],
          recommendations: [
            '全面审查产品合规性',
            '暂停部分高风险市场销售',
            '制定召回应对预案'
          ]
        },
        {
          name: '日本-东京',
          riskLevel: '高风险',
          riskScore: 76,
          mainRisk: '惩罚记录',
          impactRange: '中等',
          updateTime: new Date(Date.now() - 3600000 * 30),
          trend: '下降',
          riskDetails: [
            { severity: '高', description: '近期有进口罚款记录' },
            { severity: '中', description: '标签合规要求提高' },
            { severity: '低', description: '市场准入门槛调整' }
          ],
          recommendations: [
            '更新产品标签符合当地要求',
            '加强进口文件审核',
            '寻求当地合规咨询'
          ]
        }
      ];

      // 低风险地区
      const lowRiskAreas = [
        {
          name: '泰国-曼谷',
          riskLevel: '低风险',
          riskScore: 22,
          mainRisk: '',
          impactRange: '',
          updateTime: new Date(Date.now() - 3600000 * 48),
          trend: '稳定',
          riskDetails: [
            { severity: '低', description: '偶发合规咨询' }
          ],
          recommendations: [
            '保持常规合规检查',
            '关注法规更新'
          ]
        },
        {
          name: '马来西亚-吉隆坡',
          riskLevel: '低风险',
          riskScore: 18,
          mainRisk: '',
          impactRange: '',
          updateTime: new Date(Date.now() - 3600000 * 72),
          trend: '下降',
          riskDetails: [],
          recommendations: [
            '季度合规审查',
            '维持现有市场策略'
          ]
        },
        {
          name: '印度尼西亚-雅加达',
          riskLevel: '低风险',
          riskScore: 27,
          mainRisk: '轻微法规变动',
          impactRange: '有限',
          updateTime: new Date(Date.now() - 3600000 * 20),
          trend: '稳定',
          riskDetails: [
            { severity: '低', description: '进口流程轻微调整' }
          ],
          recommendations: [
            '更新进口流程文档',
            '与当地代理确认新要求'
          ]
        },
        {
          name: '菲律宾-马尼拉',
          riskLevel: '低风险',
          riskScore: 15,
          mainRisk: '',
          impactRange: '',
          updateTime: new Date(Date.now() - 3600000 * 96),
          trend: '下降',
          riskDetails: [],
          recommendations: [
            '半年度合规审查',
            '无紧急措施'
          ]
        }
      ];

      // 统计数据
      const mediumRiskCount = 3;

      return {
        highRiskAreas,
        lowRiskAreas,
        mediumRiskCount,
        totalRegions: highRiskAreas.length + lowRiskAreas.length + mediumRiskCount,
        totalRegionsChange: 2
      };
    };

    // 初始化数据
    const initData = () => {
      const {
        highRiskAreas,
        lowRiskAreas,
        mediumRiskCount,
        totalRegions,
        totalRegionsChange
      } = generateMockData();

      state.highRiskRegions = [...highRiskAreas];
      state.lowRiskRegions = [...lowRiskAreas];
      state.mediumRiskStats.total = mediumRiskCount;
      state.mediumRiskStats.increaseRate = 10;
      state.highRiskStats.total = highRiskAreas.length;
      state.highRiskStats.increaseRate = 25;
      state.lowRiskStats.total = lowRiskAreas.length;
      state.lowRiskStats.increaseRate = -5;
      state.totalRegions = totalRegions;
      state.totalRegionsChange = totalRegionsChange;
    };

    // 格式化日期
    const formatDate = (date) => {
      if (!date) return '';
      return new Intl.DateTimeFormat('zh-CN', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit',
        hour: '2-digit',
        minute: '2-digit'
      }).format(date);
    };

    // 获取风险等级颜色
    const getRiskColor = (level) => {
      switch(level) {
        case '高风险':
          return '#f5222d';
        case '中风险':
          return '#faad14';
        case '低风险':
          return '#52c41a';
        default:
          return '#1890ff';
      }
    };

    // 排序高风险地区
    const sortHighRiskAreas = () => {
      state.highRiskRegions.sort((a, b) => {
        if (state.highRiskSort === 'riskDesc') {
          return b.riskScore - a.riskScore;
        } else if (state.highRiskSort === 'riskAsc') {
          return a.riskScore - b.riskScore;
        } else if (state.highRiskSort === 'name') {
          return a.name.localeCompare(b.name);
        }
        return 0;
      });
    };

    // 排序低风险地区
    const sortLowRiskAreas = () => {
      state.lowRiskRegions.sort((a, b) => {
        if (state.lowRiskSort === 'riskDesc') {
          return b.riskScore - a.riskScore;
        } else if (state.lowRiskSort === 'riskAsc') {
          return a.riskScore - b.riskScore;
        } else if (state.lowRiskSort === 'name') {
          return a.name.localeCompare(b.name);
        }
        return 0;
      });
    };

    // 显示地区详情
    const showRegionDetail = (region) => {
      state.currentRegion = { ...region };
      state.detailVisible = true;

      // 等待模态框渲染后初始化图表
      setTimeout(() => {
        initRiskCompositionChart();
      }, 100);
    };

    // 处理取消
    const handleCancel = () => {
      state.detailVisible = false;
      state.currentRegion = null;

      // 销毁图表
      if (state.riskCompositionChart) {
        state.riskCompositionChart.destroy();
        state.riskCompositionChart = null;
      }
    };

    // 刷新数据
    const refreshData = () => {
      // 模拟加载状态
      const tempHigh = state.highRiskRegions;
      const tempLow = state.lowRiskRegions;
      state.highRiskRegions = [];
      state.lowRiskRegions = [];

      setTimeout(() => {
        initData();
        initCharts();
      }, 800);
    };

    // 处理日期变更
    const handleDateChange = (date) => {
      // 实际应用中可以根据日期加载对应数据
      console.log('Selected date:', date);
    };

    // 处理时间范围变更
    const handleTimeRangeChange = (range) => {
      // 实际应用中可以根据时间范围加载对应数据
      console.log('Time range:', range);
    };

    // 初始化图表
    const initCharts = () => {
      // 高风险地区图表
      if (state.highRiskChart) {
        state.highRiskChart.destroy();
      }

      const highRiskCtx = document.getElementById('highRiskChart').getContext('2d');
      state.highRiskChart = new Chart(highRiskCtx, {
        type: 'bar',
        data: {
          labels: state.highRiskRegions.map(r => r.name),
          datasets: [{
            label: '风险指数',
            data: state.highRiskRegions.map(r => r.riskScore),
            backgroundColor: 'rgba(245, 34, 45, 0.7)',
            borderColor: 'rgba(245, 34, 45, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              max: 100,
              title: {
                display: true,
                text: '风险指数'
              }
            }
          },
          plugins: {
            legend: {
              display: false
            }
          }
        }
      });

      // 低风险地区图表
      if (state.lowRiskChart) {
        state.lowRiskChart.destroy();
      }

      const lowRiskCtx = document.getElementById('lowRiskChart').getContext('2d');
      state.lowRiskChart = new Chart(lowRiskCtx, {
        type: 'bar',
        data: {
          labels: state.lowRiskRegions.map(r => r.name),
          datasets: [{
            label: '风险指数',
            data: state.lowRiskRegions.map(r => r.riskScore),
            backgroundColor: 'rgba(82, 196, 26, 0.7)',
            borderColor: 'rgba(82, 196, 26, 1)',
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          scales: {
            y: {
              beginAtZero: true,
              max: 100,
              title: {
                display: true,
                text: '风险指数'
              }
            }
          },
          plugins: {
            legend: {
              display: false
            }
          }
        }
      });
    };

    // 初始化风险构成图表
    const initRiskCompositionChart = () => {
      if (!state.currentRegion) return;

      // 模拟风险构成数据
      const riskComposition = {
        '法规风险': Math.floor(Math.random() * 40) + 30,
        '市场风险': Math.floor(Math.random() * 30) + 10,
        '运营风险': Math.floor(Math.random() * 20) + 5,
        '其他风险': 100 - (
            Math.floor(Math.random() * 40) + 30 +
            Math.floor(Math.random() * 30) + 10 +
            Math.floor(Math.random() * 20) + 5
        )
      };

      if (state.riskCompositionChart) {
        state.riskCompositionChart.destroy();
      }

      const ctx = document.getElementById('riskCompositionChart').getContext('2d');
      state.riskCompositionChart = new Chart(ctx, {
        type: 'doughnut',
        data: {
          labels: Object.keys(riskComposition),
          datasets: [{
            data: Object.values(riskComposition),
            backgroundColor: [
              'rgba(245, 34, 45, 0.7)',
              'rgba(250, 173, 20, 0.7)',
              'rgba(24, 144, 255, 0.7)',
              'rgba(150, 150, 150, 0.7)'
            ],
            borderWidth: 1
          }]
        },
        options: {
          responsive: true,
          maintainAspectRatio: false,
          plugins: {
            legend: {
              position: 'right'
            },
            title: {
              display: true,
              text: '风险构成比例'
            }
          }
        }
      });
    };

    // 组件挂载时初始化
    onMounted(() => {
      initData();
      initCharts();
    });

    // 监听数据变化重新排序
    watch(
        () => state.highRiskSort,
        () => sortHighRiskAreas()
    );

    watch(
        () => state.lowRiskSort,
        () => sortLowRiskAreas()
    );

    return {
      ...toRefs(state),
      formatDate,
      getRiskColor,
      sortHighRiskAreas,
      sortLowRiskAreas,
      showRegionDetail,
      handleCancel,
      refreshData,
      handleDateChange,
      handleTimeRangeChange
    };
  }
});
</script>

<style scoped>
.risk-monitor-container {
  padding: 1.25rem; /* 使用rem（相对于根字体大小） */
  min-height: 100%; /* 相对于父组件高度 */
  width: 100%; /* 宽度填满父组件 */
  box-sizing: border-box; /* 确保padding不超出父组件 */
}

.header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.title {
  margin: 0;
  color: #1f2329;
  font-size: 24px;
  font-weight: 600;
}

.controls {
  display: flex;
  align-items: center;
}

.stats-container {
  display: flex;
  gap: 16px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.stat-card {
  flex: 1;
  min-width: 200px;
  transition: all 0.3s ease;
}

.stat-card:hover {
  transform: translateY(-3px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.stat-content {
  display: flex;
  flex-direction: column;
  padding: 16px;
}

.stat-label {
  color: rgba(0, 0, 0, 0.65);
  font-size: 14px;
  margin-bottom: 8px;
}

.stat-value {
  font-size: 28px;
  font-weight: 600;
  margin-bottom: 8px;
}

.stat-trend {
  display: flex;
  align-items: center;
  font-size: 14px;
}

.stat-trend .anticon-arrow-up {
  color: #f5222d;
  margin-right: 4px;
}

.stat-trend .anticon-arrow-down {
  color: #52c41a;
  margin-right: 4px;
}

.stat-trend .anticon-arrow-right {
  color: #1890ff;
  margin-right: 4px;
}

.trend-text {
  margin-left: 4px;
  color: rgba(0, 0, 0, 0.5);
}

.high-risk-stat .stat-value {
  color: #f5222d;
}

.medium-risk-stat .stat-value {
  color: #faad14;
}

.low-risk-stat .stat-value {
  color: #52c41a;
}

.total-stat .stat-value {
  color: #1890ff;
}

.main-content {
  display: flex;
  gap: 24px;
  height: calc(100% - 180px);
}

.risk-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.high-risk-panel {
  border-left: 3px solid #f5222d;
  padding-left: 20px;
}

.low-risk-panel {
  border-left: 3px solid #52c41a;
  padding-left: 20px;
}

.panel-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.panel-header .anticon {
  color: #f5222d;
  margin-right: 8px;
}

.low-risk-panel .panel-header .anticon {
  color: #52c41a;
}

.panel-title {
  display: flex;
  align-items: center;
  margin: 0;
  font-size: 18px;
  font-weight: 600;
}

.sort-select {
  width: 160px;
}

.chart-container {
  margin-bottom: 24px;
}

.chart-title {
  font-size: 16px;
  margin-bottom: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
}

.chart-wrapper {
  height: 60%; /* 相对于父容器高度的60% */
  min-height: 200px; /* 最低高度保底 */
}

.region-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  overflow-y: auto;
  max-height: calc(100vh - 450px);
  padding-right: 8px;
}

.region-card {
  transition: all 0.2s ease;
}

.region-card:hover {
  transform: translateX(5px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.1);
}

.region-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.region-name {
  font-size: 16px;
  font-weight: 500;
}

.risk-level {
  color: white;
  padding: 2px 8px;
  border-radius: 4px;
  font-size: 12px;
  font-weight: 500;
}

.region-stats {
  display: flex;
  justify-content: space-between;
  margin-bottom: 12px;
}

.region-stat-item {
  flex: 1;
  text-align: center;
}

.stat-item-label {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.65);
  margin-bottom: 4px;
  display: block;
}

.stat-item-value {
  font-size: 14px;
  font-weight: 500;
}

.region-progress {
  margin-bottom: 8px;
}

.region-update-time {
  font-size: 12px;
  color: rgba(0, 0, 0, 0.5);
  text-align: right;
}

.region-detail {
  padding: 8px 0;
}

.detail-section {
  margin-bottom: 24px;
}

.section-title {
  font-size: 16px;
  margin-bottom: 16px;
  color: rgba(0, 0, 0, 0.85);
  font-weight: 500;
  padding-bottom: 8px;
  border-bottom: 1px solid #e8e8e8;
}

.info-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 16px;
}

.info-item {
  display: flex;
  margin-bottom: 8px;
}

.info-label {
  flex: 0 0 100px;
  color: rgba(0, 0, 0, 0.65);
}

.info-value {
  flex: 1;
  font-weight: 500;
}

.risk-composition {
  height: 240px;
  margin-bottom: 16px;
}

.risk-point {
  margin-bottom: 8px;
  display: flex;
  align-items: center;
}

.risk-point .ant-badge {
  margin-right: 8px;
}

.recommendation-item {
  margin-bottom: 8px;
  padding-left: 8px;
  position: relative;
}

.recommendation-item:before {
  content: '';
  position: absolute;
  left: 0;
  top: 8px;
  width: 4px;
  height: 4px;
  border-radius: 50%;
  background-color: #1890ff;
}

/* 滚动条样式优化 */
.region-list::-webkit-scrollbar {
  width: 6px;
}

.region-list::-webkit-scrollbar-track {
  background: #f1f1f1;
  border-radius: 3px;
}

.region-list::-webkit-scrollbar-thumb {
  background: #ccc;
  border-radius: 3px;
}

.region-list::-webkit-scrollbar-thumb:hover {
  background: #aaa;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .main-content {
    flex-direction: column;
  }

  .high-risk-panel, .low-risk-panel {
    padding-left: 12px;
  }

  .stats-container {
    flex-direction: column;
  }

  .stat-card {
    width: 100%;
  }

  .header {
    flex-direction: column;
    align-items: flex-start;
    gap: 16px;
  }

  .controls {
    width: 100%;
    flex-wrap: wrap;
  }

  .a-date-picker, .a-select {
    width: 100% !important;
    margin-left: 0 !important;
    margin-bottom: 12px !important;
  }

  .info-grid {
    grid-template-columns: 1fr;
  }
}
</style>
