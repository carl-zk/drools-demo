package com.kie;

/**
 * @author carl
 */
public class Rules {

    public static String user = """
                        package user;
                        
                        import com.domain.entity.User;
                        import com.domain.dto.UserDTO;
                        import com.qualitycheck.QualityCheckContext;
                        import org.slf4j.Logger;
                        
                        dialect  "mvel"
                        
                        global Logger logger;
                        
                        rule "user age < 18"
                        when
                          $ctx : QualityCheckContext(checkFlags contains "age")
                          $u : User(age < 18) from $ctx.data
                        then
                          logger.info("user age < 18! user : {}", $u);
                          $ctx.msg.add("user age < 18");
                        end
                        
                        rule "user gender is unknown"
                        when
                          $ctx : QualityCheckContext(checkFlags contains "gender")
                          $u : User(gender == null || gender != "M" && gender != "F") from $ctx.data
                        then
                          logger.info("user gender is unknown! user : {}", $u);
                          $ctx.msg.add("user gender is unknown");
                        end
                        
                        rule "always run"
                        when
                          Object()
                        then
                          logger.info("always run")
                        end
            """;
}
