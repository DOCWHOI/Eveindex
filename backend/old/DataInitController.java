package com.eveindex.controller;

import com.eveindex.dto.BaseResponse;
import com.eveindex.entity.*;
import com.eveindex.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 数据初始化控制器
 */
@Tag(name = "数据初始化", description = "用于初始化系统基础数据的接口")
@RestController
@RequestMapping("/data-init")
public class DataInitController {

    @Autowired
    private MarketService marketService;
    
    @Autowired
    private ProductService productService;
    
    @Autowired
    private CompetitorService competitorService;
    
    @Autowired
    private HsCodeService hsCodeService;
    
    @Autowired
    private RegulatoryKeywordService regulatoryKeywordService;
    
    @Autowired
    private DataSourceService dataSourceService;
    
    @Autowired
    private AlertRuleService alertRuleService;
    
    @Autowired
    private RiskScoreService riskScoreService;
    
    @Autowired
    private MonitoringDataService monitoringDataService;

    /**
     * 初始化所有基础数据
     */
    @Operation(summary = "初始化所有基础数据", description = "一键初始化系统所需的所有基础数据")
    @PostMapping("/all")
    public BaseResponse<String> initAllData() {
        try {
            initMarkets();
            initProducts();
            initCompetitors();
            initHsCodes();
            initRegulatoryKeywords();
            initDataSources();
            initAlertRules();
            initSampleRiskScores();
            initSampleMonitoringData();
            
            return BaseResponse.success("所有基础数据初始化完成！");
        } catch (Exception e) {
            return BaseResponse.error("数据初始化失败: " + e.getMessage());
        }
    }

    /**
     * 初始化市场数据
     */
    @Operation(summary = "初始化市场数据", description = "初始化全球主要市场的监管信息")
    @PostMapping("/markets")
    public BaseResponse<String> initMarkets() {
        List<Market> markets = new ArrayList<>();
        
        markets.add(createMarket("美国", "北美", "FDA", 9, "https://www.fda.gov/medical-devices", "每日"));
        markets.add(createMarket("中国", "亚洲", "NMPA", 8, "https://www.nmpa.gov.cn", "每日"));
        markets.add(createMarket("德国", "欧洲", "BfArM", 8, "https://www.bfarm.de", "每周"));
        markets.add(createMarket("日本", "亚洲", "PMDA", 8, "https://www.pmda.go.jp", "每周"));
        markets.add(createMarket("英国", "欧洲", "MHRA", 7, "https://www.gov.uk/government/organisations/medicines-and-healthcare-products-regulatory-agency", "每周"));
        
        marketService.saveBatch(markets);
        return BaseResponse.success("市场数据初始化完成，共 " + markets.size() + " 条记录");
    }

    /**
     * 初始化产品数据
     */
    @Operation(summary = "初始化产品数据", description = "初始化医疗器械产品信息")
    @PostMapping("/products")
    public BaseResponse<String> initProducts() {
        List<Product> products = new ArrayList<>();
        
        products.add(createProduct("皮肤分析仪", "基于AI的皮肤健康分析设备", "3D皮肤成像,色素分析,皱纹检测", "9018,8543.70"));
        products.add(createProduct("智能血压计", "家用智能血压监测设备", "血压测量,心率监测,数据同步", "9018.10"));
        products.add(createProduct("血糖仪", "便携式血糖监测设备", "血糖检测,数据记录,趋势分析", "9018.31"));
        products.add(createProduct("体温枪", "非接触式体温测量设备", "红外测温,快速检测,发热预警", "9025.19"));
        products.add(createProduct("心电图机", "便携式心电监测设备", "心电信号采集,心律分析,异常预警", "9018.11"));
        
        productService.saveBatch(products);
        return BaseResponse.success("产品数据初始化完成，共 " + products.size() + " 条记录");
    }

