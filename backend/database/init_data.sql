-- 数据初始化脚本
USE medical_certification_alert;

-- 1. 初始化市场数据
INSERT INTO markets (country, region, regulatory_body, regulatory_severity, query_url, update_frequency) VALUES
('美国', '北美', 'FDA', 9, 'https://www.fda.gov/medical-devices', '每日'),
('中国', '亚洲', 'NMPA', 8, 'https://www.nmpa.gov.cn', '每日'),
('德国', '欧洲', 'BfArM', 8, 'https://www.bfarm.de', '每周'),
('日本', '亚洲', 'PMDA', 8, 'https://www.pmda.go.jp', '每周'),
('英国', '欧洲', 'MHRA', 7, 'https://www.gov.uk/government/organisations/medicines-and-healthcare-products-regulatory-agency', '每周'),
('加拿大', '北美', 'Health Canada', 7, 'https://www.canada.ca/en/health-canada', '每周'),
('澳大利亚', '大洋洲', 'TGA', 7, 'https://www.tga.gov.au', '每月'),
('韩国', '亚洲', 'MFDS', 6, 'https://www.mfds.go.kr', '每月'),
('印度', '亚洲', 'CDSCO', 5, 'https://cdsco.gov.in', '每月'),
('巴西', '南美', 'ANVISA', 5, 'https://www.gov.br/anvisa', '每月');

-- 2. 初始化产品数据
INSERT INTO products (product_name, product_desc, core_functions, hs_code_references) VALUES
('皮肤分析仪', '基于AI的皮肤健康分析设备', '3D皮肤成像,色素分析,皱纹检测', '9018,8543.70'),
('智能血压计', '家用智能血压监测设备', '血压测量,心率监测,数据同步', '9018.10'),
('血糖仪', '便携式血糖监测设备', '血糖检测,数据记录,趋势分析', '9018.31'),
('体温枪', '非接触式体温测量设备', '红外测温,快速检测,发热预警', '9025.19'),
('心电图机', '便携式心电监测设备', '心电信号采集,心律分析,异常预警', '9018.11'),
('超声诊断仪', '便携式超声成像设备', '超声成像,多普勒检测,图像分析', '9018.12'),
('呼吸机', '医用呼吸辅助设备', '呼吸支持,参数监测,报警系统', '9019.20'),
('除颤器', '自动体外除颤器', '心脏除颤,心律监测,急救指导', '9018.19'),
('胰岛素泵', '可穿戴胰岛素输注设备', '胰岛素输注,血糖联动,智能调节', '9018.31'),
('助听器', '数字式助听设备', '声音放大,噪音消除,蓝牙连接', '9021.40');

-- 3. 初始化竞品数据
INSERT INTO competitors (competitor_name, product_type, is_medical_registered, registration_info) VALUES
('Visia皮肤分析仪', '皮肤分析', TRUE, 'FDA 510(k): K123456789, 注册日期: 2020-01-15'),
('小肤智能镜', '皮肤分析', FALSE, '未注册为医疗器械'),
('欧姆龙血压计', '血压监测', TRUE, 'FDA 510(k): K987654321, 注册日期: 2019-03-20'),
('鱼跃血糖仪', '血糖检测', TRUE, 'NMPA注册证: 国械注准20190001, 注册日期: 2019-05-10'),
('博朗体温枪', '体温测量', TRUE, 'CE认证: CE0123, 注册日期: 2018-12-01'),
('理邦心电图机', '心电监测', TRUE, 'NMPA注册证: 国械注准20180002, 注册日期: 2018-08-15'),
('迈瑞超声仪', '超声成像', TRUE, 'FDA 510(k): K876543210, 注册日期: 2017-11-30'),
('飞利浦呼吸机', '呼吸治疗', TRUE, 'FDA 510(k): K765432109, 注册日期: 2020-04-25'),
('美敦力除颤器', '急救设备', TRUE, 'FDA PMA: P123456, 注册日期: 2019-09-12'),
('美敦力胰岛素泵', '糖尿病管理', TRUE, 'FDA PMA: P234567, 注册日期: 2018-06-08');

