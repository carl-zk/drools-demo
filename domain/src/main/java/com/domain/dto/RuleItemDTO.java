package com.domain.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author carl
 */
@Getter
@Setter
@ToString
public class RuleItemDTO {
    private String id;
    private String ruleRealmId;
    private String name;
    @ApiModelProperty(example = """
            "$ctx : QualityCheckContext(checkFlags contains "gender") \n $u : User(gender == null || gender != "M" && gender != "F") from $ctx.data"
            """)
    private String lhs;
    @ApiModelProperty(example = """
            "logger.info("user gender is unknown! user : {}", $u); \n $ctx.msg.add("user gender is unknown");"
            """)
    private String rhs;
    private Integer rowNo;
}
