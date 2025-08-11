package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.time.LocalDateTime;

/**
 * 竞品实体类
 */
@Data
@TableName("competitors")
public class Competitor {
    
    @TableId(type = IdType.AUTO)
    private Integer competitorId;
    
    private String competitorName;
    
    private String productType;
    
    private Boolean isMedicalRegistered;
    
    private String registrationInfo;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 