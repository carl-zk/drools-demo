package com.domain.mapstruct;

import com.domain.dto.CompanyDTO;
import com.domain.entity.Company;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
* @author carl
*/
@Mapper
public interface CompanyStructMapper {

    CompanyDTO toCompanyDTO(Company company);

    List<CompanyDTO> toCompanyDTOs(List<Company> companys);

    Company toCompany(CompanyDTO companyDTO);

    void updateCompany(CompanyDTO companyDTO, @MappingTarget Company company);
}
