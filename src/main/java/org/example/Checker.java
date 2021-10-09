package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static org.example.api.ApiHeaders.SET_COOKIE;
import static org.example.api.ApiHeaders.X_CSRF_TOKEN;

/*
Класс для хранения методов, выполняющих проверку условий
 */
public class Checker {

    private final ObjectMapper objectMapper = new ObjectMapper();

    // Проверяем наличие хедера x-csrf-token
    public Boolean responseHasToken(Response response) {
        return response.header(X_CSRF_TOKEN) != null;
    }

    // Проверяем наличие хедера Set-Cookie
    public Boolean responseHasCookie(Response response) {
        return response.header(SET_COOKIE) != null;
    }

    // Проверяем, что строка ответа содержит в себе указанное поле
    public Boolean responseHasField(String response, String fieldName) {

        try {
            return objectMapper.readTree(response).has(fieldName);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