-- 4. 初始化HS编码数据
INSERT INTO hs_codes (hs_code, code_description, market_id, import_ratio, is_medical_related) VALUES
('9018', '医疗、外科或兽医用仪器及器具', 1, 15.5, TRUE),
('9018.10', '心电图记录仪', 1, 8.2, TRUE),
('9018.11', '心电图记录仪', 1, 5.3, TRUE),
('9018.12', '超声波扫描设备', 1, 12.1, TRUE),
('9018.19', '其他心电图记录仪', 1, 3.8, TRUE),
('9018.31', '注射器', 1, 22.4, TRUE),
('8543.70', '其他电气设备', 1, 6.7, FALSE),
('9025.19', '其他温度计', 1, 4.2, FALSE),
('9019.20', '臭氧治疗、氧气治疗设备', 1, 2.1, TRUE),
('9021.40', '助听器', 1, 1.8, TRUE);

-- 5. 初始化法规关键词数据
INSERT INTO regulatory_keywords (keyword, risk_level, description) VALUES
('diagnosis', 9, '诊断功能 - 高风险医疗器械关键词'),
('treatment', 9, '治疗功能 - 高风险医疗器械关键词'),
('therapeutic', 8, '治疗性的 - 中高风险医疗器械关键词'),
('medical device', 7, '医疗器械 - 中风险关键词'),
('clinical', 7, '临床 - 中风险关键词'),
('FDA approved', 6, 'FDA批准 - 中风险关键词'),
('CE marking', 5, 'CE标志 - 中低风险关键词'),
('health monitoring', 4, '健康监测 - 中低风险关键词'),
('wellness', 3, '健康 - 低风险关键词'),
('cosmetic', 2, '美容 - 低风险关键词');

-- 6. 初始化数据源信息
INSERT INTO data_sources (source_name, source_type, url, data_scope, update_cycle) VALUES
('openFDA', 'API', 'https://open.fda.gov/apis/', '美国FDA医疗器械数据', '每日'),
('EUDAMED', '爬虫', 'https://ec.europa.eu/tools/eudamed/', '欧盟医疗器械数据', '每周'),
('NMPA官网', '爬虫', 'https://www.nmpa.gov.cn', '中国NMPA医疗器械数据', '每日'),
('海关数据', 'CSV下载', 'https://customs.gov.cn', '进出口贸易数据', '每月'),
('专利数据库', 'API', 'https://patents.google.com', '医疗器械相关专利', '每周'),
('FDA Warning Letters', '爬虫', 'https://www.fda.gov/inspections-compliance-enforcement-and-criminal-investigations/warning-letters', 'FDA警告信数据', '每日'),
('FDA Recalls', 'API', 'https://www.fda.gov/safety/recalls-market-withdrawals-safety-alerts', 'FDA召回数据', '每日'),
('PubMed', 'API', 'https://pubmed.ncbi.nlm.nih.gov/', '医学文献数据', '每日'),
('Google Trends', 'API', 'https://trends.google.com', '搜索趋势数据', '每日'),
('社交媒体监控', '爬虫', 'https://twitter.com', '社交媒体提及数据', '每小时');

-- 7. 初始化预警规则
INSERT INTO alert_rules (product_type, market_type, dau_drop_threshold, wau_drop_threshold, complaint_rise_threshold, risk_score_threshold) VALUES
('家用', '成熟', 20.0, 15.0, 50.0, 7.0),
('家用', '新兴', 30.0, 25.0, 100.0, 6.0),
('专业', '成熟', 15.0, 10.0, 30.0, 8.0),
('专业', '新兴', 25.0, 20.0, 80.0, 7.0);

-- 8. 初始化一些示例监控数据（最近7天）
INSERT INTO monitoring_data (market_id, product_id, monitor_date, dau, wau, complaint_count, import_violation_count, hs_code_change, recall_announcement) VALUES
-- 皮肤分析仪在美国市场的数据
(1, 1, '2024-01-01', 1250.0, 8750.0, 3, 0, FALSE, FALSE),
(1, 1, '2024-01-02', 1180.0, 8650.0, 2, 0, FALSE, FALSE),
(1, 1, '2024-01-03', 1320.0, 8800.0, 1, 0, FALSE, FALSE),
(1, 1, '2024-01-04', 1280.0, 8900.0, 4, 1, FALSE, FALSE),
(1, 1, '2024-01-05', 1150.0, 8750.0, 6, 0, FALSE, FALSE),
(1, 1, '2024-01-06', 1090.0, 8600.0, 8, 0, FALSE, TRUE),
(1, 1, '2024-01-07', 980.0, 8400.0, 12, 2, TRUE, FALSE),

