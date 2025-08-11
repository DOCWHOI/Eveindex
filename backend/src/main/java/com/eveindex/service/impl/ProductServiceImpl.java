package com.eveindex.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.eveindex.common.exception.BusinessException;
import com.eveindex.entity.Product;
import com.eveindex.mapper.ProductMapper;
import com.eveindex.service.ProductService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 产品服务实现类
 */
@Service
public class ProductServiceImpl extends ServiceImpl<ProductMapper, Product> implements ProductService {

    @Override
    public Product getByName(String productName) {
        if (!StringUtils.hasText(productName)) {
            return null;
        }
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductName, productName);
        return getOne(queryWrapper);
    }

    @Override
    public List<Product> searchByKeyword(String keyword) {
        if (!StringUtils.hasText(keyword)) {
            return list();
        }
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Product::getProductName, keyword)
                   .or()
                   .like(Product::getProductDesc, keyword)
                   .or()
                   .like(Product::getCoreFunctions, keyword);
        
        return list(queryWrapper);
    }

    @Override
    public Page<Product> getProductPage(int current, int size, String keyword) {
        Page<Product> page = new Page<>(current, size);
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        if (StringUtils.hasText(keyword)) {
            queryWrapper.like(Product::getProductName, keyword)
                       .or()
                       .like(Product::getProductDesc, keyword)
                       .or()
                       .like(Product::getCoreFunctions, keyword);
        }
        
        queryWrapper.orderByDesc(Product::getCreatedAt);
        return page(page, queryWrapper);
    }

    @Override
    public List<Product> getByHsCode(String hsCode) {
        if (!StringUtils.hasText(hsCode)) {
            return List.of();
        }
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.like(Product::getHsCodeReferences, hsCode);
        return list(queryWrapper);
    }

    @Override
    public boolean existsByName(String productName) {
        if (!StringUtils.hasText(productName)) {
            return false;
        }
        
        LambdaQueryWrapper<Product> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Product::getProductName, productName);
        return count(queryWrapper) > 0;
    }

    @Override
    public boolean batchImport(List<Product> products) {
        if (products == null || products.isEmpty()) {
            throw new BusinessException("导入数据不能为空");
        }
        
        // 检查重复名称
        for (Product product : products) {
            if (existsByName(product.getProductName())) {
                throw new BusinessException("产品名称已存在: " + product.getProductName());
            }
        }
        
        return saveBatch(products);
    }

    @Override
    public long getProductCount() {
        return count();
    }
} 