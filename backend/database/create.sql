-- 创建数据库
CREATE DATABASE IF NOT EXISTS risk_monitoring DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE risk_monitoring;

-- 国家表
CREATE TABLE IF NOT EXISTS country (
    country_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL COMMENT '国家名称',
    code VARCHAR(10) NOT NULL COMMENT '国家代码',
    regulatory_level VARCHAR(20) NOT NULL COMMENT '监管严格程度',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '国家信息表';

-- 竞品表
CREATE TABLE IF NOT EXISTS competitor (
    comp_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(200) NOT NULL COMMENT '竞品名称',
    product_type VARCHAR(100) COMMENT '产品类型',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) COMMENT '竞品信息表';

-- HS编码表
CREATE TABLE IF NOT EXISTS hs_code (
    hs_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    code VARCHAR(20) NOT NULL COMMENT 'HS编码',
    description TEXT COMMENT '描述',
    country_id BIGINT NOT NULL COMMENT '国家ID',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
) COMMENT 'HS编码表';

-- Pre-market风险评估表
CREATE TABLE IF NOT EXISTS pre_market_risk (
    risk_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL COMMENT '国家ID',
    comp_id BIGINT NOT NULL COMMENT '竞品ID',
    comp_score INT COMMENT '竞品注册分',
    hs_code_score INT COMMENT 'HS编码分',
    regulatory_score INT COMMENT '法规语义分',
    violation_score INT COMMENT '违规记录分',
    total_score INT COMMENT '总分',
    risk_level VARCHAR(20) COMMENT '风险等级',
    decision VARCHAR(50) COMMENT '决策建议',
    description TEXT COMMENT '描述',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country(country_id),
    FOREIGN KEY (comp_id) REFERENCES competitor(comp_id)
) COMMENT 'Pre-market风险评估表';

-- Post-market监控表
CREATE TABLE IF NOT EXISTS post_market_monitor (
    monitor_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL COMMENT '国家ID',
    recall_announce TEXT COMMENT '召回公告',
    hs_change TEXT COMMENT 'HS编码异动',
    dau INT COMMENT '日活跃用户数',
    wau INT COMMENT '周活跃用户数',
    complaint_count INT COMMENT '投诉数量',
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
) COMMENT 'Post-market监控表';

-- 预警表
CREATE TABLE IF NOT EXISTS early_warning (
    warning_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL COMMENT '国家ID',
    warning_type VARCHAR(50) NOT NULL COMMENT '预警类型',
    trigger_value VARCHAR(200) COMMENT '触发值',
    threshold VARCHAR(200) COMMENT '阈值',
    status VARCHAR(20) DEFAULT '未处理' COMMENT '状态',
    description TEXT COMMENT '描述',
    email_log TEXT COMMENT '邮件记录',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
) COMMENT '预警表';

-- 数据源表
CREATE TABLE IF NOT EXISTS data_source (
    source_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    country_id BIGINT NOT NULL COMMENT '国家ID',
    type VARCHAR(20) NOT NULL COMMENT '类型',
    url VARCHAR(500) COMMENT '数据源地址',
    sync_cycle VARCHAR(20) COMMENT '同步周期',
    last_sync_time DATETIME COMMENT '最后同步时间',
    create_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (country_id) REFERENCES country(country_id)
) COMMENT '数据源表';

-- 插入初始数据
INSERT INTO country (name, code, regulatory_level, description) VALUES
('美国', 'US', 'high', 'FDA监管严格'),
('欧盟', 'EU', 'high', 'EUDAMED监管'),
('中国', 'CN', 'medium', 'NMPA监管'),
('日本', 'JP', 'high', 'PMDA监管'),
('韩国', 'KR', 'medium', 'MFDS监管'),
('澳大利亚', 'AU', 'medium', 'TGA监管'),
('巴西', 'BR', 'medium', 'ANVISA监管');

INSERT INTO competitor (name, product_type, description) VALUES
('Visia', '医疗器械', '眼科诊断设备'),
('MedTech Pro', '医疗器械', '心脏监测设备'),
('HealthCare Plus', '医疗器械', '家用健康监测设备'),
('DiagnosticMaster', '医疗器械', '实验室诊断设备');

INSERT INTO hs_code (code, description, country_id) VALUES
('9018.11', '眼科诊断设备', 1),
('9018.12', '眼科治疗设备', 1),
('9018.20', '心脏监测设备', 2),
('9018.21', '心脏治疗设备', 2),
('9018.30', '家用健康设备', 3),
('9018.31', '实验室设备', 3);

INSERT INTO data_source (country_id, type, url, sync_cycle) VALUES
(1, 'API', 'https://api.fda.gov', 'daily'),
(2, 'CSV', 'https://ec.europa.eu/health/documents/eudamed', 'weekly'),
(3, 'Crawler', 'https://www.nmpa.gov.cn', 'weekly'),
(4, 'API', 'https://www.pmda.go.jp/api', 'daily'),
(5, 'Crawler', 'https://www.mfds.go.kr', 'weekly'),
(6, 'CSV', 'https://www.tga.gov.au', 'monthly'),
(7, 'CSV', 'https://www.gov.br/anvisa', 'monthly'); 