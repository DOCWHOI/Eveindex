package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.Competitor;
import com.eveindex.service.CompetitorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 竞品管理控制器
 */
@Tag(name = "竞品管理", description = "竞品信息的增删改查接口")
@RestController
@RequestMapping("/competitor")
public class CompetitorController {

    @Autowired
    private CompetitorService competitorService;

    /**
     * 分页查询竞品列表
     */
    @Operation(summary = "分页查询竞品列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<Competitor>> page(@RequestBody PageRequest pageRequest) {
        Page<Competitor> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<Competitor> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("competitor_name", pageRequest.getKeyword())
                    .or().like("product_type", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<Competitor> result = competitorService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有竞品
     */
    @Operation(summary = "查询所有竞品", description = "获取所有竞品信息列表")
    @GetMapping("/list")
    public BaseResponse<List<Competitor>> list() {
        List<Competitor> list = competitorService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询竞品
     */
    @Operation(summary = "根据ID查询竞品", description = "根据竞品ID获取竞品详细信息")
    @GetMapping("/{id}")
    public BaseResponse<Competitor> getById(@PathVariable Integer id) {
        Competitor competitor = competitorService.getById(id);
        if (competitor == null) {
            return BaseResponse.error("竞品不存在");
        }
        return BaseResponse.success(competitor);
    }

    /**
     * 新增竞品
     */
    @Operation(summary = "新增竞品", description = "创建新的竞品信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody Competitor competitor) {
        boolean result = competitorService.save(competitor);
        return BaseResponse.success(result);
    }

    /**
     * 更新竞品
     */
    @Operation(summary = "更新竞品", description = "根据竞品ID更新竞品信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody Competitor competitor) {
        competitor.setCompetitorId(id);
        boolean result = competitorService.updateById(competitor);
        return BaseResponse.success(result);
    }

    /**
     * 删除竞品
     */
    @Operation(summary = "删除竞品", description = "根据竞品ID删除竞品")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = competitorService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除竞品
     */
    @Operation(summary = "批量删除竞品", description = "根据竞品ID列表批量删除竞品")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = competitorService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 