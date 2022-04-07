package com;

import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.core.util.StatusPrinter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author carl
 */
@SpringBootApplication
public class DroolsDemoApplication implements CommandLineRunner {
    public static final Logger logger = LoggerFactory.getLogger(DroolsDemoApplication.class);

    public static void main(String[] args) {

        SpringApplication.run(DroolsDemoApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        logger.info("logback context is [begin]: ");
        StatusPrinter.print(lc);
        logger.info("logback context is [end]: ");
    }
}
