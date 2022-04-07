package com.domain.entity;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 * @author carl
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
@TableName("RULE_ITEM")
public class RuleItem extends BaseEntity<RuleItem> {
    private Long ruleRealmId;
    private String name;
    private String lhs;
    private String rhs;
    private Integer rowNo;

    @Builder
    public RuleItem(Long id, Long ruleRealmId, String name, String lhs, String rhs, Integer rowNo) {
        this.setId(id);
        this.ruleRealmId = ruleRealmId;
        this.name = name;
        this.lhs = lhs;
        this.rhs = rhs;
        this.rowNo = rowNo;
    }
}
