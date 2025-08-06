package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("pre_market_risk")
public class PreMarketRisk {
    
    @TableId(type = IdType.AUTO)
    private Long riskId;
    
    private Long countryId;
    
    private Long compId;
    
    private Integer compScore;
    
    private Integer hsCodeScore;
    
    private Integer regulatoryScore;
    
    private Integer violationScore;
    
    private Integer totalScore;
    
    private String riskLevel;
    
    private String decision;
    
    private String description;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 