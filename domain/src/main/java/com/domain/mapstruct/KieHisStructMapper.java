package com.domain.mapstruct;

import com.domain.dto.KieHisDTO;
import com.domain.entity.KieHis;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
* @author carl
*/
@Mapper
public interface KieHisStructMapper {

    KieHisDTO toKieHisDTO(KieHis kieHis);

    List<KieHisDTO> toKieHisDTOs(List<KieHis> kieHiss);

    KieHis toKieHis(KieHisDTO kieHisDTO);

    void updateKieHis(KieHisDTO kieHisDTO, @MappingTarget KieHis kieHis);
}
