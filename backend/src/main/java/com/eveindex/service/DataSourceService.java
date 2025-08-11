package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.DataSource;

import java.util.List;

/**
 * 数据源服务接口
 */
public interface DataSourceService extends IService<DataSource> {
    
    /**
     * 根据数据源名称查询
     */
    DataSource getBySourceName(String sourceName);
    
    /**
     * 根据数据源类型查询
     */
    List<DataSource> getBySourceType(String sourceType);
    
    /**
     * 获取API类型的数据源
     */
    List<DataSource> getApiDataSources();
    
    /**
     * 获取爬虫类型的数据源
     */
    List<DataSource> getCrawlerDataSources();
    
    /**
     * 根据更新周期查询
     */
    List<DataSource> getByUpdateCycle(String updateCycle);
    
    /**
     * 根据关键词搜索数据源
     */
    List<DataSource> searchByKeyword(String keyword);
    
    /**
     * 获取数据源分页数据
     */
    Page<DataSource> getDataSourcePage(int current, int size, String keyword);
    
    /**
     * 检查数据源名称是否已存在
     */
    boolean existsBySourceName(String sourceName);
    
    /**
     * 获取所有数据源类型
     */
    List<String> getAllSourceTypes();
    
    /**
     * 获取数据源统计数据
     */
    long getDataSourceCount();
} 