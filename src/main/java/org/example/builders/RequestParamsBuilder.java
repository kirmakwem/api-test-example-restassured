package org.example.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.HashMap;

/*
Класс для формирования списка параметров запроса с помощью паттерна Builder
https://vertex-academy.com/tutorials/ru/pattern-builder-java/
 */
public class RequestParamsBuilder {

    private final ObjectNode bodyParams = new ObjectMapper().createObjectNode();

    public RequestParamsBuilder add(String paramName, String value) {
        bodyParams.put(paramName, value);
        return this;
    }

    public String build() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            return objMapper.writeValueAsString(bodyParams);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
