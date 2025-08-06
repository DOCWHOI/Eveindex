package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("early_warning")
public class EarlyWarning {
    
    @TableId(type = IdType.AUTO)
    private Long warningId;
    
    private Long countryId;
    
    private String warningType;
    
    private String triggerValue;
    
    private String threshold;
    
    private String status;
    
    private String description;
    
    private String emailLog;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 