package ${package.Entity};

import com.baomidou.mybatisplus.annotation.FieldStrategy;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.*;

/**
 *
 * @author ${author}
 */
@Getter
@Setter
@EqualsAndHashCode(of = {""}, callSuper = true)
@NoArgsConstructor
@TableName("${table.name}")
public class ${entity} extends BaseEntity<${entity}> {
<#list table.fields as field>
    <#if field.comment!?length gt 0>
    /**
     * ${field.comment}
     */
    </#if>
    @TableField(updateStrategy = FieldStrategy.IGNORED)
    private ${field.propertyType} ${field.propertyName};
</#list>

    @Builder
    public ${entity}(<#list table.fields as f>${f.propertyType} ${f.propertyName}, </#list>Long id) {
        this.setId(id);
        <#list table.fields as f>
        this.${f.propertyName} = ${f.propertyName};
        </#list>
    }

    @Override
    public String toString() {
        return "{" + super.toString() +
    <#list table.fields as field>
                ", ${field.propertyName}=" + ${field.propertyName} +
    </#list>
                '}';
    }
}
