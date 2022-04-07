package com.domain.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.domain.entity.RuleItem;

import java.util.List;

/**
 * @author carl
 */
public interface RuleItemMapper extends BaseMapper<RuleItem> {
    List<RuleItem> findAllByRuleRealmId(Long ruleRealmId);
}
