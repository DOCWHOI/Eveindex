package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.HsCode;
import com.eveindex.mapper.HsCodeMapper;
import com.eveindex.service.HsCodeService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

/**
 * HS编码服务实现类
 */
@Service
public class HsCodeServiceImpl extends ServiceImpl<HsCodeMapper, HsCode> implements HsCodeService {

    @Override
    public HsCode getByHsCode(String hsCode) {
        if (!StringUtils.hasText(hsCode)) {
            return null;
        }
        
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HsCode::getHsCode, hsCode);
        return getOne(queryWrapper);
    }

    @Override
    public List<HsCode> getByMarketId(Integer marketId) {
        if (marketId == null) {
            return List.of();
        }
        
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HsCode::getMarketId, marketId);
        queryWrapper.orderByAsc(HsCode::getHsCode);
        return list(queryWrapper);
    }

    @Override
    public List<HsCode> getMedicalRelatedHsCodes() {
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HsCode::getIsMedicalRelated, true);
        queryWrapper.orderByAsc(HsCode::getHsCode);
        return list(queryWrapper);
    }

    @Override
    public List<HsCode> getByImportRatioRange(BigDecimal minRatio, BigDecimal maxRatio) {
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        
        if (minRatio != null) {
            queryWrapper.ge(HsCode::getImportRatio, minRatio);
        }
        if (maxRatio != null) {
            queryWrapper.le(HsCode::getImportRatio, maxRatio);
        }
        
        queryWrapper.orderByDesc(HsCode::getImportRatio);
        return list(queryWrapper);
    }

    @Override
    public List<HsCode> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(HsCode::getHsCode, keyword)
                   .or()
                   .like(HsCode::getCodeDescription, keyword);
        
        queryWrapper.orderByAsc(HsCode::getHsCode);
        return list(queryWrapper);
    }

    @Override
    public Page<HsCode> getHsCodePage(int current, int size, String keyword) {
        Page<HsCode> page = new Page<>(current, size);
        
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(HsCode::getHsCode, keyword)
                       .or()
                       .like(HsCode::getCodeDescription, keyword);
        }
        
        queryWrapper.orderByAsc(HsCode::getHsCode);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsByHsCode(String hsCode) {
        if (!StringUtils.hasText(hsCode)) {
            return false;
        }
        
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(HsCode::getHsCode, hsCode);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllUniqueCodes() {
        LambdaQueryWrapper<HsCode> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(HsCode::getHsCode);
        queryWrapper.isNotNull(HsCode::getHsCode);
        
        return list(queryWrapper).stream()
                .map(HsCode::getHsCode)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getHsCodeCount() {
        return count();
    }
}