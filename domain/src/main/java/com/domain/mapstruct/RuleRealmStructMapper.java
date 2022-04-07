package com.domain.mapstruct;

import com.domain.dto.RuleRealmDTO;
import com.domain.entity.RuleItem;
import com.domain.entity.RuleRealm;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author carl
 */
@Mapper(uses = RuleItemStructMapper.class)
public interface RuleRealmStructMapper {
    RuleRealm toRuleRealm(RuleRealmDTO ruleRealmDTO);

    RuleRealmDTO toRuleRealmDTO(RuleRealm ruleRealm);

    RuleRealmDTO toRuleRealmDTO(RuleRealm ruleRealm, List<RuleItem> ruleItems);

    List<RuleRealmDTO> toRuleRealmDTOs(List<RuleRealm> ruleRealms);
}
