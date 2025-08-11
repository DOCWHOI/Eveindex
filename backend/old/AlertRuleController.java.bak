package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.AlertRule;
import com.eveindex.service.AlertRuleService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 告警规则管理控制器
 */
@Tag(name = "告警规则管理", description = "告警规则信息的增删改查接口")
@RestController
@RequestMapping("/alert-rule")

public class AlertRuleController {

    @Autowired
    private AlertRuleService alertRuleService;

    /**
     * 分页查询告警规则列表
     */
    @Operation(summary = "分页查询告警规则列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<AlertRule>> page(@RequestBody PageRequest pageRequest) {
        Page<AlertRule> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<AlertRule> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("product_type", pageRequest.getKeyword())
                    .or().like("market_type", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<AlertRule> result = alertRuleService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有告警规则
     */
    @Operation(summary = "查询所有告警规则", description = "获取所有告警规则信息列表")
    @GetMapping("/list")
    public BaseResponse<List<AlertRule>> list() {
        List<AlertRule> list = alertRuleService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询告警规则
     */
    @Operation(summary = "根据ID查询告警规则", description = "根据规则ID获取规则详细信息")
    @GetMapping("/{id}")
    public BaseResponse<AlertRule> getById(@PathVariable Integer id) {
        AlertRule alertRule = alertRuleService.getById(id);
        if (alertRule == null) {
            return BaseResponse.error("告警规则不存在");
        }
        return BaseResponse.success(alertRule);
    }

    /**
     * 新增告警规则
     */
    @Operation(summary = "新增告警规则", description = "创建新的告警规则信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody AlertRule alertRule) {
        boolean result = alertRuleService.save(alertRule);
        return BaseResponse.success(result);
    }

    /**
     * 更新告警规则
     */
    @Operation(summary = "更新告警规则", description = "根据规则ID更新告警规则信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody AlertRule alertRule) {
        alertRule.setRuleId(id);
        boolean result = alertRuleService.updateById(alertRule);
        return BaseResponse.success(result);
    }

    /**
     * 删除告警规则
     */
    @Operation(summary = "删除告警规则", description = "根据规则ID删除告警规则")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = alertRuleService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除告警规则
     */
    @Operation(summary = "批量删除告警规则", description = "根据规则ID列表批量删除规则")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = alertRuleService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 