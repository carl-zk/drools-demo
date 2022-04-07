package ${package.DTO};

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
* @author ${author}
*/
@Getter
@Setter
@ToString
public class ${table.dtoName} {
    private String id;
<#list table.fields as field>
    <#if field.propertyName?ends_with("Id")>
    private String ${field.propertyName};
    <#else>
    private ${field.propertyType} ${field.propertyName};
    </#if>
</#list>
}
