package com.qualitycheck.impl;

import com.domain.entity.Company;
import com.qualitycheck.AbstractQualityCheckStrategy;
import com.qualitycheck.QualityCheckContext;
import org.springframework.stereotype.Component;

/**
 * @author carl
 */
@Component
public class QualityCheckStrategyCompany extends AbstractQualityCheckStrategy {

    @Override
    public QualityCheckContext<Company> check(String context) {
        return null;
    }

    @Override
    public String support() {
        return "company";
    }
}
