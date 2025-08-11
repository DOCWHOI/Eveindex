package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.MonitoringData;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 监控数据服务接口
 */
public interface MonitoringDataService extends IService<MonitoringData> {
    
    /**
     * 根据市场和产品查询监控数据
     */
    List<MonitoringData> getByMarketAndProduct(Integer marketId, Integer productId);
    
    /**
     * 根据日期范围查询监控数据
     */
    List<MonitoringData> getByDateRange(LocalDate startDate, LocalDate endDate);
    
    /**
     * 获取最新的监控数据
     */
    MonitoringData getLatestData(Integer marketId, Integer productId);
    
    /**
     * 获取有投诉的监控数据
     */
    List<MonitoringData> getDataWithComplaints();
    
    /**
     * 获取有违规的监控数据
     */
    List<MonitoringData> getDataWithViolations();
    
    /**
     * 获取有召回公告的数据
     */
    List<MonitoringData> getDataWithRecalls();
    
    /**
     * 根据DAU范围查询
     */
    List<MonitoringData> getByDauRange(BigDecimal minDau, BigDecimal maxDau);
    
    /**
     * 获取监控数据分页
     */
    Page<MonitoringData> getMonitoringDataPage(int current, int size, LocalDate startDate, LocalDate endDate);
    
    /**
     * 批量导入监控数据
     */
    boolean batchImport(List<MonitoringData> dataList);
    
    /**
     * 获取监控数据统计
     */
    long getMonitoringDataCount();
}