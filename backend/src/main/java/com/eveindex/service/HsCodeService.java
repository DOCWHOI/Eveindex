package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.HsCode;

import java.math.BigDecimal;
import java.util.List;

/**
 * HS编码服务接口
 */
public interface HsCodeService extends IService<HsCode> {
    
    /**
     * 根据HS编码查询
     */
    HsCode getByHsCode(String hsCode);
    
    /**
     * 根据市场ID查询HS编码列表
     */
    List<HsCode> getByMarketId(Integer marketId);
    
    /**
     * 获取医疗器械相关HS编码
     */
    List<HsCode> getMedicalRelatedHsCodes();
    
    /**
     * 根据进口比例范围查询
     */
    List<HsCode> getByImportRatioRange(BigDecimal minRatio, BigDecimal maxRatio);
    
    /**
     * 根据关键词搜索HS编码
     */
    List<HsCode> searchByKeyword(String keyword);
    
    /**
     * 获取HS编码分页数据
     */
    Page<HsCode> getHsCodePage(int current, int size, String keyword);
    
    /**
     * 检查HS编码是否已存在
     */
    boolean existsByHsCode(String hsCode);
    
    /**
     * 获取所有独特的HS编码
     */
    List<String> getAllUniqueCodes();
    
    /**
     * 获取HS编码统计数据
     */
    long getHsCodeCount();
}