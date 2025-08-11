package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.RiskScore;
import com.eveindex.service.RiskScoreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 风险评分管理控制器
 */
@Tag(name = "风险评分管理", description = "风险评分信息的增删改查接口")
@RestController
@RequestMapping("/risk-score")
public class RiskScoreController {

    @Autowired
    private RiskScoreService riskScoreService;

    /**
     * 分页查询风险评分列表
     */
    @Operation(summary = "分页查询风险评分列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<RiskScore>> page(@RequestBody PageRequest pageRequest) {
        Page<RiskScore> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<RiskScore> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("risk_level", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("evaluation_date");
        }
        
        Page<RiskScore> result = riskScoreService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有风险评分
     */
    @Operation(summary = "查询所有风险评分", description = "获取所有风险评分信息列表")
    @GetMapping("/list")
    public BaseResponse<List<RiskScore>> list() {
        List<RiskScore> list = riskScoreService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询风险评分
     */
    @Operation(summary = "根据ID查询风险评分", description = "根据风险评分ID获取评分详细信息")
    @GetMapping("/{id}")
    public BaseResponse<RiskScore> getById(@PathVariable Integer id) {
        RiskScore riskScore = riskScoreService.getById(id);
        if (riskScore == null) {
            return BaseResponse.error("风险评分不存在");
        }
        return BaseResponse.success(riskScore);
    }

    /**
     * 新增风险评分
     */
    @Operation(summary = "新增风险评分", description = "创建新的风险评分信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody RiskScore riskScore) {
        boolean result = riskScoreService.save(riskScore);
        return BaseResponse.success(result);
    }

    /**
     * 更新风险评分
     */
    @Operation(summary = "更新风险评分", description = "根据评分ID更新风险评分信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody RiskScore riskScore) {
        riskScore.setRiskId(id);
        boolean result = riskScoreService.updateById(riskScore);
        return BaseResponse.success(result);
    }

    /**
     * 删除风险评分
     */
    @Operation(summary = "删除风险评分", description = "根据评分ID删除风险评分")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = riskScoreService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除风险评分
     */
    @Operation(summary = "批量删除风险评分", description = "根据评分ID列表批量删除评分")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = riskScoreService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 