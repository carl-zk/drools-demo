package com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.domain.dto.KieHisDTO;
import com.domain.entity.KieHis;
import com.domain.mapstruct.KieHisStructMapper;
import com.domain.service.KieHisService;
import com.support.BeanUtils;
import com.support.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author carl
 */
@RestController
@RequestMapping("/api")
public class KieHisController {
    private KieHisStructMapper kieHisStructMapper;
    private KieHisService kieHisService;

    public KieHisController(KieHisStructMapper kieHisStructMapper, KieHisService kieHisService) {
        this.kieHisStructMapper = kieHisStructMapper;
        this.kieHisService = kieHisService;
    }

    @PostMapping("/kieHis")
    public Result<Void> createKieHis(@RequestBody KieHisDTO kieHisDTO) {
        KieHis kieHis = kieHisStructMapper.toKieHis(kieHisDTO);
        kieHisService.save(kieHis);
        return Result.success();
    }

    @PutMapping("/kieHis")
    public Result<Void> updateKieHis(@RequestBody KieHisDTO kieHisDTO) {
        KieHis kieHis = kieHisService.getById(kieHisDTO.getId());
        kieHisStructMapper.updateKieHis(kieHisDTO, kieHis);
        kieHisService.updateById(kieHis);
        return Result.success();
    }

    @PatchMapping("/kieHis")
    public Result<Void> patchKieHis(@RequestBody KieHisDTO kieHisDTO) {
        KieHis kieHis = kieHisService.getById(kieHisDTO.getId());
        BeanUtils.copyNonNullProperties(kieHisDTO, kieHis);
        kieHisService.updateById(kieHis);
        return Result.success();
    }

    @DeleteMapping("/kieHis/{id}")
    public Result<Void> deleteKieHis(@PathVariable("id") Long kieHisId) {
        kieHisService.removeById(kieHisId);
        return Result.success();
    }

    @GetMapping("/kieHis/{id}")
    public Result<KieHisDTO> getKieHis(@PathVariable("id") Long kieHisId) {
        KieHis kieHis = kieHisService.getById(kieHisId);
        return Result.success(kieHisStructMapper.toKieHisDTO(kieHis));
    }

    @GetMapping("/kieHis")
    public Result<Page<KieHisDTO>> listKieHis(@RequestParam(required = false, defaultValue = "1") Integer current,
                                              @RequestParam(required = false, defaultValue = "10") Integer size) {
        IPage<KieHis> p = Page.of(current, size);
        kieHisService.page(p);
        Page<KieHisDTO> pageDTO = PageDTO.of(p.getCurrent(), p.getSize(), p.getTotal());
        pageDTO.setRecords(kieHisStructMapper.toKieHisDTOs(p.getRecords()));
        return Result.success(pageDTO);
    }
}