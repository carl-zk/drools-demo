package com.domain.mapstruct;

import com.domain.dto.RuleItemDTO;
import com.domain.entity.RuleItem;
import org.mapstruct.Mapper;

import java.util.List;

/**
 * @author carl
 */
@Mapper
public interface RuleItemStructMapper {
    RuleItem toRuleItem(RuleItemDTO ruleItemDTO);

    RuleItemDTO toRuleItemDTO(RuleItem ruleItem);

    List<RuleItemDTO> toRuleItemDTOs(List<RuleItem> ruleItems);
}
