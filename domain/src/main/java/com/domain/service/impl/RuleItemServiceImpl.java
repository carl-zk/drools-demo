package com.domain.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.domain.dao.RuleItemMapper;
import com.domain.entity.RuleItem;
import com.domain.service.RuleItemService;
import org.springframework.stereotype.Service;

/**
 * @author carl
 */
@Service
public class RuleItemServiceImpl extends ServiceImpl<RuleItemMapper, RuleItem> implements RuleItemService {
}