-- 血压计在中国市场的数据
(2, 2, '2024-01-01', 2100.0, 14700.0, 1, 0, FALSE, FALSE),
(2, 2, '2024-01-02', 2050.0, 14600.0, 2, 0, FALSE, FALSE),
(2, 2, '2024-01-03', 2200.0, 14900.0, 0, 0, FALSE, FALSE),
(2, 2, '2024-01-04', 2180.0, 15100.0, 1, 0, FALSE, FALSE),
(2, 2, '2024-01-05', 2250.0, 15200.0, 3, 0, FALSE, FALSE),
(2, 2, '2024-01-06', 2300.0, 15400.0, 2, 0, FALSE, FALSE),
(2, 2, '2024-01-07', 2350.0, 15600.0, 1, 0, FALSE, FALSE);

-- 9. 初始化风险评分数据
INSERT INTO risk_scores (market_id, product_id, competitor_reg_score, hs_code_score, regulatory_keyword_score, violation_history_score, total_score, risk_level, evaluation_date) VALUES
-- 皮肤分析仪的风险评分
(1, 1, 6.5, 7.2, 8.5, 5.5, 7.1, '高', '2024-01-07'),
(2, 1, 5.8, 6.5, 7.8, 4.2, 6.2, '中', '2024-01-07'),
(3, 1, 6.2, 6.8, 7.5, 4.8, 6.5, '中', '2024-01-07'),

-- 血压计的风险评分
(1, 2, 3.5, 4.2, 5.5, 2.8, 4.1, '低', '2024-01-07'),
(2, 2, 4.2, 4.8, 6.2, 3.5, 4.8, '低', '2024-01-07'),
(3, 2, 4.0, 4.5, 5.8, 3.2, 4.5, '低', '2024-01-07'),

-- 血糖仪的风险评分
(1, 3, 4.8, 5.5, 6.8, 4.2, 5.5, '中', '2024-01-07'),
(2, 3, 5.2, 5.8, 7.2, 4.8, 5.9, '中', '2024-01-07'),

-- 体温枪的风险评分
(1, 4, 2.5, 3.2, 4.5, 2.0, 3.2, '低', '2024-01-07'),
(2, 4, 3.0, 3.5, 5.0, 2.5, 3.6, '低', '2024-01-07');

-- 创建视图：风险评分概览
CREATE VIEW risk_overview AS
SELECT 
    m.country,
    p.product_name,
    rs.total_score,
    rs.risk_level,
    rs.evaluation_date,
    CASE 
        WHEN rs.total_score >= 7.0 THEN '高风险'
        WHEN rs.total_score >= 5.0 THEN '中风险'
        ELSE '低风险'
    END as risk_category
FROM risk_scores rs
JOIN markets m ON rs.market_id = m.market_id
JOIN products p ON rs.product_id = p.product_id
WHERE rs.evaluation_date = (SELECT MAX(evaluation_date) FROM risk_scores WHERE market_id = rs.market_id AND product_id = rs.product_id);

-- 创建视图：监控数据趋势
CREATE VIEW monitoring_trend AS
SELECT 
    m.country,
    p.product_name,
    md.monitor_date,
    md.dau,
    md.wau,
    md.complaint_count,
    CASE 
        WHEN md.recall_announcement = TRUE THEN '有召回'
        ELSE '无召回'
    END as recall_status
FROM monitoring_data md
JOIN markets m ON md.market_id = m.market_id
JOIN products p ON md.product_id = p.product_id
ORDER BY md.monitor_date DESC;

-- 插入完成提示
SELECT '数据初始化完成！' as message,
       (SELECT COUNT(*) FROM markets) as markets_count,
       (SELECT COUNT(*) FROM products) as products_count,
       (SELECT COUNT(*) FROM competitors) as competitors_count,
       (SELECT COUNT(*) FROM risk_scores) as risk_scores_count,
       (SELECT COUNT(*) FROM monitoring_data) as monitoring_data_count;