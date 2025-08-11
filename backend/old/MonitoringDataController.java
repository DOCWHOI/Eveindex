package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.MonitoringData;
import com.eveindex.service.MonitoringDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 监控数据管理控制器
 */
@Tag(name = "监控数据管理", description = "监控数据信息的增删改查接口")
@RestController
@RequestMapping("/monitoring-data")
public class MonitoringDataController {

    @Autowired
    private MonitoringDataService monitoringDataService;

    /**
     * 分页查询监控数据列表
     */
    @Operation(summary = "分页查询监控数据列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<MonitoringData>> page(@RequestBody PageRequest pageRequest) {
        Page<MonitoringData> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<MonitoringData> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            // 可以根据需要添加搜索条件
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("monitor_date");
        }
        
        Page<MonitoringData> result = monitoringDataService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有监控数据
     */
    @Operation(summary = "查询所有监控数据", description = "获取所有监控数据信息列表")
    @GetMapping("/list")
    public BaseResponse<List<MonitoringData>> list() {
        List<MonitoringData> list = monitoringDataService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询监控数据
     */
    @Operation(summary = "根据ID查询监控数据", description = "根据监控数据ID获取数据详细信息")
    @GetMapping("/{id}")
    public BaseResponse<MonitoringData> getById(@PathVariable Integer id) {
        MonitoringData monitoringData = monitoringDataService.getById(id);
        if (monitoringData == null) {
            return BaseResponse.error("监控数据不存在");
        }
        return BaseResponse.success(monitoringData);
    }

    /**
     * 新增监控数据
     */
    @Operation(summary = "新增监控数据", description = "创建新的监控数据信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody MonitoringData monitoringData) {
        boolean result = monitoringDataService.save(monitoringData);
        return BaseResponse.success(result);
    }

    /**
     * 更新监控数据
     */
    @Operation(summary = "更新监控数据", description = "根据数据ID更新监控数据信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody MonitoringData monitoringData) {
        monitoringData.setMonitorId(id);
        boolean result = monitoringDataService.updateById(monitoringData);
        return BaseResponse.success(result);
    }

    /**
     * 删除监控数据
     */
    @Operation(summary = "删除监控数据", description = "根据数据ID删除监控数据")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = monitoringDataService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除监控数据
     */
    @Operation(summary = "批量删除监控数据", description = "根据数据ID列表批量删除数据")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = monitoringDataService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 