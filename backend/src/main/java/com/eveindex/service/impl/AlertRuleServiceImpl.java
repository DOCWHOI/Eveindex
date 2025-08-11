package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.AlertRule;
import com.eveindex.mapper.AlertRuleMapper;
import com.eveindex.service.AlertRuleService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 告警规则服务实现类
 */
@Service
public class AlertRuleServiceImpl extends ServiceImpl<AlertRuleMapper, AlertRule> implements AlertRuleService {

    @Override
    public List<AlertRule> getByProductType(String productType) {
        if (!StringUtils.hasText(productType)) {
            return List.of();
        }
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlertRule::getProductType, productType);
        queryWrapper.orderByDesc(AlertRule::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<AlertRule> getByMarketType(String marketType) {
        if (!StringUtils.hasText(marketType)) {
            return List.of();
        }
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlertRule::getMarketType, marketType);
        queryWrapper.orderByDesc(AlertRule::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public AlertRule getByProductAndMarketType(String productType, String marketType) {
        if (!StringUtils.hasText(productType) || !StringUtils.hasText(marketType)) {
            return null;
        }
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlertRule::getProductType, productType)
                   .eq(AlertRule::getMarketType, marketType);
        return getOne(queryWrapper);
    }

    @Override
    public List<AlertRule> getActiveRules() {
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        // 假设有一个启用状态字段，这里简单返回所有规则
        queryWrapper.orderByDesc(AlertRule::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<AlertRule> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(AlertRule::getProductType, keyword)
                   .or()
                   .like(AlertRule::getMarketType, keyword);
        
        queryWrapper.orderByDesc(AlertRule::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public Page<AlertRule> getAlertRulePage(int current, int size, String keyword) {
        Page<AlertRule> page = new Page<>(current, size);
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(AlertRule::getProductType, keyword)
                       .or()
                       .like(AlertRule::getMarketType, keyword);
        }
        
        queryWrapper.orderByDesc(AlertRule::getCreatedAt);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsByProductAndMarketType(String productType, String marketType) {
        if (!StringUtils.hasText(productType) || !StringUtils.hasText(marketType)) {
            return false;
        }
        
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(AlertRule::getProductType, productType)
                   .eq(AlertRule::getMarketType, marketType);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllProductTypes() {
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AlertRule::getProductType);
        queryWrapper.isNotNull(AlertRule::getProductType);
        
        return list(queryWrapper).stream()
                .map(AlertRule::getProductType)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public List<String> getAllMarketTypes() {
        LambdaQueryWrapper<AlertRule> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(AlertRule::getMarketType);
        queryWrapper.isNotNull(AlertRule::getMarketType);
        
        return list(queryWrapper).stream()
                .map(AlertRule::getMarketType)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getAlertRuleCount() {
        return count();
    }
}