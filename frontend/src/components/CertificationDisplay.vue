<template>
  <div class="certification-container">
    <!-- 组件标题 -->
    <div class="certification-header">
      <h3 class="title">产品认证信息</h3>
      <a-button type="primary" size="small" @click="handleAddCertification">
        <a-icon type="plus" /> 添加认证
      </a-button>
    </div>

    <!-- 认证列表 -->
    <div class="certification-list">
      <a-card
          class="certification-card"
          v-for="(cert, index) in certifications"
          :key="index"
          hoverable
      >
        <div class="certification-content">
          <!-- 认证基本信息 -->
          <div class="certification-main">
            <div class="certification-title">
              <span class="name">{{ cert.name }}</span>
              <a-tag :color="cert.status === '已认证' ? 'green' : 'orange'">
                {{ cert.status }}
              </a-tag>
            </div>

            <div class="certification-details">
              <div class="detail-item">
                <span class="detail-label">认证来源：</span>
                <span class="detail-value">{{ cert.source }}</span>
              </div>

              <div class="detail-item">
                <span class="detail-label">认证原因：</span>
                <span class="detail-value">{{ cert.reason }}</span>
              </div>

              <div class="detail-item">
                <span class="detail-label">有效期：</span>
                <span class="detail-value">{{ cert.validity }}</span>
              </div>
            </div>
          </div>

          <!-- 操作按钮 -->
          <div class="certification-actions">
            <a-button
                type="text"
                size="small"
                @click="handleViewDetails(cert)"
            >
              查看详情
            </a-button>
            <a-button
                type="text"
                size="small"
                @click="handleEdit(cert)"
                class="edit-btn"
            >
              编辑
            </a-button>
            <a-popconfirm
                title="确定要删除该认证吗？"
                @confirm="handleDelete(index)"
            >
              <a-button
                  type="text"
                  size="small"
                  class="delete-btn"
              >
                删除
              </a-button>
            </a-popconfirm>
          </div>
        </div>
      </a-card>

      <!-- 空状态 -->
      <div v-if="certifications.length === 0" class="empty-state">
        <a-empty description="暂无认证信息，请添加" />
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, reactive, toRefs } from 'vue';
import { Card, Button, Tag, Popconfirm, Empty } from 'ant-design-vue';

export default defineComponent({
  components: {
    aCard: Card,
    aButton: Button,
    // aIcon: Icon,
    aTag: Tag,
    aPopconfirm: Popconfirm,
    aEmpty: Empty
  },
  setup() {
    // 认证数据
    const state = reactive({
      certifications: [
        {
          name: 'ISO 9001 质量管理体系认证',
          status: '已认证',
          source: '国际标准化组织',
          reason: '满足国际市场准入要求，提升产品质量信誉',
          validity: '2023-05-10 至 2026-05-09'
        },
        {
          name: 'CE 认证',
          status: '认证中',
          source: '欧盟委员会',
          reason: '产品出口欧盟市场必须具备的安全认证',
          validity: '待认证通过后确定'
        },
        {
          name: 'FDA 认证',
          status: '未认证',
          source: '美国食品药品监督管理局',
          reason: '计划进入美国市场，需符合当地监管要求',
          validity: '未认证'
        }
      ]
    });

    // 操作方法
    const handleViewDetails = (cert) => {
      // 查看详情逻辑
      console.log('查看详情:', cert);
      // 实际应用中可打开详情模态框
    };

    const handleEdit = (cert) => {
      // 编辑逻辑
      console.log('编辑:', cert);
    };

    const handleDelete = (index) => {
      // 删除逻辑
      state.certifications.splice(index, 1);
    };

    const handleAddCertification = () => {
      // 添加新认证逻辑
      console.log('添加新认证');
    };

    return {
      ...toRefs(state),
      handleViewDetails,
      handleEdit,
      handleDelete,
      handleAddCertification
    };
  }
});
</script>

<style scoped>
.certification-container {
  padding: 16px;
}

.certification-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
}

.title {
  margin: 0;
  font-size: 18px;
  font-weight: 500;
}

.certification-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.certification-card {
  transition: all 0.2s ease;
}

.certification-card:hover {
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
}

.certification-content {
  display: flex;
  justify-content: space-between;
  align-items: flex-start;
}

.certification-main {
  flex: 1;
}

.certification-title {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.name {
  font-size: 16px;
  font-weight: 500;
}

.certification-details {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.detail-item {
  font-size: 14px;
}

.detail-label {
  color: rgba(0, 0, 0, 0.65);
  margin-right: 4px;
}

.detail-value {
  color: rgba(0, 0, 0, 0.85);
}

.certification-actions {
  display: flex;
  flex-direction: column;
  gap: 8px;
  margin-left: 24px;
}

.edit-btn {
  color: #1890ff;
}

.delete-btn {
  color: #f5222d;
}

.empty-state {
  padding: 40px 0;
  text-align: center;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .certification-content {
    flex-direction: column;
  }

  .certification-actions {
    flex-direction: row;
    margin-left: 0;
    margin-top: 16px;
  }
}
</style>
