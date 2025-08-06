CREATE DATABASE IF NOT EXISTS medical_certification_alert;
USE medical_certification_alert;
CREATE TABLE markets (
                         market_id INT PRIMARY KEY AUTO_INCREMENT,
                         country VARCHAR(50) NOT NULL COMMENT '国家/地区',
                         region VARCHAR(50) COMMENT '所属区域（如EU、亚洲）',
                         regulatory_body VARCHAR(100) COMMENT '监管机构（如FDA、NMPA）',
                         regulatory_severity INT COMMENT '监管严格程度（1-10，10为最严）',
                         query_url VARCHAR(255) COMMENT '官方查询地址',
                         update_frequency VARCHAR(20) COMMENT '数据更新频率（如每日、每周）',
                         created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                         updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                         UNIQUE KEY uk_country (country)
);
CREATE TABLE competitors (
                             competitor_id INT PRIMARY KEY AUTO_INCREMENT,
                             competitor_name VARCHAR(100) NOT NULL COMMENT '竞品名称（如Visia、小肤）',
                             product_type VARCHAR(100) COMMENT '产品类型（如Skin Analysis）',
                             is_medical_registered BOOLEAN COMMENT '是否注册为医疗器械',
                             registration_info TEXT COMMENT '注册详情（如证书编号、日期）',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                             UNIQUE KEY uk_competitor (competitor_name)
);
CREATE TABLE products (
                          product_id INT PRIMARY KEY AUTO_INCREMENT,
                          product_name VARCHAR(100) NOT NULL COMMENT '产品名称',
                          product_desc TEXT COMMENT '产品描述',
                          core_functions TEXT COMMENT '核心功能（如3D skin imaging）',
                          hs_code_references VARCHAR(50) COMMENT '参考HS编码（如9018,8543.70）',
                          created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
                          UNIQUE KEY uk_product (product_name)
);
CREATE TABLE hs_codes (
                          hs_code_id INT PRIMARY KEY AUTO_INCREMENT,
                          hs_code VARCHAR(20) NOT NULL COMMENT 'HS编码（如9018）',
                          code_description VARCHAR(255) COMMENT '编码描述',
                          market_id INT COMMENT '关联市场ID',
                          import_ratio DECIMAL(5,2) COMMENT '过去12个月进口比例（%）',
                          is_medical_related BOOLEAN COMMENT '是否倾向医疗器械分类',
                          FOREIGN KEY (market_id) REFERENCES markets(market_id),
                          UNIQUE KEY uk_hs_market (hs_code, market_id)
);
CREATE TABLE regulatory_keywords (
                                     keyword_id INT PRIMARY KEY AUTO_INCREMENT,
                                     keyword VARCHAR(50) NOT NULL COMMENT '关键词（如diagnosis、treatment）',
                                     risk_level INT COMMENT '风险等级（1-10）',
                                     description TEXT COMMENT '术语解释',
                                     UNIQUE KEY uk_keyword (keyword)
);
CREATE TABLE risk_scores (
                             risk_id INT PRIMARY KEY AUTO_INCREMENT,
                             market_id INT NOT NULL COMMENT '关联市场ID',
                             product_id INT NOT NULL COMMENT '关联产品ID',
                             competitor_reg_score DECIMAL(5,2) COMMENT '竞品注册得分（0-10）',
                             hs_code_score DECIMAL(5,2) COMMENT 'HS编码得分（0-10）',
                             regulatory_keyword_score DECIMAL(5,2) COMMENT '法规关键词得分（0-10）',
                             violation_history_score DECIMAL(5,2) COMMENT '违规历史得分（0-10）',
                             total_score DECIMAL(5,2) COMMENT '总得分（加权计算）',
                             risk_level ENUM('低','中','高') COMMENT '风险等级',
                             evaluation_date DATE COMMENT '评估日期',
                             FOREIGN KEY (market_id) REFERENCES markets(market_id),
                             FOREIGN KEY (product_id) REFERENCES products(product_id),
                             UNIQUE KEY uk_market_product_date (market_id, product_id, evaluation_date)
);
CREATE TABLE monitoring_data (
                                 monitor_id INT PRIMARY KEY AUTO_INCREMENT,
                                 market_id INT NOT NULL COMMENT '关联市场ID',
                                 product_id INT NOT NULL COMMENT '关联产品ID',
                                 monitor_date DATE NOT NULL COMMENT '监控日期',
                                 dau DECIMAL(10,2) COMMENT '日活跃用户数',
                                 wau DECIMAL(10,2) COMMENT '周活跃用户数',
                                 complaint_count INT COMMENT '投诉量',
                                 import_violation_count INT COMMENT '进口违规次数',
                                 hs_code_change BOOLEAN COMMENT 'HS编码是否变动',
                                 recall_announcement BOOLEAN COMMENT '是否有召回公告',
                                 FOREIGN KEY (market_id) REFERENCES markets(market_id),
                                 FOREIGN KEY (product_id) REFERENCES products(product_id),
                                 UNIQUE KEY uk_monitor_date (market_id, product_id, monitor_date)
);
CREATE TABLE alert_rules (
                             rule_id INT PRIMARY KEY AUTO_INCREMENT,
                             product_type ENUM('家用','专业') NOT NULL COMMENT '产品类型',
                             market_type ENUM('成熟','新兴') NOT NULL COMMENT '市场类型',
                             dau_drop_threshold DECIMAL(5,2) COMMENT 'DAU下降阈值（%）',
                             wau_drop_threshold DECIMAL(5,2) COMMENT 'WAU下降阈值（%）',
                             complaint_rise_threshold DECIMAL(5,2) COMMENT '投诉量上升阈值（%）',
                             risk_score_threshold DECIMAL(5,2) COMMENT '风险评分阈值',
                             created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                             updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);
CREATE TABLE data_sources (
                              source_id INT PRIMARY KEY AUTO_INCREMENT,
                              source_name VARCHAR(100) NOT NULL COMMENT '来源名称（如openFDA、EUDAMED）',
                              source_type ENUM('API','爬虫','CSV下载','人工') COMMENT '数据获取方式',
                              url VARCHAR(255) COMMENT '来源地址',
                              data_scope TEXT COMMENT '数据覆盖范围',
                              update_cycle VARCHAR(20) COMMENT '更新周期（如每日、每月）',
                              UNIQUE KEY uk_source (source_name)
);
-- 市场及产品关联查询索引
CREATE INDEX idx_market_product ON risk_scores(market_id, product_id);
CREATE INDEX idx_monitor_market_product ON monitoring_data(market_id, product_id);

-- 时间维度查询索引
CREATE INDEX idx_evaluation_date ON risk_scores(evaluation_date);
CREATE INDEX idx_monitor_date ON monitoring_data(monitor_date);

-- 关键词查询索引
CREATE INDEX idx_hs_code ON hs_codes(hs_code);
CREATE INDEX idx_competitor_name ON competitors(competitor_name);