    /**
     * 初始化竞品数据
     */
    @Operation(summary = "初始化竞品数据", description = "初始化竞争产品信息")
    @PostMapping("/competitors")
    public BaseResponse<String> initCompetitors() {
        List<Competitor> competitors = new ArrayList<>();
        
        competitors.add(createCompetitor("Visia皮肤分析仪", "皮肤分析", true, "FDA 510(k): K123456789, 注册日期: 2020-01-15"));
        competitors.add(createCompetitor("小肤智能镜", "皮肤分析", false, "未注册为医疗器械"));
        competitors.add(createCompetitor("欧姆龙血压计", "血压监测", true, "FDA 510(k): K987654321, 注册日期: 2019-03-20"));
        competitors.add(createCompetitor("鱼跃血糖仪", "血糖检测", true, "NMPA注册证: 国械注准20190001, 注册日期: 2019-05-10"));
        competitors.add(createCompetitor("博朗体温枪", "体温测量", true, "CE认证: CE0123, 注册日期: 2018-12-01"));
        
        competitorService.saveBatch(competitors);
        return BaseResponse.success("竞品数据初始化完成，共 " + competitors.size() + " 条记录");
    }

    /**
     * 初始化法规关键词数据
     */
    @Operation(summary = "初始化法规关键词", description = "初始化医疗器械法规相关关键词")
    @PostMapping("/keywords")
    public BaseResponse<String> initRegulatoryKeywords() {
        List<RegulatoryKeyword> keywords = new ArrayList<>();
        
        keywords.add(createKeyword("diagnosis", 9, "诊断功能 - 高风险医疗器械关键词"));
        keywords.add(createKeyword("treatment", 9, "治疗功能 - 高风险医疗器械关键词"));
        keywords.add(createKeyword("therapeutic", 8, "治疗性的 - 中高风险医疗器械关键词"));
        keywords.add(createKeyword("medical device", 7, "医疗器械 - 中风险关键词"));
        keywords.add(createKeyword("clinical", 7, "临床 - 中风险关键词"));
        
        regulatoryKeywordService.saveBatch(keywords);
        return BaseResponse.success("法规关键词初始化完成，共 " + keywords.size() + " 条记录");
    }

    // 辅助方法
    private Market createMarket(String country, String region, String regulatoryBody, Integer severity, String queryUrl, String updateFreq) {
        Market market = new Market();
        market.setCountry(country);
        market.setRegion(region);
        market.setRegulatoryBody(regulatoryBody);
        market.setRegulatorySeverity(severity);
        market.setQueryUrl(queryUrl);
        market.setUpdateFrequency(updateFreq);
        return market;
    }
    
    private Product createProduct(String name, String desc, String functions, String hsCode) {
        Product product = new Product();
        product.setProductName(name);
        product.setProductDesc(desc);
        product.setCoreFunctions(functions);
        product.setHsCodeReferences(hsCode);
        return product;
    }
    
    private Competitor createCompetitor(String name, String type, Boolean isRegistered, String regInfo) {
        Competitor competitor = new Competitor();
        competitor.setCompetitorName(name);
        competitor.setProductType(type);
        competitor.setIsMedicalRegistered(isRegistered);
        competitor.setRegistrationInfo(regInfo);
        return competitor;
    }
    
    private RegulatoryKeyword createKeyword(String keyword, Integer riskLevel, String description) {
        RegulatoryKeyword regulatoryKeyword = new RegulatoryKeyword();
        regulatoryKeyword.setKeyword(keyword);
        regulatoryKeyword.setRiskLevel(riskLevel);
        regulatoryKeyword.setDescription(description);
        return regulatoryKeyword;
    }
    
    private void initHsCodes() {
        // HS编码初始化逻辑
    }
    
    private void initDataSources() {
        // 数据源初始化逻辑
    }
    
    private void initAlertRules() {
        // 预警规则初始化逻辑
    }
    
    private void initSampleRiskScores() {
        // 示例风险评分初始化逻辑
    }
    
    private void initSampleMonitoringData() {
        // 示例监控数据初始化逻辑
    }
}