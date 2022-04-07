package com.qualitycheck.impl;

import com.domain.entity.User;
import com.fasterxml.jackson.core.type.TypeReference;
import com.kie.Kie;
import com.qualitycheck.AbstractQualityCheckStrategy;
import com.qualitycheck.QualityCheckContext;
import com.support.Mapper;
import org.springframework.stereotype.Component;

/**
 * @author carl
 */
@Component
public class QualityCheckStrategyUser extends AbstractQualityCheckStrategy {
    private Kie kie;

    public QualityCheckStrategyUser(Kie kie) {
        this.kie = kie;
    }


    @Override
    public QualityCheckContext<User> check(String context) {
        QualityCheckContext<User> qcc = Mapper.readValue(context, new TypeReference<>() {
        });
        kie.qualityCheck(qcc);
        return qcc;
    }

    @Override
    public String support() {
        return "user";
    }
}
