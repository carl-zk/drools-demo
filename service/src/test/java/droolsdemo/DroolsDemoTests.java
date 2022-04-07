package droolsdemo;

import com.domain.entity.Item;
import com.domain.entity.User;
import com.github.jgonian.ipmath.Ipv4;
import com.github.jgonian.ipmath.Ipv4Range;
import org.drools.model.DSL;
import org.drools.model.Rule;
import org.drools.model.Variable;
import org.drools.model.impl.ModelImpl;
import org.drools.modelcompiler.ExecutableModelProject;
import org.drools.modelcompiler.builder.KieBaseBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.kie.api.KieBase;
import org.kie.api.KieServices;
import org.kie.api.builder.KieBuilder;
import org.kie.api.builder.KieFileSystem;
import org.kie.api.builder.KieModule;
import org.kie.api.builder.Message;
import org.kie.api.builder.model.KieBaseModel;
import org.kie.api.builder.model.KieModuleModel;
import org.kie.api.builder.model.KieSessionModel;
import org.kie.api.command.Command;
import org.kie.api.conf.EqualityBehaviorOption;
import org.kie.api.conf.EventProcessingOption;
import org.kie.api.runtime.*;
import org.kie.api.runtime.conf.ClockTypeOption;
import org.kie.internal.command.CommandFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.drools.model.DSL.on;
import static org.drools.model.PatternDSL.pattern;
import static org.drools.model.PatternDSL.rule;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DroolsDemoTests {
    public static final Logger logger = LoggerFactory.getLogger(DroolsDemoTests.class);

    @Test
    void contextLoads() {
        KieServices ks = KieServices.Factory.get();
        KieModuleModel kieModuleModel = ks.newKieModuleModel();
        KieBaseModel kieBaseModel1 = kieModuleModel.newKieBaseModel("KBase1")
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.CLOUD);

        KieSessionModel ksessionModel1 = kieBaseModel1.newKieSessionModel("KSession1")
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.REALTIME);

        KieFileSystem kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/KBase1/ruleSet1.drl", "");
        kfs.writeKModuleXML(kieModuleModel.toXML());
        KieBuilder kieBuilder = ks.newKieBuilder(kfs).buildAll(ExecutableModelProject.class);

        if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new Error(kieBuilder.getResults().getMessages(Message.Level.ERROR).stream().map(Message::getText)
                    .collect(Collectors.joining(",")));
        }

        KieModule km = kieBuilder.getKieModule();
        KieContainer kc = ks.newKieContainer(km.getReleaseId());
        System.out.println(kc.getReleaseId());
//        kc.newStatelessKieSession();
        KieContainerSessionsPool pool = kc.newKieSessionsPool(10);
        StatelessKieSession ss = pool.newStatelessKieSession("KSession1");
        List<Command> cmds = new ArrayList<>();

        Item a = Item.builder().type(Item.Type.NULL).value("a").build();
//        cmds.add(CommandFactory.newSetGlobal("logger", logger, false));
        cmds.add(CommandFactory.newInsert(a, "0"));
        cmds.add(CommandFactory.newInsert(Item.builder().type(Item.Type.NULL).build(), "1"));
        cmds.add(CommandFactory.newInsert(Item.builder().type(Item.Type.NULL).value("aaa").build(), "4"));
        cmds.add(CommandFactory.newInsert(Item.builder().type(Item.Type.ERROR).build(), "2"));
