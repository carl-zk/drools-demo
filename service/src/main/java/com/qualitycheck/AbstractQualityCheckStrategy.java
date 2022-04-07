package com.qualitycheck;

import javax.annotation.PostConstruct;

/**
 * @author carl
 */
public abstract class AbstractQualityCheckStrategy implements QualityCheck {

    @PostConstruct
    public void afterInit() {
        QualityCheckStrategyHolder.register(this.support(), this);
    }
}
