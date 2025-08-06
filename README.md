# 风险监控系统

一个基于 Vue3 + Spring Boot 的医疗器械风险监控系统，提供全球范围内的风险评估、动态监控和预警功能。

## 🚀 功能特性

### 前端功能
- **风险可视化**: 直观的风险展示和操作界面
- **Pre-market风险评估**: 竞争对手信息、HS编码风险评估
- **Post-market动态监控**: 实时监控政府召回、HS编码变更、设备活跃度、投诉数据
- **数据查询管理**: 官方数据源快速访问，手动数据录入
- **预警报告中心**: 历史预警记录，自动报告生成

### 后端功能
- **数据收集处理**: 多通道数据收集（API、爬虫、文件解析、手动录入）
- **风险计算**: 基于权重的风险评估算法
- **预警触发**: 实时监控和邮件预警
- **报告生成**: 定时报告生成和自定义报告

## 🛠️ 技术栈

### 前端
- Vue 3 (Composition API)
- Ant Design Vue
- TypeScript
- Vite
- Vue Router

### 后端
- Spring Boot 2.7.18
- MyBatis Plus
- MySQL 8.0
- Lombok
- Druid

## 📦 快速开始

### 环境要求
- Node.js 16+
- Java 8+
- MySQL 8.0+
- Maven 3.6+

### 安装步骤

1. **克隆项目**
```bash
git clone <repository-url>
cd Eveindex
```

2. **初始化数据库**
```bash
mysql -u root -p < database/init.sql
```

3. **安装依赖**
```bash
# 前端依赖
cd frontend
npm install

# 后端依赖
cd ../backend
mvn install
```

4. **启动服务**
```bash
# Windows
./start.bat

# Linux/Mac
./start.sh
```

5. **访问系统**
- 前端: http://localhost:3000
- 后端API: http://localhost:8080/api

## 📁 项目结构

```
Eveindex/
├── frontend/                 # Vue3 前端
│   ├── src/
│   │   ├── views/           # 页面组件
│   │   ├── layouts/         # 布局组件
│   │   └── router/          # 路由配置
│   ├── package.json
│   └── vite.config.ts
├── backend/                  # Spring Boot 后端
│   ├── src/main/java/
│   │   └── com/eveindex/
│   │       ├── controller/  # 控制器
│   │       ├── entity/      # 实体类
│   │       └── service/     # 服务层
│   ├── pom.xml
│   └── application.yml
├── database/                 # 数据库脚本
│   └── init.sql
├── docs/                     # 文档
├── start.bat                 # Windows启动脚本
├── start.sh                  # Linux/Mac启动脚本
└── README.md
```

## 🔧 配置说明

### 数据库配置
编辑 `backend/src/main/resources/application.yml`:
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/risk_monitoring
    username: your_username
    password: your_password
```

### 前端配置
编辑 `frontend/vite.config.ts` 中的代理配置:
```typescript
proxy: {
  '/api': {
    target: 'http://localhost:8080',
    changeOrigin: true
  }
}
```

## 📊 核心功能

### Pre-market风险评估
- 竞争对手信息查询
- HS编码风险评估
- 监管语义分析
- 违规记录检查
- 风险等级判定（高/中/低）
- 决策建议生成

### Post-market动态监控
- 政府召回公告监控
- HS编码变更跟踪
- 设备活跃度（DAU/WAU）监控
- 投诉/退货数据分析
- 实时预警触发

### 数据管理
- 官方数据源集成（FDA、NMPA、EUDAMED）
- 手动数据录入
- Excel导入导出
- 数据同步状态监控

### 预警报告
- 历史预警记录查询
- 自动报告生成（周报/月报）
- 邮件通知记录
- 预警处理状态跟踪

## 🤝 贡献指南

1. Fork 项目
2. 创建功能分支 (`git checkout -b feature/AmazingFeature`)
3. 提交更改 (`git commit -m 'Add some AmazingFeature'`)
4. 推送到分支 (`git push origin feature/AmazingFeature`)
5. 打开 Pull Request

## 📄 许可证

本项目采用 MIT 许可证 - 查看 [LICENSE](LICENSE) 文件了解详情

## 📞 联系方式

- 项目维护者: [Your Name]
- 邮箱: [your.email@example.com]
- 项目地址: [https://github.com/your-username/risk-monitoring-system]

## 🙏 致谢

感谢所有为这个项目做出贡献的开发者和用户。