package com.domain.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author carl
 */
@Getter
@Setter
@ToString
public class RuleRealmDTO {
    private String id;
    private String type;
    private String name;
    private String group;
    private String header;
    private List<RuleItemDTO> ruleItems;
}
