<#import "./lib/tools.ftl" as t>
package ${package.Controller};

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.plugins.pagination.PageDTO;
import ${package.DTO}.${table.dtoName};
import ${package.Entity}.${table.entityName};
import ${package.StructMapper}.${table.structMapperName};
import ${package.Service}.${table.serviceName};
import com.support.BeanUtils;
import com.support.Result;
import org.springframework.web.bind.annotation.*;

/**
 * @author ${author}
 */
@RestController
@RequestMapping("/api")
public class ${table.controllerName} {
    private ${table.structMapperName} ${table.structMapperName?uncap_first};
    private ${table.serviceName} ${table.serviceName?uncap_first};

    public ${table.controllerName}(${table.structMapperName} ${table.structMapperName?uncap_first}, ${table.serviceName} ${table.serviceName?uncap_first}) {
        this.${table.structMapperName?uncap_first} = ${table.structMapperName?uncap_first};
        this.${table.serviceName?uncap_first} = ${table.serviceName?uncap_first};
    }

    @PostMapping("/${table.entityName?uncap_first}")
    public Result<Void> create${table.entityName}(@RequestBody ${table.dtoName} ${table.dtoName?uncap_first}) {
        ${table.entityName} ${table.entityName?uncap_first} = ${table.structMapperName?uncap_first}.to${table.entityName}(${table.dtoName?uncap_first});
        ${table.serviceName?uncap_first}.save(${table.entityName?uncap_first});
        return Result.success();
    }

    @PutMapping("/${table.entityName?uncap_first}")
    public Result<Void> update${table.entityName}(@RequestBody ${table.dtoName} ${table.dtoName?uncap_first}) {
        ${table.entityName} ${table.entityName?uncap_first} = ${table.serviceName?uncap_first}.getById(${table.dtoName?uncap_first}.getId());
        ${table.structMapperName?uncap_first}.update${table.entityName}(${table.dtoName?uncap_first}, ${table.entityName?uncap_first});
        ${table.serviceName?uncap_first}.updateById(${table.entityName?uncap_first});
        return Result.success();
    }

    @PatchMapping("/${table.entityName?uncap_first}")
    public Result<Void> patch${table.entityName}(@RequestBody ${table.dtoName} ${table.dtoName?uncap_first}) {
        ${table.entityName} ${table.entityName?uncap_first} = ${table.serviceName?uncap_first}.getById(${table.dtoName?uncap_first}.getId());
        BeanUtils.copyNonNullProperties(${table.dtoName?uncap_first}, ${table.entityName?uncap_first});
        ${table.serviceName?uncap_first}.updateById(${table.entityName?uncap_first});
        return Result.success();
    }

    @DeleteMapping("/${table.entityName?uncap_first}/{id}")
    public Result<Void> delete${table.entityName}(@PathVariable("id") Long ${table.entityName?uncap_first}Id) {
        ${table.serviceName?uncap_first}.removeById(${table.entityName?uncap_first}Id);
        return Result.success();
    }

    @GetMapping("/${table.entityName?uncap_first}/{id}")
    public Result<${table.dtoName}> get${table.entityName}(@PathVariable("id") Long ${table.entityName?uncap_first}Id) {
        ${table.entityName} ${table.entityName?uncap_first} = ${table.serviceName?uncap_first}.getById(${table.entityName?uncap_first}Id);
        return Result.success(${table.structMapperName?uncap_first}.to${table.dtoName}(${table.entityName?uncap_first}));
    }

    @GetMapping("/${t.complexOf(table.entityName)?uncap_first}")
    public Result<Page<${table.dtoName}>> list${t.complexOf(table.entityName)}(@RequestParam(required = false, defaultValue = "1") Integer current,
                                           @RequestParam(required = false, defaultValue = "10") Integer size) {
        IPage<${table.entityName}> p = Page.of(current, size);
        ${table.serviceName?uncap_first}.page(p);
        Page<${table.dtoName}> pageDTO = PageDTO.of(p.getCurrent(), p.getSize(), p.getTotal());
        pageDTO.setRecords(${table.structMapperName?uncap_first}.to${table.dtoName}s(p.getRecords()));
        return Result.success(pageDTO);
    }
}