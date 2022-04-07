package com.domain.service.impl;

import com.domain.dao.RuleItemMapper;
import com.domain.dao.RuleRealmMapper;
import com.domain.dto.RuleRealmDTO;
import com.domain.entity.RuleItem;
import com.domain.entity.RuleRealm;
import com.domain.mapstruct.RuleItemStructMapper;
import com.domain.mapstruct.RuleRealmStructMapper;
import com.domain.service.RuleService;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

/**
 * @author carl
 */
@Service
public class RuleServiceImpl implements RuleService {
    private RuleRealmMapper ruleRealmMapper;
    private RuleItemMapper ruleItemMapper;
    private RuleRealmStructMapper ruleRealmStructMapper;
    private RuleItemStructMapper ruleItemStructMapper;

    public RuleServiceImpl(RuleRealmMapper ruleRealmMapper, RuleItemMapper ruleItemMapper,
                           RuleRealmStructMapper ruleRealmStructMapper, RuleItemStructMapper ruleItemStructMapper) {
        this.ruleRealmMapper = ruleRealmMapper;
        this.ruleItemMapper = ruleItemMapper;
        this.ruleRealmStructMapper = ruleRealmStructMapper;
        this.ruleItemStructMapper = ruleItemStructMapper;
    }

    @Override
    public List<RuleRealmDTO> listAllRuleRealmDTOs() {
        List<RuleRealm> ruleRealms = ruleRealmMapper.selectList(null);
        List<RuleItem> ruleItems = ruleItemMapper.selectList(null);
        Map<Long, List<RuleItem>> map = ruleItems.stream().collect(groupingBy(RuleItem::getRuleRealmId));
        map.forEach((ruleRealmId, items) -> items.stream().sorted(Comparator.comparingInt(RuleItem::getRowNo)).collect(Collectors.toList()));
        List<RuleRealmDTO> ruleRealmDTOs = ruleRealms.stream().map(ruleRealm -> {
            RuleRealmDTO ruleRealmDTO = ruleRealmStructMapper.toRuleRealmDTO(ruleRealm);
            ruleRealmDTO.setRuleItems(ruleItemStructMapper.toRuleItemDTOs(map.get(ruleRealm.getId())));
            return ruleRealmDTO;
        }).collect(Collectors.toList());
        return ruleRealmDTOs;
    }
}
