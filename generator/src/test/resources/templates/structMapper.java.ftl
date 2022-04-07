package ${package.StructMapper};

import ${package.DTO}.${table.dtoName};
import ${package.Entity}.${entity};
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
* @author ${author}
*/
@Mapper
public interface ${table.structMapperName} {

    ${table.dtoName} to${table.dtoName}(${entity} ${entity?uncap_first});

    List<${table.dtoName}> to${table.dtoName}s(List<${entity}> ${entity?uncap_first}s);

    ${entity} to${entity}(${table.dtoName} ${table.dtoName?uncap_first});

    void update${entity}(${table.dtoName} ${table.dtoName?uncap_first}, @MappingTarget ${entity} ${entity?uncap_first});
}
