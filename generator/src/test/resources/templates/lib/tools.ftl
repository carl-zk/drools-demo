<#function complexOf val>
    <#if val?ends_with("y")>
        <#return val?remove_ending("y") + 'ies'>
    <#else>
        <#return val>
    </#if>
</#function>