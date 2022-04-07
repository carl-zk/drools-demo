package com;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import com.baomidou.mybatisplus.generator.engine.FreemarkerTemplateEngine;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.Comparator;

/**
 * @author carl
 */
public class Tools {

    private String url;
    private String username;
    private String password;
    private String tables; // split with ,

    @Test
    public void generateFromDB() throws IOException {
        url = "jdbc:mysql://localhost:3306/drools-example";
        username = "root";
        password = "password";
        tables = "KIE_HIS,USER,RULE_REALM,RULE_ITEM,COMPANY";

        String userDir = System.getProperty("user.dir");
        String outputDir = userDir + "/src/test/dist";
        String xmlOutputDir = userDir + "/src/test/dist/resources/mapper";

        Files.walk(Path.of(outputDir))
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);

        FastAutoGenerator.create(url, username, password)
                .globalConfig(builder -> {
                    builder.author("carl") // 设置作者
                            .outputDir(outputDir); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent("com") // 设置父包名
                            .entity("domain.entity")
                            .mapper("domain.dao")
                            .service("domain.service")
                            .serviceImpl("domain.service.impl")
                            .controller("controller")
                            .mapstruct("domain.mapstruct")
                            .dto("domain.dto")
                            .pathInfo(Collections.singletonMap(OutputFile.xml, xmlOutputDir)); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.addInclude(tables)
                            .entityBuilder().addIgnoreColumns("id", "create_at", "version", "update_at", "update_by", "deleted")
                            .enableLombok();
                })
                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();

    }
}
