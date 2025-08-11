package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * 告警规则实体类
 */
@Data
@TableName("alert_rules")
public class AlertRule {
    
    @TableId(type = IdType.AUTO)
    private Integer ruleId;
    
    private String productType;
    
    private String marketType;
    
    private BigDecimal dauDropThreshold;
    
    private BigDecimal wauDropThreshold;
    
    private BigDecimal complaintRiseThreshold;
    
    private BigDecimal riskScoreThreshold;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 