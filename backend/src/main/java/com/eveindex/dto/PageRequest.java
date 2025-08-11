package com.eveindex.dto;

import lombok.Data;

/**
 * 分页请求DTO
 */
@Data
public class PageRequest {
    
    private Integer current = 1;
    
    private Integer size = 10;
    
    private String keyword;
    
    private String sortField;
    
    private String sortOrder = "desc";
} 