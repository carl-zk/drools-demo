package com.qualitycheck;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * @author carl
 */
public class QualityCheckStrategyHolder {
    public static final Logger logger = LoggerFactory.getLogger(QualityCheckStrategyHolder.class);

    private static final Map<String, QualityCheck> STRATEGY_HOLDER = new HashMap<>();

    public static void register(String type, QualityCheck qualityCheckStrategy) {
        if (STRATEGY_HOLDER.containsKey(type)) {
            throw new IllegalArgumentException(type + " strategy already exists");
        }
        STRATEGY_HOLDER.put(type, qualityCheckStrategy);
        logger.info("register quality strategy : {}", type);
    }

    public static QualityCheckContext<?> check(String type, String context) {
        return STRATEGY_HOLDER.get(type).check(context);
    }
}
