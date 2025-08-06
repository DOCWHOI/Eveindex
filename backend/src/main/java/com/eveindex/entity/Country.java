package com.eveindex.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("country")
public class Country {
    
    @TableId(type = IdType.AUTO)
    private Long countryId;
    
    private String name;
    
    private String code;
    
    private String regulatoryLevel;
    
    private String description;
    
    private LocalDateTime createTime;
    
    private LocalDateTime updateTime;
} 