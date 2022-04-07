package ${group};

${header?no_esc}

<#list ruleItems![] as ruleItem>
rule '${ruleItem.name?no_esc}'
when
  ${ruleItem.lhs?no_esc}
then
  ${ruleItem.rhs?no_esc}
end

</#list>