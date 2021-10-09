package org.example.builders;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.example.api.ApiParams;

import java.util.HashMap;

/*
Класс для формирования параметров запроса на создание пользователя с помощью паттерна Builder
https://vertex-academy.com/tutorials/ru/pattern-builder-java/
 */
public class UserBuilder {

    private final ObjectNode userParams = new ObjectMapper().createObjectNode();

    public UserBuilder withUsername(String username) {
        userParams.put(ApiParams.USERNAME, username);
        return this;
    }

    public UserBuilder withPassword(String password) {
        userParams.put(ApiParams.PASSWORD, password);
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        userParams.put(ApiParams.FIRST_NAME, firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        userParams.put(ApiParams.LAST_NAME, lastName);
        return this;
    }

    public UserBuilder withEmail(String email) {
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

