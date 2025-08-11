package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 产品实体类
 */
@Data
@TableName("products")
public class Product {
    
    @TableId(type = IdType.AUTO)
    private Integer productId;
    
    private String productName;
    
    private String productDesc;
    
    private String coreFunctions;
    
    private String hsCodeReferences;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 