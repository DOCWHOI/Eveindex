package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 监控数据实体类
 */
@Data
@TableName("monitoring_data")
public class MonitoringData {
    
    @TableId(type = IdType.AUTO)
    private Integer monitorId;
    
    private Integer marketId;
    
    private Integer productId;
    
    private LocalDate monitorDate;
    
    private BigDecimal dau;
    
    private BigDecimal wau;
    
    private Integer complaintCount;
    
    private Integer importViolationCount;
    
    private Boolean hsCodeChange;
    
    private Boolean recallAnnouncement;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 