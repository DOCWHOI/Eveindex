package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.Market;
import com.eveindex.service.MarketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 市场管理控制器
 */
@Tag(name = "市场管理", description = "市场信息的增删改查接口")
@RestController
@RequestMapping("/market")
public class MarketController {

    @Autowired
    private MarketService marketService;

    /**
     * 分页查询市场列表
     */
    @Operation(summary = "分页查询市场列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<Market>> page(@RequestBody PageRequest pageRequest) {
        Page<Market> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<Market> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("country", pageRequest.getKeyword())
                    .or().like("region", pageRequest.getKeyword())
                    .or().like("regulatory_body", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<Market> result = marketService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有市场
     */
    @Operation(summary = "查询所有市场", description = "获取所有市场信息列表")
    @GetMapping("/list")
    public BaseResponse<List<Market>> list() {
        List<Market> list = marketService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询市场
     */
    @Operation(summary = "根据ID查询市场", description = "根据市场ID获取市场详细信息")
    @GetMapping("/{id}")
    public BaseResponse<Market> getById(@PathVariable Integer id) {
        Market market = marketService.getById(id);
        if (market == null) {
            return BaseResponse.error("市场不存在");
        }
        return BaseResponse.success(market);
    }

    /**
     * 新增市场
     */
    @Operation(summary = "新增市场", description = "创建新的市场信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody Market market) {
        boolean result = marketService.save(market);
        return BaseResponse.success(result);
    }

    /**
     * 更新市场
     */
    @Operation(summary = "更新市场", description = "根据市场ID更新市场信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody Market market) {
        market.setMarketId(id);
        boolean result = marketService.updateById(market);
        return BaseResponse.success(result);
    }

    /**
     * 删除市场
     */
    @Operation(summary = "删除市场", description = "根据市场ID删除市场")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = marketService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除市场
     */
    @Operation(summary = "批量删除市场", description = "根据市场ID列表批量删除市场")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = marketService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 