//        cmds.add( CommandFactory.newQuery( "Get People", "getPeople" ));

        ExecutionResults results = ss.execute(CommandFactory.newBatchExecution(cmds));

        logger.info(results.getValue("0").toString());
        logger.info(results.getValue("1").toString());
        logger.info(results.getValue("2").toString());
        System.out.println(a.getResult());


        /**
         * dynamically update rules at runtime
         */
        kieModuleModel.removeKieBaseModel("KBase1");
        KieBaseModel kieBaseModel11 = kieModuleModel.newKieBaseModel("KBase1")
                .addPackage("com.p")
                .setDefault(true)
                .setEqualsBehavior(EqualityBehaviorOption.EQUALITY)
                .setEventProcessingMode(EventProcessingOption.CLOUD);

        KieSessionModel ksessionModel11 = kieBaseModel11.newKieSessionModel("KSession1")
                .setDefault(true)
                .setType(KieSessionModel.KieSessionType.STATEFUL)
                .setClockType(ClockTypeOption.REALTIME);
        kfs = ks.newKieFileSystem();
        kfs.write("src/main/resources/com/p/ruleSet1.drl", "");
        kfs.writeKModuleXML(kieModuleModel.toXML());
        kieBuilder = ks.newKieBuilder(kfs).buildAll(ExecutableModelProject.class);

        if (kieBuilder.getResults().getMessages(Message.Level.ERROR).size() > 0) {
            throw new Error(kieBuilder.getResults().getMessages(Message.Level.ERROR).stream().map(Message::getText)
                    .collect(Collectors.joining(",")));
        }

        km = kieBuilder.getKieModule();
        kc = ks.newKieContainer(km.getReleaseId());

        pool = kc.newKieSessionsPool(10);
        ss = pool.newStatelessKieSession("KSession1");
        ss.setGlobal("logger", logger);
        ss.execute(new Object());
        System.out.println("end....");

    }

    @Test
    public void executableRuleTest() {
        Variable<User> user = DSL.declarationOf(User.class);

        Rule rule = rule("age")
                .build(pattern(user).expr("exprA", p -> p.getAge() > 18),
                        on(user).execute(adult -> System.out.println("user is adult: " + adult)));

        KieBase kieBase = KieBaseBuilder.createKieBaseFromModel(new ModelImpl().addRule(rule));

        KieSession kieSession = kieBase.newKieSession();

        kieSession.insert(new User(1L, 13, "F"));
        kieSession.insert(new User(2L, 19, "M"));

        kieSession.fireAllRules();
    }


    @ParameterizedTest
    @MethodSource("provideIpRange_ip")
    void test_ip(String start, String end, String ip, boolean isContains) {
        Ipv4 s = Ipv4.of(start);
        Ipv4 e = Ipv4.of(end);
        Ipv4Range ipv4Range = Ipv4Range.from(s).to(e);
        Ipv4 i = Ipv4.of(ip);
        assertEquals(isContains, ipv4Range.contains(i));
    }

    private static Stream<Arguments> provideIpRange_ip() {
        return Stream.of(
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.55.0", false),
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.56.1", true),
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.57.14", true),
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.76.14", true),
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.77.255", true),
                Arguments.of("192.168.56.0", "192.168.77.255", "192.168.78.255", false)
        );
    }


    @ParameterizedTest
    @MethodSource("provideIpRange_ip")
    void test_ip2(String start, String end, String ip, boolean isContains) throws UnknownHostException {
        assertEquals(isContains, checkIPv4IsInRangeByConvertingToInt(ip, start, end));
    }

    public static boolean checkIPv4IsInRangeByConvertingToInt(String inputIP, String rangeStartIP, String rangeEndIP)
            throws UnknownHostException {
        long startIPAddress = ipToLongInt(InetAddress.getByName(rangeStartIP));
        long endIPAddress = ipToLongInt(InetAddress.getByName(rangeEndIP));
        long inputIPAddress = ipToLongInt(InetAddress.getByName(inputIP));

        return (inputIPAddress >= startIPAddress && inputIPAddress <= endIPAddress);
    }

    static long ipToLongInt(InetAddress ipAddress) {
        long resultIP = 0;
        byte[] ipAddressOctets = ipAddress.getAddress();

        for (byte octet : ipAddressOctets) {
            resultIP <<= 8;
            resultIP |= octet & 0xFF;
        }
        return resultIP;
    }

}
