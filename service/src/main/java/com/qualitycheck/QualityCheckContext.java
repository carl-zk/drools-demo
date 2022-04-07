package com.qualitycheck;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author carl
 */
@Data
@Builder
@AllArgsConstructor
public class QualityCheckContext<T> {
    private String type;
    private T data;
    private Set<String> checkFlags;
    private List<String> msg;

    public QualityCheckContext() {
        this.msg = new ArrayList<>();
    }
}
