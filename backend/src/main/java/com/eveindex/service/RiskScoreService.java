package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.RiskScore;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 风险评分服务接口
 */
public interface RiskScoreService extends IService<RiskScore> {
    
    /**
     * 根据市场和产品查询风险评分
     */
    List<RiskScore> getByMarketAndProduct(Integer marketId, Integer productId);
    
    /**
     * 根据风险等级查询风险评分
     */
    List<RiskScore> getByRiskLevel(String riskLevel);
    
    /**
     * 获取最新的风险评分
     */
    RiskScore getLatestScore(Integer marketId, Integer productId);
    
    /**
     * 计算综合风险评分
     */
    BigDecimal calculateTotalScore(BigDecimal competitorScore, BigDecimal hsCodeScore, 
                                  BigDecimal keywordScore, BigDecimal violationScore);
    
    /**
     * 根据总分确定风险等级
     */
    String determineRiskLevel(BigDecimal totalScore);
    
    /**
     * 获取风险评分趋势
     */
    List<RiskScore> getRiskTrend(Integer marketId, Integer productId, LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取高风险评分列表
     */
    List<RiskScore> getHighRiskScores();
    
    /**
     * 获取风险等级分布统计
     */
    Map<String, Long> getRiskLevelDistribution();
    
    /**
     * 批量计算风险评分
     */
    boolean batchCalculateRiskScores(List<RiskScore> riskScores);
    
    /**
     * 获取风险评分分页数据
     */
    Page<RiskScore> getRiskScorePage(int current, int size, String riskLevel);
    
    /**
     * 获取风险评分统计数据
     */
    long getRiskScoreCount();
}