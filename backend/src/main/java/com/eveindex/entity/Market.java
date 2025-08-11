package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 市场实体类
 */
@Data
@TableName("markets")
public class Market {
    
    @TableId(type = IdType.AUTO)
    private Integer marketId;
    
    private String country;
    
    private String region;
    
    private String regulatoryBody;
    
    private Integer regulatorySeverity;
    
    private String queryUrl;
    
    private String updateFrequency;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 