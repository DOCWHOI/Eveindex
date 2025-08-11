package com.eveindex.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.eveindex.dto.BaseResponse;
import com.eveindex.dto.PageRequest;
import com.eveindex.entity.Product;
import com.eveindex.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 产品管理控制器
 */
@Tag(name = "产品管理", description = "产品信息的增删改查接口")
@RestController
@RequestMapping("/product")
public class ProductController {

    @Autowired
    private ProductService productService;

    /**
     * 分页查询产品列表
     */
    @Operation(summary = "分页查询产品列表", description = "支持关键词搜索和排序的分页查询")
    @PostMapping("/page")
    public BaseResponse<Page<Product>> page(@RequestBody PageRequest pageRequest) {
        Page<Product> page = new Page<>(pageRequest.getCurrent(), pageRequest.getSize());
        QueryWrapper<Product> queryWrapper = new QueryWrapper<>();
        
        if (pageRequest.getKeyword() != null && !pageRequest.getKeyword().isEmpty()) {
            queryWrapper.like("product_name", pageRequest.getKeyword())
                    .or().like("product_desc", pageRequest.getKeyword());
        }
        
        if (pageRequest.getSortField() != null && !pageRequest.getSortField().isEmpty()) {
            queryWrapper.orderBy(true, "asc".equals(pageRequest.getSortOrder()), pageRequest.getSortField());
        } else {
            queryWrapper.orderByDesc("created_at");
        }
        
        Page<Product> result = productService.page(page, queryWrapper);
        return BaseResponse.success(result);
    }

    /**
     * 查询所有产品
     */
    @Operation(summary = "查询所有产品", description = "获取所有产品信息列表")
    @GetMapping("/list")
    public BaseResponse<List<Product>> list() {
        List<Product> list = productService.list();
        return BaseResponse.success(list);
    }

    /**
     * 根据ID查询产品
     */
    @Operation(summary = "根据ID查询产品", description = "根据产品ID获取产品详细信息")
    @GetMapping("/{id}")
    public BaseResponse<Product> getById(@PathVariable Integer id) {
        Product product = productService.getById(id);
        if (product == null) {
            return BaseResponse.error("产品不存在");
        }
        return BaseResponse.success(product);
    }

    /**
     * 新增产品
     */
    @Operation(summary = "新增产品", description = "创建新的产品信息")
    @PostMapping
    public BaseResponse<Boolean> save(@RequestBody Product product) {
        boolean result = productService.save(product);
        return BaseResponse.success(result);
    }

    /**
     * 更新产品
     */
    @Operation(summary = "更新产品", description = "根据产品ID更新产品信息")
    @PutMapping("/{id}")
    public BaseResponse<Boolean> update(@PathVariable Integer id, @RequestBody Product product) {
        product.setProductId(id);
        boolean result = productService.updateById(product);
        return BaseResponse.success(result);
    }

    /**
     * 删除产品
     */
    @Operation(summary = "删除产品", description = "根据产品ID删除产品")
    @DeleteMapping("/{id}")
    public BaseResponse<Boolean> delete(@PathVariable Integer id) {
        boolean result = productService.removeById(id);
        return BaseResponse.success(result);
    }

    /**
     * 批量删除产品
     */
    @Operation(summary = "批量删除产品", description = "根据产品ID列表批量删除产品")
    @DeleteMapping("/batch")
    public BaseResponse<Boolean> batchDelete(@RequestBody List<Integer> ids) {
        boolean result = productService.removeByIds(ids);
        return BaseResponse.success(result);
    }
}