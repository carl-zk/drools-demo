package com.domain.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.*;

/**
 * @author carl
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
public class RuleRealm extends BaseEntity {
    @TableField("`type`")
    private String type;
    private String name;
    @TableField("`group`")
    private String group;
    private String header;

    @Builder
    public RuleRealm(Long id, String type, String name, String group, String header) {
        this.setId(id);
        this.type = type;
        this.name = name;
        this.group = group;
        this.header = header;
    }
}
