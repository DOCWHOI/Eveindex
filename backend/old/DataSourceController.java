package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.DataSource;
import com.eveindex.service.DataSourceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 数据源管理控制器
 */
@Tag(name = "数据源管理", description = "数据源信息的增删改查接口")
@RestController
@RequestMapping("/data-source")
public class DataSourceController {

    @Autowired
    private DataSourceService dataSourceService;

    /**
     * 分页查询数据源列表
     */
    @Operation(summary = "分页查询数据源列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<DataSource>> page(@RequestBody PageRequest pageRequest) {
        Page<DataSource> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<DataSource> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("source_name", pageRequest.getKeyword())
                    .or().like("source_type", pageRequest.getKeyword())
                    .or().like("data_scope", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<DataSource> result = dataSourceService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有数据源
     */
    @Operation(summary = "查询所有数据源", description = "获取所有数据源信息列表")
    @GetMapping("/list")
    public BaseResponse<List<DataSource>> list() {
        List<DataSource> list = dataSourceService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询数据源
     */
    @Operation(summary = "根据ID查询数据源", description = "根据数据源ID获取数据源详细信息")
    @GetMapping("/{id}")
    public BaseResponse<DataSource> getById(@PathVariable Integer id) {
        DataSource dataSource = dataSourceService.getById(id);
        if (dataSource == null) {
            return BaseResponse.error("数据源不存在");
        }
        return BaseResponse.success(dataSource);
    }

    /**
     * 新增数据源
     */
    @Operation(summary = "新增数据源", description = "创建新的数据源信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody DataSource dataSource) {
        boolean result = dataSourceService.save(dataSource);
        return BaseResponse.success(result);
    }

    /**
     * 更新数据源
     */
    @Operation(summary = "更新数据源", description = "根据数据源ID更新数据源信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody DataSource dataSource) {
        dataSource.setSourceId(id);
        boolean result = dataSourceService.updateById(dataSource);
        return BaseResponse.success(result);
    }

    /**
     * 删除数据源
     */
    @Operation(summary = "删除数据源", description = "根据数据源ID删除数据源")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = dataSourceService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除数据源
     */
    @Operation(summary = "批量删除数据源", description = "根据数据源ID列表批量删除数据源")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = dataSourceService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 