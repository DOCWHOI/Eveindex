package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.Market;
import com.eveindex.mapper.MarketMapper;
import com.eveindex.service.MarketService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 市场服务实现类
 */
@Service
public class MarketServiceImpl extends ServiceImpl<MarketMapper, Market> implements MarketService {

    @Override
    public Market getByCountry(String country) {
        if (!StringUtils.hasText(country)) {
            return null;
        }
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Market::getCountry, country);
        return getOne(queryWrapper);
    }

    @Override
    public List<Market> getByRegion(String region) {
        if (!StringUtils.hasText(region)) {
            return List.of();
        }
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Market::getRegion, region);
        queryWrapper.orderByDesc(Market::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<Market> getByRegulatoryBody(String regulatoryBody) {
        if (!StringUtils.hasText(regulatoryBody)) {
            return List.of();
        }
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Market::getRegulatoryBody, regulatoryBody);
        queryWrapper.orderByDesc(Market::getRegulatorySeverity);
        return list(queryWrapper);
    }

    @Override
    public List<Market> getByRegulatorySeverity(Integer minSeverity, Integer maxSeverity) {
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        
        if (minSeverity != null) {
            queryWrapper.ge(Market::getRegulatorySeverity, minSeverity);
        }
        if (maxSeverity != null) {
            queryWrapper.le(Market::getRegulatorySeverity, maxSeverity);
        }
        
        queryWrapper.orderByDesc(Market::getRegulatorySeverity);
        return list(queryWrapper);
    }

    @Override
    public List<Market> getHighRiskMarkets() {
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(Market::getRegulatorySeverity, 8);
        queryWrapper.orderByDesc(Market::getRegulatorySeverity);
        return list(queryWrapper);
    }

    @Override
    public List<Market> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Market::getCountry, keyword)
                   .or()
                   .like(Market::getRegion, keyword)
                   .or()
                   .like(Market::getRegulatoryBody, keyword);
        
        queryWrapper.orderByDesc(Market::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public Page<Market> getMarketPage(int current, int size, String keyword) {
        Page<Market> page = new Page<>(current, size);
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Market::getCountry, keyword)
                       .or()
                       .like(Market::getRegion, keyword)
                       .or()
                       .like(Market::getRegulatoryBody, keyword);
        }
        
        queryWrapper.orderByDesc(Market::getCreatedAt);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsByCountry(String country) {
        if (!StringUtils.hasText(country)) {
            return false;
        }
        
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Market::getCountry, country);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllRegions() {
        LambdaQueryWrapper<Market> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Market::getRegion);
        queryWrapper.isNotNull(Market::getRegion);
        
        return list(queryWrapper).stream()
                .map(Market::getRegion)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getMarketCount() {
        return count();
    }
} 