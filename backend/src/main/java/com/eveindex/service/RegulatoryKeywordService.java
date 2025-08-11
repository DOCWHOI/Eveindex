package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.RegulatoryKeyword;

import java.util.List;

/**
 * 法规关键词服务接口
 */
public interface RegulatoryKeywordService extends IService<RegulatoryKeyword> {
    
    /**
     * 根据关键词查询
     */
    RegulatoryKeyword getByKeyword(String keyword);
    
    /**
     * 根据风险等级查询关键词
     */
    List<RegulatoryKeyword> getByRiskLevel(Integer riskLevel);
    
    /**
     * 获取高风险关键词（风险等级>=8）
     */
    List<RegulatoryKeyword> getHighRiskKeywords();
    
    /**
     * 根据风险等级范围查询
     */
    List<RegulatoryKeyword> getByRiskLevelRange(Integer minLevel, Integer maxLevel);
    
    /**
     * 根据关键词搜索
     */
    List<RegulatoryKeyword> searchByKeyword(String searchKeyword);
    
    /**
     * 获取关键词分页数据
     */
    Page<RegulatoryKeyword> getKeywordPage(int current, int size, String keyword);
    
    /**
     * 检查关键词是否已存在
     */
    boolean existsByKeyword(String keyword);
    
    /**
     * 获取所有关键词
     */
    List<String> getAllKeywords();
    
    /**
     * 获取关键词统计数据
     */
    long getKeywordCount();
}