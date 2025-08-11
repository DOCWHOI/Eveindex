package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.AlertRule;

import java.util.List;

/**
 * 告警规则服务接口
 */
public interface AlertRuleService extends IService<AlertRule> {
    
    /**
     * 根据产品类型查询告警规则
     */
    List<AlertRule> getByProductType(String productType);
    
    /**
     * 根据市场类型查询告警规则
     */
    List<AlertRule> getByMarketType(String marketType);
    
    /**
     * 根据产品类型和市场类型查询
     */
    AlertRule getByProductAndMarketType(String productType, String marketType);
    
    /**
     * 获取所有有效的告警规则
     */
    List<AlertRule> getActiveRules();
    
    /**
     * 根据关键词搜索告警规则
     */
    List<AlertRule> searchByKeyword(String keyword);
    
    /**
     * 获取告警规则分页数据
     */
    Page<AlertRule> getAlertRulePage(int current, int size, String keyword);
    
    /**
     * 检查规则是否已存在
     */
    boolean existsByProductAndMarketType(String productType, String marketType);
    
    /**
     * 获取所有产品类型
     */
    List<String> getAllProductTypes();
    
    /**
     * 获取所有市场类型
     */
    List<String> getAllMarketTypes();
    
    /**
     * 获取告警规则统计数据
     */
    long getAlertRuleCount();
}