package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 数据源实体类
 */
@Data
@TableName("data_sources")
public class DataSource {
    
    @TableId(type = IdType.AUTO)
    private Integer sourceId;
    
    private String sourceName;
    
    private String sourceType;
    
    private String url;
    
    private String dataScope;
    
    private String updateCycle;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 