package com.eveindex.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.eveindex.entity.Product;

import java.util.List;

/**
 * 产品服务接口
 */
public interface ProductService extends IService<Product> {
    
    /**
     * 根据产品名称查询产品
     */
    Product getByName(String productName);
    
    /**
     * 根据关键词搜索产品
     */
    List<Product> searchByKeyword(String keyword);
    
    /**
     * 获取产品分页数据
     */
    Page<Product> getProductPage(int current, int size, String keyword);
    
    /**
     * 根据HS编码查询相关产品
     */
    List<Product> getByHsCode(String hsCode);
    
    /**
     * 检查产品名称是否已存在
     */
    boolean existsByName(String productName);
    
    /**
     * 批量导入产品
     */
    boolean batchImport(List<Product> products);
    
    /**
     * 获取产品统计数据
     */
    long getProductCount();
}