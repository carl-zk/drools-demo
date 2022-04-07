package com.qualitycheck;

/**
 * @author carl
 */
public interface QualityCheck {
    /**
     * quality check
     *
     * @param context ctx
     */
    QualityCheckContext check(String context);

    /**
     * quality check support type
     *
     * @return type
     */
    String support();
}
