package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 法规关键词实体类
 */
@Data
@TableName("regulatory_keywords")
public class RegulatoryKeyword {
    
    @TableId(type = IdType.AUTO)
    private Integer keywordId;
    
    private String keyword;
    
    private Integer riskLevel;
    
    private String description;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 