package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.common.exception.BusinessException;
import com.eveindex.entity.RiskScore;
import com.eveindex.mapper.RiskScoreMapper;
import com.eveindex.service.RiskScoreService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * 风险评分服务实现类
 */
@Service
public class RiskScoreServiceImpl extends ServiceImpl<RiskScoreMapper, RiskScore> implements RiskScoreService {

    @Override
    public List<RiskScore> getByMarketAndProduct(Integer marketId, Integer productId) {
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        
        if (marketId != null) {
            queryWrapper.eq(RiskScore::getMarketId, marketId);
        }
        if (productId != null) {
            queryWrapper.eq(RiskScore::getProductId, productId);
        }
        
        queryWrapper.orderByDesc(RiskScore::getEvaluationDate);
        return list(queryWrapper);
    }

    @Override
    public List<RiskScore> getByRiskLevel(String riskLevel) {
        if (!StringUtils.hasText(riskLevel)) {
            return List.of();
        }
        
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RiskScore::getRiskLevel, riskLevel);
        queryWrapper.orderByDesc(RiskScore::getTotalScore);
        return list(queryWrapper);
    }

    @Override
    public RiskScore getLatestScore(Integer marketId, Integer productId) {
        if (marketId == null || productId == null) {
            return null;
        }
        
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RiskScore::getMarketId, marketId)
                   .eq(RiskScore::getProductId, productId)
                   .orderByDesc(RiskScore::getEvaluationDate)
                   .last("LIMIT 1");
        
        return getOne(queryWrapper);
    }

    @Override
    public BigDecimal calculateTotalScore(BigDecimal competitorScore, BigDecimal hsCodeScore, 
                                         BigDecimal keywordScore, BigDecimal violationScore) {
        // 权重配置：竞品30%、HS编码25%、关键词25%、违规历史20%
        BigDecimal competitorWeight = new BigDecimal("0.30");
        BigDecimal hsCodeWeight = new BigDecimal("0.25");
        BigDecimal keywordWeight = new BigDecimal("0.25");
        BigDecimal violationWeight = new BigDecimal("0.20");
        
        BigDecimal totalScore = BigDecimal.ZERO;
        
        if (competitorScore != null) {
            totalScore = totalScore.add(competitorScore.multiply(competitorWeight));
        }
        if (hsCodeScore != null) {
            totalScore = totalScore.add(hsCodeScore.multiply(hsCodeWeight));
        }
        if (keywordScore != null) {
            totalScore = totalScore.add(keywordScore.multiply(keywordWeight));
        }
        if (violationScore != null) {
            totalScore = totalScore.add(violationScore.multiply(violationWeight));
        }
        
        return totalScore.setScale(2, RoundingMode.HALF_UP);
    }

    @Override
    public String determineRiskLevel(BigDecimal totalScore) {
        if (totalScore == null) {
            return "低";
        }
        
        if (totalScore.compareTo(new BigDecimal("7.0")) >= 0) {
            return "高";
        } else if (totalScore.compareTo(new BigDecimal("4.0")) >= 0) {
            return "中";
        } else {
            return "低";
        }
    }

    @Override
    public List<RiskScore> getRiskTrend(Integer marketId, Integer productId, LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        
        if (marketId != null) {
            queryWrapper.eq(RiskScore::getMarketId, marketId);
        }
        if (productId != null) {
            queryWrapper.eq(RiskScore::getProductId, productId);
        }
        if (startDate != null) {
            queryWrapper.ge(RiskScore::getEvaluationDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(RiskScore::getEvaluationDate, endDate);
        }
        
        queryWrapper.orderByAsc(RiskScore::getEvaluationDate);
        return list(queryWrapper);
    }

    @Override
    public List<RiskScore> getHighRiskScores() {
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RiskScore::getRiskLevel, "高")
                   .orderByDesc(RiskScore::getTotalScore);
        return list(queryWrapper);
    }

    @Override
    public Map<String, Long> getRiskLevelDistribution() {
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RiskScore::getRiskLevel);
        
        List<RiskScore> riskScores = list(queryWrapper);
        
        Map<String, Long> distribution = new HashMap<>();
        distribution.put("高", riskScores.stream().filter(r -> "高".equals(r.getRiskLevel())).count());
        distribution.put("中", riskScores.stream().filter(r -> "中".equals(r.getRiskLevel())).count());
        distribution.put("低", riskScores.stream().filter(r -> "低".equals(r.getRiskLevel())).count());
        
        return distribution;
    }

    @Override
    public boolean batchCalculateRiskScores(List<RiskScore> riskScores) {
        if (riskScores == null || riskScores.isEmpty()) {
            throw new BusinessException("风险评分数据不能为空");
        }
        
        // 计算每个风险评分的总分和风险等级
        for (RiskScore riskScore : riskScores) {
            BigDecimal totalScore = calculateTotalScore(
                riskScore.getCompetitorRegScore(),
                riskScore.getHsCodeScore(),
                riskScore.getRegulatoryKeywordScore(),
                riskScore.getViolationHistoryScore()
            );
            
            riskScore.setTotalScore(totalScore);
            riskScore.setRiskLevel(determineRiskLevel(totalScore));
        }
        
        return saveOrUpdateBatch(riskScores);
    }

    @Override
    public Page<RiskScore> getRiskScorePage(int current, int size, String riskLevel) {
        Page<RiskScore> page = new Page<>(current, size);
        
        LambdaQueryWrapper<RiskScore> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(riskLevel)) {
            queryWrapper.eq(RiskScore::getRiskLevel, riskLevel);
        }
        
        queryWrapper.orderByDesc(RiskScore::getEvaluationDate);
        return page(page, queryWrapper);
    }

    @Override
    public long getRiskScoreCount() {
        return count();
    }
}