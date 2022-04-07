package com.qualitycheck.impl;

import com.qualitycheck.AbstractQualityCheckStrategy;
import com.qualitycheck.QualityCheckContext;
import org.springframework.stereotype.Component;

/**
 * @author carl
 */
@Component
public class QualityCheckStrategyMap extends AbstractQualityCheckStrategy {

    @Override
    public QualityCheckContext check(String context) {
        return null;
    }

    @Override
    public String support() {
        return "map";
    }
}
