package com.eveindex.controller;

import com.eveindex.dto.BaseResponse;
import com.eveindex.service.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 统计控制器
 */
@Tag(name = "统计管理", description = "系统统计数据接口")
@RestController
@RequestMapping("/statistics")
public class StatisticsController {

    @Autowired
    private ProductService productService;
    
    @Autowired
    private MarketService marketService;
    
    @Autowired
    private CompetitorService competitorService;
    
    @Autowired
    private RiskScoreService riskScoreService;
    
    @Autowired
    private MonitoringDataService monitoringDataService;

    /**
     * 获取仪表板统计数据
     */
    @Operation(summary = "获取仪表板统计数据", description = "获取系统各模块的基础统计数据")
    @GetMapping("/dashboard")
    public BaseResponse<Map<String, Object>> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        // 统计各模块数据量
        stats.put("productCount", productService.count());
        stats.put("marketCount", marketService.count());
        stats.put("competitorCount", competitorService.count());
        stats.put("riskScoreCount", riskScoreService.count());
        stats.put("monitoringDataCount", monitoringDataService.count());
        
        return BaseResponse.success(stats);
    }

    /**
     * 获取风险等级分布
     */
    @Operation(summary = "获取风险等级分布", description = "获取风险评分的等级分布情况")
    @GetMapping("/risk-distribution")
    public BaseResponse<Map<String, Object>> getRiskDistribution() {
        Map<String, Object> distribution = new HashMap<>();
        
        // 这里可以添加具体的风险等级统计逻辑
        // 示例数据
        distribution.put("low", 30);
        distribution.put("medium", 45);
        distribution.put("high", 25);
        
        return BaseResponse.success(distribution);
    }
} 