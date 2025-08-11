package com.eveindex.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.eveindex.entity.AlertRule;
import org.apache.ibatis.annotations.Mapper;

/**
 * 告警规则Mapper接口
 */
@Mapper
public interface AlertRuleMapper extends BaseMapper<AlertRule> {
} 