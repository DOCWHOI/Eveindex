package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.Competitor;
import com.eveindex.mapper.CompetitorMapper;
import com.eveindex.service.CompetitorService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 竞品服务实现类
 */
@Service
public class CompetitorServiceImpl extends ServiceImpl<CompetitorMapper, Competitor> implements CompetitorService {

    @Override
    public Competitor getByName(String competitorName) {
        if (!StringUtils.hasText(competitorName)) {
            return null;
        }
        
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Competitor::getCompetitorName, competitorName);
        return getOne(queryWrapper);
    }

    @Override
    public List<Competitor> getByProductType(String productType) {
        if (!StringUtils.hasText(productType)) {
            return List.of();
        }
        
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Competitor::getProductType, productType);
        queryWrapper.orderByDesc(Competitor::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<Competitor> getMedicalRegisteredCompetitors() {
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Competitor::getIsMedicalRegistered, true);
        queryWrapper.orderByDesc(Competitor::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<Competitor> getNonMedicalCompetitors() {
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Competitor::getIsMedicalRegistered, false)
                   .or()
                   .isNull(Competitor::getIsMedicalRegistered);
        queryWrapper.orderByDesc(Competitor::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public List<Competitor> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Competitor::getCompetitorName, keyword)
                   .or()
                   .like(Competitor::getProductType, keyword)
                   .or()
                   .like(Competitor::getRegistrationInfo, keyword);
        
        queryWrapper.orderByDesc(Competitor::getCreatedAt);
        return list(queryWrapper);
    }

    @Override
    public Page<Competitor> getCompetitorPage(int current, int size, String keyword) {
        Page<Competitor> page = new Page<>(current, size);
        
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Competitor::getCompetitorName, keyword)
                       .or()
                       .like(Competitor::getProductType, keyword)
                       .or()
                       .like(Competitor::getRegistrationInfo, keyword);
        }
        
        queryWrapper.orderByDesc(Competitor::getCreatedAt);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsByName(String competitorName) {
        if (!StringUtils.hasText(competitorName)) {
            return false;
        }
        
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Competitor::getCompetitorName, competitorName);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllProductTypes() {
        LambdaQueryWrapper<Competitor> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(Competitor::getProductType);
        queryWrapper.isNotNull(Competitor::getProductType);
        
        return list(queryWrapper).stream()
                .map(Competitor::getProductType)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getCompetitorCount() {
        return count();
    }
}