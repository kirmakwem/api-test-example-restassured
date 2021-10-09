package org.example.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static org.example.api.ApiHeaders.COOKIE_AUTH_SID;

/*
Класс для хранения вспомогательных методов,
которые выполняют различные действия с ответом от сервера.
 */
public class ResponseHelper {

    private static final String X_CSRF_TOKEN = "x-csrf-token";
    private final ObjectMapper objectMapper = new ObjectMapper();

    // Берём из класса Response тело ответа и возвращаем в виде строки
    public String getResponseBody(Response response) {
        return response.body().asString();
    }

    // Возвращает значение токена x-csrf-token
    public String getXcsrfToken(Response response) {
        return response.header(X_CSRF_TOKEN);
    }

    // Возвращает значение cookie
    public String getCookies(Response response) {
        return response.cookie(COOKIE_AUTH_SID);
    }

    // Ищем в строке ответа нужный параметр и возвращаем его значение
    public String getFieldValue(String response, String fieldName) {

        try {
            return objectMapper.readTree(response).get(fieldName).asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
