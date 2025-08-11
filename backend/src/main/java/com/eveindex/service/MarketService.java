package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.Market;

import java.util.List;

/**
 * 市场服务接口
 */
public interface MarketService extends IService<Market> {
    
    /**
     * 根据国家查询市场
     */
    Market getByCountry(String country);
    
    /**
     * 根据区域查询市场列表
     */
    List<Market> getByRegion(String region);
    
    /**
     * 根据监管机构查询市场
     */
    List<Market> getByRegulatoryBody(String regulatoryBody);
    
    /**
     * 根据监管严格程度查询市场
     */
    List<Market> getByRegulatorySeverity(Integer minSeverity, Integer maxSeverity);
    
    /**
     * 获取高风险市场列表（监管严格程度>=8）
     */
    List<Market> getHighRiskMarkets();
    
    /**
     * 根据关键词搜索市场
     */
    List<Market> searchByKeyword(String keyword);
    
    /**
     * 获取市场分页数据
     */
    Page<Market> getMarketPage(int current, int size, String keyword);
    
    /**
     * 检查国家是否已存在
     */
    boolean existsByCountry(String country);
    
    /**
     * 获取所有区域列表
     */
    List<String> getAllRegions();
    
    /**
     * 获取市场统计数据
     */
    long getMarketCount();
} 