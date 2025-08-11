package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 风险评分实体类
 */
@Data
@TableName("risk_scores")
public class RiskScore {
    
    @TableId(type = IdType.AUTO)
    private Integer riskId;
    
    private Integer marketId;
    
    private Integer productId;
    
    private BigDecimal competitorRegScore;
    
    private BigDecimal hsCodeScore;
    
    private BigDecimal regulatoryKeywordScore;
    
    private BigDecimal violationHistoryScore;
    
    private BigDecimal totalScore;
    
    private String riskLevel;
    
    private LocalDate evaluationDate;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 