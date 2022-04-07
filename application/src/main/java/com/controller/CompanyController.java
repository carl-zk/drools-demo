package com.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import com.domain.dto.CompanyDTO;
import com.domain.entity.Company;
import com.domain.mapstruct.CompanyStructMapper;
import com.domain.service.CompanyService;
import com.support.BeanUtils;
import com.support.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author carl
 */
@RestController
@RequestMapping("/api")
public class CompanyController {
    private CompanyStructMapper companyStructMapper;
    private CompanyService companyService;

    public CompanyController(CompanyStructMapper companyStructMapper, CompanyService companyService) {
        this.companyStructMapper = companyStructMapper;
        this.companyService = companyService;
    }

    @PostMapping("/company")
    public Result<Void> createCompany(@RequestBody CompanyDTO companyDTO) {
        Company company = companyStructMapper.toCompany(companyDTO);
        companyService.save(company);
        return Result.success();
    }

    @PutMapping("/company")
    public Result<Void> updateCompany(@RequestBody CompanyDTO companyDTO) {
        Company company = companyService.getById(companyDTO.getId());
        companyStructMapper.updateCompany(companyDTO, company);
        companyService.updateById(company);
        return Result.success();
    }

    @PatchMapping("/company")
    public Result<Void> patchCompany(@RequestBody CompanyDTO companyDTO) {
        Company company = companyService.getById(companyDTO.getId());
        BeanUtils.copyNonNullProperties(companyDTO, company);
        companyService.updateById(company);
        return Result.success();
    }

    @DeleteMapping("/company/{id}")
    public Result<Void> deleteCompany(@PathVariable("id") Long companyId) {
        companyService.removeById(companyId);
        return Result.success();
    }

    @GetMapping("/company/{id}")
    public Result<CompanyDTO> getCompany(@PathVariable("id") Long companyId) {
        Company company = companyService.getById(companyId);
        return Result.success(companyStructMapper.toCompanyDTO(company));
    }

    @GetMapping("/companies")
    public Result<Page<CompanyDTO>> listCompanies(@RequestParam(required = false, defaultValue = "1") Integer current,
                                                  @RequestParam(required = false, defaultValue = "10") Integer size) {
        IPage<Company> p = Page.of(current, size);
        companyService.page(p);
        Page<CompanyDTO> pageDTO = PageDTO.of(p.getCurrent(), p.getSize(), p.getTotal());
        pageDTO.setRecords(companyStructMapper.toCompanyDTOs(p.getRecords()));
        return Result.success(pageDTO);
    }
}