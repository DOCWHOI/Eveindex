package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.entity.DataSource;
import com.eveindex.mapper.DataSourceMapper;
import com.eveindex.service.DataSourceService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 数据源服务实现类
 */
@Service
public class DataSourceServiceImpl extends ServiceImpl<DataSourceMapper, DataSource> implements DataSourceService {

    @Override
    public DataSource getBySourceName(String sourceName) {
        if (!StringUtils.hasText(sourceName)) {
            return null;
        }
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getSourceName, sourceName);
        return getOne(queryWrapper);
    }

    @Override
    public List<DataSource> getBySourceType(String sourceType) {
        if (!StringUtils.hasText(sourceType)) {
            return List.of();
        }
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getSourceType, sourceType);
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return list(queryWrapper);
    }

    @Override
    public List<DataSource> getApiDataSources() {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getSourceType, "API");
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return list(queryWrapper);
    }

    @Override
    public List<DataSource> getCrawlerDataSources() {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getSourceType, "爬虫");
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return list(queryWrapper);
    }

    @Override
    public List<DataSource> getByUpdateCycle(String updateCycle) {
        if (!StringUtils.hasText(updateCycle)) {
            return List.of();
        }
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getUpdateCycle, updateCycle);
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return list(queryWrapper);
    }

    @Override
    public List<DataSource> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(DataSource::getSourceName, keyword)
                   .or()
                   .like(DataSource::getSourceType, keyword)
                   .or()
                   .like(DataSource::getDataScope, keyword);
        
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return list(queryWrapper);
    }

    @Override
    public Page<DataSource> getDataSourcePage(int current, int size, String keyword) {
        Page<DataSource> page = new Page<>(current, size);
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(DataSource::getSourceName, keyword)
                       .or()
                       .like(DataSource::getSourceType, keyword)
                       .or()
                       .like(DataSource::getDataScope, keyword);
        }
        
        queryWrapper.orderByAsc(DataSource::getSourceName);
        return page(page, queryWrapper);
    }

    @Override
    public boolean existsBySourceName(String sourceName) {
        if (!StringUtils.hasText(sourceName)) {
            return false;
        }
        
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(DataSource::getSourceName, sourceName);
        return count(queryWrapper) > 0;
    }

    @Override
    public List<String> getAllSourceTypes() {
        LambdaQueryWrapper<DataSource> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.select(DataSource::getSourceType);
        queryWrapper.isNotNull(DataSource::getSourceType);
        
        return list(queryWrapper).stream()
                .map(DataSource::getSourceType)
                .filter(StringUtils::hasText)
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    public long getDataSourceCount() {
        return count();
    }
}