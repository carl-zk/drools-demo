package com.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 *
 * @author carl
 */
@Getter
@Setter
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
@TableName("KIE_HIS")
public class KieHis extends BaseEntity<KieHis> {
    /**
     * aka. ruleRealm.type
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String kieBaseModelName;
    /**
     * aka. ruleRealm.group
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String packageName;
    /**
     * full rule content
     */
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private String content;

    @Builder
    public KieHis(String kieBaseModelName, String packageName, String content, Long id) {
        this.setId(id);
        this.kieBaseModelName = kieBaseModelName;
        this.packageName = packageName;
        this.content = content;
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
                ", kieBaseModelName=" + kieBaseModelName +
                ", packageName=" + packageName +
                ", content=" + content +
                '}';
    }
}
