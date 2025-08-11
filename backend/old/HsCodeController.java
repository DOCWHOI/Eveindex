package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.HsCode;
import com.eveindex.service.HsCodeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * HS编码管理控制器
 */
@Tag(name = "HS编码管理", description = "HS编码信息的增删改查接口")
@RestController
@RequestMapping("/hscode")
public class HsCodeController {

    @Autowired
    private HsCodeService hsCodeService;

    /**
     * 分页查询HS编码列表
     */
    @Operation(summary = "分页查询HS编码列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<HsCode>> page(@RequestBody PageRequest pageRequest) {
        Page<HsCode> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<HsCode> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("hs_code", pageRequest.getKeyword())
                    .or().like("code_description", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<HsCode> result = hsCodeService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有HS编码
     */
    @Operation(summary = "查询所有HS编码", description = "获取所有HS编码信息列表")
    @GetMapping("/list")
    public BaseResponse<List<HsCode>> list() {
        List<HsCode> list = hsCodeService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询HS编码
     */
    @Operation(summary = "根据ID查询HS编码", description = "根据HS编码ID获取HS编码详细信息")
    @GetMapping("/{id}")
    public BaseResponse<HsCode> getById(@PathVariable Integer id) {
        HsCode hsCode = hsCodeService.getById(id);
        if (hsCode == null) {
            return BaseResponse.error("HS编码不存在");
        }
        return BaseResponse.success(hsCode);
    }

    /**
     * 新增HS编码
     */
    @Operation(summary = "新增HS编码", description = "创建新的HS编码信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody HsCode hsCode) {
        boolean result = hsCodeService.save(hsCode);
        return BaseResponse.success(result);
    }

    /**
     * 更新HS编码
     */
    @Operation(summary = "更新HS编码", description = "根据HS编码ID更新HS编码信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody HsCode hsCode) {
        hsCode.setHsCodeId(id);
        boolean result = hsCodeService.updateById(hsCode);
        return BaseResponse.success(result);
    }

    /**
     * 删除HS编码
     */
    @Operation(summary = "删除HS编码", description = "根据HS编码ID删除HS编码")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = hsCodeService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除HS编码
     */
    @Operation(summary = "批量删除HS编码", description = "根据HS编码ID列表批量删除HS编码")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = hsCodeService.removeByIds(ids);
        return BaseResponse.success(result);
    }
} 