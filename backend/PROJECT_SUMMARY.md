# 风险监控系统后端项目总结

## 项目概述
本项目是一个医疗器械风险监控系统的后端服务，基于Spring Boot 3.5.4和MyBatis-Plus构建。

## 技术栈
- **框架**: Spring Boot 3.5.4
- **数据库**: MySQL 8.0
- **ORM**: MyBatis-Plus 3.5.12
- **连接池**: Druid 1.2.20
- **API文档**: Knife4j 4.5.0
- **Java版本**: JDK 17

## 项目结构
```
backend/
├── src/main/java/com/eveindex/
│   ├── controller/          # 控制器层
│   ├── service/            # 服务层
│   │   └── impl/          # 服务实现
│   ├── entity/            # 实体类
│   ├── mapper/            # 数据访问层
│   ├── dto/               # 数据传输对象
│   ├── common/            # 公共配置
│   │   └── exception/     # 异常处理
│   └── RiskMonitoringApplication.java  # 启动类
├── src/main/resources/
│   └── application.yml    # 配置文件
├── database/
│   └── createtabel.sql   # 数据库脚本
└── pom.xml               # Maven配置
```

## 核心功能模块

### 1. 产品管理 (Product)
- 产品信息的增删改查
- 支持分页查询和关键词搜索
- 产品功能描述和HS编码关联

### 2. 市场管理 (Market)
- 各国市场的监管信息管理
- 监管机构信息维护
- 监管严格程度评估

### 3. 竞品管理 (Competitor)
- 竞品信息管理
- 医疗器械注册状态跟踪
- 竞品分析数据

### 4. HS编码管理 (HsCode)
- HS编码信息维护
- 进口比例统计
- 医疗器械分类标识

### 5. 法规关键词管理 (RegulatoryKeyword)
- 监管关键词维护
- 风险等级评估
- 术语解释管理

### 6. 风险评分管理 (RiskScore)
- 综合风险评分计算
- 多维度风险评估
- 风险等级分类

### 7. 监控数据管理 (MonitoringData)
- 市场监控数据收集
- 用户活跃度统计
- 违规事件跟踪

### 8. 告警规则管理 (AlertRule)
- 告警规则配置
- 阈值设置
- 自动告警机制

### 9. 数据源管理 (DataSource)
- 数据源配置
- 数据获取方式管理
- 更新周期设置

## API接口

### 基础CRUD接口
所有模块都提供标准的RESTful API：
- `POST /{module}/page` - 分页查询
- `GET /{module}/list` - 查询所有
- `GET /{module}/{id}` - 根据ID查询
- `POST /{module}` - 新增
- `PUT /{module}/{id}` - 更新
- `DELETE /{module}/{id}` - 删除
- `DELETE /{module}/batch` - 批量删除

### 统计接口
- `GET /statistics/dashboard` - 仪表板统计数据
- `GET /statistics/risk-distribution` - 风险等级分布

## 数据库设计
数据库名：`medical_certification_alert`

主要表结构：
- `products` - 产品信息表
- `markets` - 市场信息表
- `competitors` - 竞品信息表
- `hs_codes` - HS编码表
- `regulatory_keywords` - 法规关键词表
- `risk_scores` - 风险评分表
- `monitoring_data` - 监控数据表
- `alert_rules` - 告警规则表
- `data_sources` - 数据源表

## 配置说明

### 数据库配置
```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/medical_certification_alert
    username: root
    password: 2020
```

### 服务端口
```yaml
server:
  port: 8100
  servlet:
    context-path: /api
```

## 启动方式

### Windows
```bash
start.bat
```

### Linux/Mac
```bash
chmod +x start.sh
./start.sh
```

### 手动启动
```bash
mvn spring-boot:run
```

## API文档访问
启动服务后，可通过以下地址访问API文档：
- **Knife4j文档**：http://localhost:8100/api/doc.html （推荐使用）
- **OpenAPI文档**：http://localhost:8100/api/v3/api-docs

## 注意事项
1. 确保MySQL服务已启动
2. 确保数据库`medical_certification_alert`已创建
3. 确保JDK 17已安装
4. 确保Maven已安装并配置

## 开发建议
1. 使用IDE（如IntelliJ IDEA）导入项目
2. 运行前先执行数据库脚本
3. 根据实际需求修改数据库连接配置
4. 可根据业务需求扩展统计功能 