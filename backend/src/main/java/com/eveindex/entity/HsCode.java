package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * HS编码实体类
 */
@Data
@TableName("hs_codes")
public class HsCode {
    
    @TableId(type = IdType.AUTO)
    private Integer hsCodeId;
    
    private String hsCode;
    
    private String codeDescription;
    
    private Integer marketId;
    
    private BigDecimal importRatio;
    
    private Boolean isMedicalRelated;
    
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createdAt;
    
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updatedAt;
} 