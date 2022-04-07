package com.support;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author carl
 */
public class Mapper {
    public static ObjectMapper _MAPPER = new ObjectMapper();

    static {
        _MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        _MAPPER.configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false);
        _MAPPER.setDateFormat(new SimpleDateFormat("yyyyMMdd HH:mm:ss"));
    }

    public static <T> T readValue(String json, Class<T> classType) {
        try {
            return _MAPPER.readValue(json, classType);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T readValue(String json, TypeReference<T> typeReference) {
        try {
            return _MAPPER.readValue(json, typeReference);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> String writeValueAsString(T t) {
        try {
            return _MAPPER.writeValueAsString(t);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
