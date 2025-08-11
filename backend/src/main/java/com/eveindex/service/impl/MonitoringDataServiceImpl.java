package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.common.exception.BusinessException;
import com.eveindex.entity.MonitoringData;
import com.eveindex.mapper.MonitoringDataMapper;
import com.eveindex.service.MonitoringDataService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * 监控数据服务实现类
 */
@Service
public class MonitoringDataServiceImpl extends ServiceImpl<MonitoringDataMapper, MonitoringData> implements MonitoringDataService {

    @Override
    public List<MonitoringData> getByMarketAndProduct(Integer marketId, Integer productId) {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        
        if (marketId != null) {
            queryWrapper.eq(MonitoringData::getMarketId, marketId);
        }
        if (productId != null) {
            queryWrapper.eq(MonitoringData::getProductId, productId);
        }
        
        queryWrapper.orderByDesc(MonitoringData::getMonitorDate);
        return list(queryWrapper);
    }

    @Override
    public List<MonitoringData> getByDateRange(LocalDate startDate, LocalDate endDate) {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        
        if (startDate != null) {
            queryWrapper.ge(MonitoringData::getMonitorDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(MonitoringData::getMonitorDate, endDate);
        }
        
        queryWrapper.orderByDesc(MonitoringData::getMonitorDate);
        return list(queryWrapper);
    }

    @Override
    public MonitoringData getLatestData(Integer marketId, Integer productId) {
        if (marketId == null || productId == null) {
            return null;
        }
        
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitoringData::getMarketId, marketId)
                   .eq(MonitoringData::getProductId, productId)
                   .orderByDesc(MonitoringData::getMonitorDate)
                   .last("LIMIT 1");
        
        return getOne(queryWrapper);
    }

    @Override
    public List<MonitoringData> getDataWithComplaints() {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(MonitoringData::getComplaintCount, 0);
        queryWrapper.orderByDesc(MonitoringData::getComplaintCount);
        return list(queryWrapper);
    }

    @Override
    public List<MonitoringData> getDataWithViolations() {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.gt(MonitoringData::getImportViolationCount, 0);
        queryWrapper.orderByDesc(MonitoringData::getImportViolationCount);
        return list(queryWrapper);
    }

    @Override
    public List<MonitoringData> getDataWithRecalls() {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(MonitoringData::getRecallAnnouncement, true);
        queryWrapper.orderByDesc(MonitoringData::getMonitorDate);
        return list(queryWrapper);
    }

    @Override
    public List<MonitoringData> getByDauRange(BigDecimal minDau, BigDecimal maxDau) {
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        
        if (minDau != null) {
            queryWrapper.ge(MonitoringData::getDau, minDau);
        }
        if (maxDau != null) {
            queryWrapper.le(MonitoringData::getDau, maxDau);
        }
        
        queryWrapper.orderByDesc(MonitoringData::getDau);
        return list(queryWrapper);
    }

    @Override
    public Page<MonitoringData> getMonitoringDataPage(int current, int size, LocalDate startDate, LocalDate endDate) {
        Page<MonitoringData> page = new Page<>(current, size);
        
        LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
        if (startDate != null) {
            queryWrapper.ge(MonitoringData::getMonitorDate, startDate);
        }
        if (endDate != null) {
            queryWrapper.le(MonitoringData::getMonitorDate, endDate);
        }
        
        queryWrapper.orderByDesc(MonitoringData::getMonitorDate);
        return page(page, queryWrapper);
    }

    @Override
    public boolean batchImport(List<MonitoringData> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            throw new BusinessException("导入数据不能为空");
        }
        
        // 检查重复数据（同一市场、产品、日期的数据只能有一条）
        for (MonitoringData data : dataList) {
            LambdaQueryWrapper<MonitoringData> queryWrapper = new LambdaQueryWrapper<>();
            queryWrapper.eq(MonitoringData::getMarketId, data.getMarketId())
                       .eq(MonitoringData::getProductId, data.getProductId())
                       .eq(MonitoringData::getMonitorDate, data.getMonitorDate());
            
            if (count(queryWrapper) > 0) {
                throw new BusinessException("监控数据已存在: " + 
                    data.getMarketId() + "-" + data.getProductId() + "-" + data.getMonitorDate());
            }
        }
        
        return saveBatch(dataList);
    }

    @Override
    public long getMonitoringDataCount() {
        return count();
    }
}