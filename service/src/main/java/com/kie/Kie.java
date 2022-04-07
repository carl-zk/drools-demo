package com.kie;

import com.domain.dao.RuleItemMapper;
import com.domain.dao.RuleRealmMapper;
import com.domain.dto.RuleRealmDTO;
import com.domain.entity.RuleItem;
import com.domain.entity.RuleRealm;
import com.domain.mapstruct.RuleRealmStructMapper;
import com.domain.service.RuleService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.qualitycheck.QualityCheckContext;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.drools.core.event.DebugRuleRuntimeEventListener;
import org.drools.modelcompiler.ExecutableModelProject;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieContainerSessionsPool;
import org.kie.api.runtime.StatelessKieSession;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author carl
 */
@Slf4j
@Component
public class Kie {
    private static final KieServices KS = KieServices.Factory.get();
    private KieModuleModel kmm;
    private Map<String, KieBaseModel> group2base;
    private Map<String, KieContainerSessionsPool> group2pool;
    private RuleService ruleService;
    private ExecutorService executorService;
    private RuleRealmMapper ruleRealmMapper;
    private RuleItemMapper ruleItemMapper;
    private RuleRealmStructMapper ruleRealmStructMapper;
    private Template template;

    public Kie(RuleService ruleService, RuleRealmMapper ruleRealmMapper, RuleItemMapper ruleItemMapper, RuleRealmStructMapper ruleRealmStructMapper) {
        this.kmm = KS.newKieModuleModel();
        this.group2base = new HashMap<>();
        this.group2pool = new HashMap<>();
        this.ruleService = ruleService;
        this.executorService = new ThreadPoolExecutor(1, 1, 0, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(),
                new ThreadFactoryBuilder().setNameFormat("kie-daemon-%d").setDaemon(true).setUncaughtExceptionHandler((t, e) -> {
                    log.error("load knowledge failed.", e);
                }).build());
        this.ruleRealmMapper = ruleRealmMapper;
        this.ruleItemMapper = ruleItemMapper;
        this.ruleRealmStructMapper = ruleRealmStructMapper;
    }

    @PostConstruct
    protected void init() {
        this.executorService.submit(this::loadKnowledge);
    }

    @SneakyThrows
    private void loadKnowledge() {
        StringWriter sw = new StringWriter(1 << 10);
        try {
            this.template = fetchTemplate();
        } catch (IOException e) {
            log.error("load rule template failed.", e);
            throw new Error("load rule template failed.");
        }
        List<RuleRealmDTO> ruleRealmDTOs = ruleService.listAllRuleRealmDTOs();
        for (RuleRealmDTO ruleRealmDTO : ruleRealmDTOs) {
            String group = ruleRealmDTO.getGroup();
            KieBaseModel kbm = kmm.newKieBaseModel(ruleRealmDTO.getType())
                    .addPackage(group)
                    .setDefault(true)
                    .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                    .setEventProcessingMode(EventProcessingOption.CLOUD);

            kbm.newKieSessionModel("kieSession")
                    .setDefault(true)
                    .setType(KieSessionModel.KieSessionType.STATEFUL)
                    .setClockType(ClockTypeOption.REALTIME);

            KieFileSystem kfs = KS.newKieFileSystem();
            this.template.process(ruleRealmDTO, sw);
            String ruleRealmContent = sw.toString();
            sw.getBuffer().setLength(0);
            log.info(">>>> rule {} content is :\n{}", ruleRealmDTO.getType(), ruleRealmContent);
            kfs.write("src/main/resources/" + group + "/rule.drl", ruleRealmContent);
            kfs.writeKModuleXML(kmm.toXML());
            KieBuilder kb = KS.newKieBuilder(kfs).buildAll(ExecutableModelProject.class);

            if (kb.getResults().getMessages(Message.Level.ERROR).size() > 0) {
                throw new Error(kb.getResults().getMessages(Message.Level.ERROR).stream().map(Message::getText)
                        .collect(Collectors.joining(",")));
            }

            KieModule km = kb.getKieModule();
            KieContainer kc = KS.newKieContainer(km.getReleaseId());
            KieContainerSessionsPool pool = kc.newKieSessionsPool(10);

            group2base.put(ruleRealmDTO.getType(), kbm);
            group2pool.put(ruleRealmDTO.getType(), pool);
        }
    }

    Template fetchTemplate() throws IOException {
        Configuration cfg = new Configuration(Configuration.VERSION_2_3_31);
        cfg.setClassForTemplateLoading(Kie.class, "/ftl");
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.DEBUG_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        return cfg.getTemplate("RuleRealmTemplate.ftlh");
    }

    public void reloadKieBaseModel(Long ruleRealmId) {
        RuleRealm ruleRealm = ruleRealmMapper.selectById(ruleRealmId);
        List<RuleItem> ruleItems = ruleItemMapper.findAllByRuleRealmId(ruleRealmId);
        KieBaseModel old = kmm.getKieBaseModels().get(ruleRealm.getType());
        kmm.removeKieBaseModel(ruleRealm.getType());
        KieBaseModel kbm = kmm.newKieBaseModel(ruleRealm.getType())
                .addPackage(ruleRealm.getGroup())
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.CLOUD);
        kbm.newKieSessionModel("kieSession")
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.REALTIME);
        KieFileSystem kfs = KS.newKieFileSystem();
        StringWriter sw = new StringWriter(1 << 10);
        try {
            this.template.process(ruleRealmStructMapper.toRuleRealmDTO(ruleRealm, ruleItems), sw);
        } catch (TemplateException | IOException e) {
            log.error("update rule {} failed", ruleRealmId, e);
            kmm.getKieBaseModels().put(ruleRealm.getType(), old);
            throw new RuntimeException(e);
        }
        String ruleContent = sw.toString();
        sw = null;
        log.info(">>>> rule {} content is :\n{}", ruleRealmId, ruleContent);
        kfs.write("src/main/resources/" + ruleRealm.getGroup() + "/rule.drl", ruleContent);
        kfs.writeKModuleXML(kmm.toXML());
        KieBuilder kb = KS.newKieBuilder(kfs).buildAll(ExecutableModelProject.class);

        if (kb.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new RuntimeException(kb.getResults().getMessages(Message.Level.ERROR).stream().map(Message::getText)
                    .collect(Collectors.joining(",")));
        }

        KieModule km = kb.getKieModule();
        KieContainer kc = KS.newKieContainer(km.getReleaseId());
        KieContainerSessionsPool pool = kc.newKieSessionsPool(10);

        group2base.put(ruleRealm.getType(), kbm);
        group2pool.put(ruleRealm.getType(), pool);
    }

    public <T> void qualityCheck(QualityCheckContext<T> context) {
        StatelessKieSession ss = group2pool.get(context.getType()).newStatelessKieSession();
        if (log.isDebugEnabled()) {
            ss.addEventListener(new DebugRuleRuntimeEventListener());
        }
        ss.setGlobal("logger", log);
        ss.execute(context);
        log.info("qualityCheck result : {}", context);
    }
}
