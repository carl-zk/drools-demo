package com.common;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.Context;
import ch.qos.logback.core.spi.ContextAwareBase;
import ch.qos.logback.core.spi.LifeCycle;

/**
 * @author carl
 */
public class LogbackListener extends ContextAwareBase implements LoggerContextListener, LifeCycle {
    protected boolean isStarted = false;

    @Override
    public void start() {
        Context context = getContext();
        context.putProperty("LOG_PATTERN", "%d{yyyy-MM-dd HH:mm:ss.SSS} [%thread] %-5level %logger{36}-%M:%L - %msg%n");
        context.putProperty("ROOT_LEVEL", "info");

        String level = System.getProperty("root_level");
        if (level != null) {
            context.putProperty("ROOT_LEVEL", level);
        }

        isStarted = true;
    }

    @Override
    public void stop() {
        isStarted = false;
    }

    @Override
    public boolean isStarted() {
        return isStarted;
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    public void onStart(LoggerContext context) {
    }

    @Override
    public void onReset(LoggerContext context) {

    }

    @Override
    public void onStop(LoggerContext context) {

    }

    @Override
    public void onLevelChange(Logger logger, Level level) {

    }
}
