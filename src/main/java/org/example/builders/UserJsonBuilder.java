package org.example.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.api.core.ApiParams;

/*
Класс для формирования параметров запроса на создание пользователя с помощью паттерна Builder
https://vertex-academy.com/tutorials/ru/pattern-builder-java/
 */
public class UserJsonBuilder {

    private final ObjectNode userParams = new ObjectMapper().createObjectNode();

    public UserJsonBuilder addUsername(String username) {
        userParams.put(ApiParams.USERNAME, username);
        return this;
    }

    public UserJsonBuilder addPassword(String password) {
        userParams.put(ApiParams.PASSWORD, password);
        return this;
    }

    public UserJsonBuilder addFirstName(String firstName) {
        userParams.put(ApiParams.FIRST_NAME, firstName);
        return this;
    }

    public UserJsonBuilder addLastName(String lastName) {
        userParams.put(ApiParams.LAST_NAME, lastName);
        return this;
    }

    public UserJsonBuilder addEmail(String email) {
        userParams.put(ApiParams.EMAIL, email);
        return this;
    }

    public String build() {
        ObjectMapper objMapper = new ObjectMapper();
        try {
            return objMapper.writeValueAsString(userParams);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

