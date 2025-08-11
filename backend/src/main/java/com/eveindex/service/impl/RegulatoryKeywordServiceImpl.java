package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.RegulatoryKeyword;
import com.eveindex.mapper.RegulatoryKeywordMapper;
import com.eveindex.service.RegulatoryKeywordService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 法规关键词服务实现类
 */
@Service
public class RegulatoryKeywordServiceImpl extends ServiceImpl<RegulatoryKeywordMapper, RegulatoryKeyword> implements RegulatoryKeywordService {

    @Override
    public RegulatoryKeyword getByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return null;
        }
        
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegulatoryKeyword::getKeyword, keyword);
        return getOne(queryWrapper);
    }

    @Override
    public List<RegulatoryKeyword> getByRiskLevel(Integer riskLevel) {
        if (riskLevel == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegulatoryKeyword::getRiskLevel, riskLevel);
        queryWrapper.orderByAsc(RegulatoryKeyword::getKeyword);
        return list(queryWrapper);
    }

    @Override
    public List<RegulatoryKeyword> getHighRiskKeywords() {
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.ge(RegulatoryKeyword::getRiskLevel, 8);
        queryWrapper.orderByDesc(RegulatoryKeyword::getRiskLevel);
        return list(queryWrapper);
    }

    @Override
    public List<RegulatoryKeyword> getByRiskLevelRange(Integer minLevel, Integer maxLevel) {
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        
        if (minLevel != null) {
            queryWrapper.ge(RegulatoryKeyword::getRiskLevel, minLevel);
        }
        if (maxLevel != null) {
            queryWrapper.le(RegulatoryKeyword::getRiskLevel, maxLevel);
        }
        
        queryWrapper.orderByDesc(RegulatoryKeyword::getRiskLevel);
        return list(queryWrapper);
    }

    @Override
    public List<RegulatoryKeyword> searchByKeyword(String searchKeyword) {
        if (!StringUtils.hasText(searchKeyword)) {
            return list();
        }
        
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(RegulatoryKeyword::getKeyword, searchKeyword)
                   .or()
                   .like(RegulatoryKeyword::getDescription, searchKeyword);
        
        queryWrapper.orderByDesc(RegulatoryKeyword::getRiskLevel);
        return list(queryWrapper);
    }

    @Override
    public Page<RegulatoryKeyword> getKeywordPage(int current, int size, String keyword) {
        Page<RegulatoryKeyword> page = new Page<>(current, size);
        
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(RegulatoryKeyword::getKeyword, keyword)
                       .or()
                       .like(RegulatoryKeyword::getDescription, keyword);
        }
        
        queryWrapper.orderByDesc(RegulatoryKeyword::getRiskLevel);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return false;
        }
        
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(RegulatoryKeyword::getKeyword, keyword);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllKeywords() {
        LambdaQueryWrapper<RegulatoryKeyword> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(RegulatoryKeyword::getKeyword);
        queryWrapper.isNotNull(RegulatoryKeyword::getKeyword);
        
        return list(queryWrapper).stream()
                .map(RegulatoryKeyword::getKeyword)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getKeywordCount() {
        return count();
    }
}