# drools example
scaffold of building DDD (Domain Driven Design) projects based on spring boot.

## Prepare
`MySQL 8.0.27+`
`JDK 16+`

## Get Start
execute sql `domain/src/test/resources/init.sql`

```shell
./mvnw clean install
./mvnw spring-boot:run -pl application
```

[http://localhost:8080/api-doc/swagger-ui/](http://localhost:8080/api-doc/swagger-ui/)

## Project Structure
**modules**
- application
- domain
- generator
- service

with generator module helping, you can focus on business logic implementation in service module.
all you need to do is create domain model table in mysql/oracle etc. then using generator for create all basic domain model classes
and its CRUD APIs.

this project example using drools to check one entity whether satisfy defined rules.
there two simple rules:
```
rule "user age < 11"
when
  $ctx : QualityCheckContext(checkFlags contains "age")
  $u : User(age < 11) from $ctx.data
then
  logger.info("user age < 11! user : {}", $u);
  $ctx.msg.add("user age < 11");
end

rule "user gender is unknown"
when
  $ctx : QualityCheckContext(checkFlags contains "gender")
  $u : User(gender == null || gender != "M" && gender != "F") from $ctx.data
then
  logger.info("user gender is unknown! user : {}", $u);
  $ctx.msg.add("user gender is unknown");
end
```

calling `/api/qualityCheck` in rule-controller to see what happens.

## Code Snippet

### batch command
```java
KieContainerSessionsPool pool = kc.newKieSessionsPool(10);
StatelessKieSession ss = pool.newStatelessKieSession("kieSession");
List<Command> commands = new ArrayList<>();
commands.add(CommandFactory.newSetGlobal("logger", logger, false));
QualityCheckContext<User> ctx = QualityCheckContext.<User>builder()
        .type("user")
        .data(User.builder().age(17).gender("A").build())
        .checkFlags(new HashSet<>(Arrays.asList("age", "gender")))
        .msg(new ArrayList<>())
        .build();
commands.add(CommandFactory.newInsert(ctx, "0"));

ss.execute(CommandFactory.newBatchExecution(commands));
```

## new founds
bugs:
雪花算法id长度超出js承受范围，后两位被强制置零。解决：long -> string

features:
recompile java class : `shift + command + F9`

## Reference
[https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api](https://www.baeldung.com/swagger-2-documentation-for-spring-rest-api)
