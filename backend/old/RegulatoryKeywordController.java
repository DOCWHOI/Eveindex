package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.RegulatoryKeyword;
import com.eveindex.service.RegulatoryKeywordService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 法规关键词管理控制器
 */
@Tag(name = "法规关键词管理", description = "法规关键词信息的增删改查接口")
@RestController
@RequestMapping("/regulatory-keyword")
public class RegulatoryKeywordController {

    @Autowired
    private RegulatoryKeywordService regulatoryKeywordService;

    /**
     * 分页查询法规关键词列表
     */
    @Operation(summary = "分页查询法规关键词列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<RegulatoryKeyword>> page(@RequestBody PageRequest pageRequest) {
        Page<RegulatoryKeyword> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<RegulatoryKeyword> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("keyword", pageRequest.getKeyword())
                    .or().like("description", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<RegulatoryKeyword> result = regulatoryKeywordService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有法规关键词
     */
    @Operation(summary = "查询所有法规关键词", description = "获取所有法规关键词信息列表")
    @GetMapping("/list")
    public BaseResponse<List<RegulatoryKeyword>> list() {
        List<RegulatoryKeyword> list = regulatoryKeywordService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询法规关键词
     */
    @Operation(summary = "根据ID查询法规关键词", description = "根据关键词ID获取关键词详细信息")
    @GetMapping("/{id}")
    public BaseResponse<RegulatoryKeyword> getById(@PathVariable Integer id) {
        RegulatoryKeyword keyword = regulatoryKeywordService.getById(id);
        if (keyword == null) {
            return BaseResponse.error("法规关键词不存在");
        }
        return BaseResponse.success(keyword);
    }

    /**
     * 新增法规关键词
     */
    @Operation(summary = "新增法规关键词", description = "创建新的法规关键词信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody RegulatoryKeyword keyword) {
        boolean result = regulatoryKeywordService.save(keyword);
        return BaseResponse.success(result);
    }

    /**
     * 更新法规关键词
     */
    @Operation(summary = "更新法规关键词", description = "根据关键词ID更新关键词信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody RegulatoryKeyword keyword) {
        keyword.setKeywordId(id);
        boolean result = regulatoryKeywordService.updateById(keyword);
        return BaseResponse.success(result);
    }

    /**
     * 删除法规关键词
     */
    @Operation(summary = "删除法规关键词", description = "根据关键词ID删除关键词")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = regulatoryKeywordService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除法规关键词
     */
    @Operation(summary = "批量删除法规关键词", description = "根据关键词ID列表批量删除关键词")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = regulatoryKeywordService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 