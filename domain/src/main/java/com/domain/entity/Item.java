package com.domain.entity;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author carl
 */
@Getter
@Setter
@Builder
@ToString
public class Item<T> {
    private Type type;
    private T value;
    private String result;

    public enum Type {
        NULL,
        ERROR
    }
}
