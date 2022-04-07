package com.controller;

import com.domain.dto.RuleItemDTO;
import com.domain.dto.RuleRealmDTO;
import com.domain.entity.RuleItem;
import com.domain.entity.RuleRealm;
import com.domain.mapstruct.RuleItemStructMapper;
import com.domain.mapstruct.RuleRealmStructMapper;
import com.domain.service.RuleItemService;
import com.domain.service.RuleRealmService;
import com.domain.service.RuleService;
import com.kie.Kie;
import com.qualitycheck.QualityCheckContext;
import com.qualitycheck.QualityCheckStrategyHolder;
import com.support.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author carl
 */
@RestController
@RequestMapping("/api")
public class RuleController {
    private RuleRealmService ruleRealmService;
    private RuleItemService ruleItemService;
    private RuleService ruleService;
    private RuleRealmStructMapper ruleRealmStructMapper;
    private RuleItemStructMapper ruleItemStructMapper;
    private Kie kie;

    public RuleController(RuleRealmService ruleRealmService, RuleItemService ruleItemService, RuleService ruleService,
                          RuleRealmStructMapper ruleRealmStructMapper, RuleItemStructMapper ruleItemStructMapper, Kie kie) {
        this.ruleRealmService = ruleRealmService;
        this.ruleItemService = ruleItemService;
        this.ruleService = ruleService;
        this.ruleRealmStructMapper = ruleRealmStructMapper;
        this.ruleItemStructMapper = ruleItemStructMapper;
        this.kie = kie;
    }

    @ApiOperation("质检")
    @PostMapping("/qualityCheck")
    public Result<QualityCheckContext<?>> qualityCheck(@ApiParam(allowableValues = "company,user") @RequestParam String type, @ApiParam(example = """
            {
                "type": "user",
                "data":{
                    "age": 13,
                    "gender": "a",
                    "msg":[]
                },
                "checkFlags":["age", "gender"]
            }
            """) @RequestBody String context) {
        QualityCheckContext<?> res = QualityCheckStrategyHolder.check(type, context);
        return Result.success(res);
    }

    @PutMapping("/kie/ruleSet/{id}")
    public Result<Void> updateKieRuleOf(@PathVariable("id") Long ruleRealmId) {
        kie.reloadKieBaseModel(ruleRealmId);
        return Result.success();
    }

    @PostMapping("/ruleRealm")
    public Result<Void> createRuleRealm(@RequestBody RuleRealmDTO ruleRealmDTO) {
        RuleRealm ruleRealm = ruleRealmStructMapper.toRuleRealm(ruleRealmDTO);
        ruleRealm.insert();
        return Result.success();
    }

    @PutMapping("/ruleRealm")
    Result<Void> updateRuleRealm(@RequestBody RuleRealmDTO ruleRealmDTO) {
        RuleRealm ruleRealm = ruleRealmStructMapper.toRuleRealm(ruleRealmDTO);
        ruleRealm.updateById();
        return Result.success();
    }

    @GetMapping("/ruleRealm/{id}")
    public Result<RuleRealmDTO> getRuleRealmById(@PathVariable("id") Long ruleRealmId) {
        RuleRealm ruleRealm = ruleRealmService.getById(ruleRealmId);
        return Result.success(ruleRealmStructMapper.toRuleRealmDTO(ruleRealm));
    }

    @GetMapping("/ruleRealms")
    public Result<List<RuleRealmDTO>> listAllRuleRealms() {
        return Result.success(ruleRealmStructMapper.toRuleRealmDTOs(ruleRealmService.list()));
    }

    @PostMapping("/ruleItem")
    public Result<Void> createRuleItem(@RequestBody RuleItemDTO ruleItemDTO) {
        RuleItem ruleItem = ruleItemStructMapper.toRuleItem(ruleItemDTO);
        ruleItem.insert();
        return Result.success();
    }

    @ApiOperation(value = "更新ruleItem", notes = "需要乐观锁的情况，需要先取数db中的对象，然后setVersion才可以")
    @PutMapping("/ruleItem")
    public Result<Void> updateRuleItem(@RequestBody RuleItemDTO ruleItemDTO) {
        RuleItem ruleItem = ruleItemStructMapper.toRuleItem(ruleItemDTO);
        ruleItem.setVersion(ruleItemService.getById(ruleItem.getId()).getVersion());
        ruleItemService.updateById(ruleItem);
        return Result.success();
    }

    @GetMapping("/ruleItem/{id}")
    public Result<RuleItemDTO> getRuleItem(@PathVariable("id") Long ruleItemId) {
        return Result.success(ruleItemStructMapper.toRuleItemDTO(ruleItemService.getById(ruleItemId)));
    }

    @GetMapping("/ruleItems")
    public Result<List<RuleItemDTO>> listAllRuleItems() {
        return Result.success(ruleItemStructMapper.toRuleItemDTOs(ruleItemService.list()));
    }
}
