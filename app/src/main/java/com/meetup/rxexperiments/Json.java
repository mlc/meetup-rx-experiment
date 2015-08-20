package com.meetup.rxexperiments;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.MappingJsonFactory;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Json {
    private static final ObjectMapper objectMapper;
    private static final JsonFactory factory;

    static {
        ObjectMapper om = new ObjectMapper();
        om.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        om.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);
        objectMapper = om;
        factory = new MappingJsonFactory(om);
    }

    private Json() {
        throw new UnsupportedOperationException();
    }

    public static JsonFactory getJsonFactory() {
        return factory;
    }
}
