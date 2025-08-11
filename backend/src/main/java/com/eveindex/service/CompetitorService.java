package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.Competitor;

import java.util.List;

/**
 * 竞品服务接口
 */
public interface CompetitorService extends IService<Competitor> {
    
    /**
     * 根据竞品名称查询竞品
     */
    Competitor getByName(String competitorName);
    
    /**
     * 根据产品类型查询竞品
     */
    List<Competitor> getByProductType(String productType);
    
    /**
     * 获取已注册为医疗器械的竞品
     */
    List<Competitor> getMedicalRegisteredCompetitors();
    
    /**
     * 获取未注册为医疗器械的竞品
     */
    List<Competitor> getNonMedicalCompetitors();
    
    /**
     * 根据关键词搜索竞品
     */
    List<Competitor> searchByKeyword(String keyword);
    
    /**
     * 获取竞品分页数据
     */
    Page<Competitor> getCompetitorPage(int current, int size, String keyword);
    
    /**
     * 检查竞品名称是否已存在
     */
    boolean existsByName(String competitorName);
    
    /**
     * 获取所有产品类型
     */
    List<String> getAllProductTypes();
    
    /**
     * 获取竞品统计数据
     */
    long getCompetitorCount();
} 