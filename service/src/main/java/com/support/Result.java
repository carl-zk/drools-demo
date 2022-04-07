package com.support;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author carl
 */
@Data
public class Result<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = -1481686592037868939L;

    private static final Result<Void> SUCCESS = new Result<>(null);

    private int code;
    private T data;
    private String message;

    public static Result<Void> success() {
        return SUCCESS;
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(data);
    }

    public static Result<Void> error(int code, String message) {
        return new Result<>(code, message);
    }

    private Result(T data) {
        this.data = data;
    }

    private Result(int code, String message) {
        this.code = code;
        this.message = message;
    }
}