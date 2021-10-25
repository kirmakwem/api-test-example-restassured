package org.example.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.response.Response;

import static org.example.api.core.ApiHeaders.COOKIE_AUTH_SID;

/*
Класс для хранения вспомогательных методов,
которые выполняют различные действия с ответом от сервера.
 */
public class ResponseHelper {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public String getHeaderValue(String headerName, Response response) {
        return response.getHeader(headerName);
    }

    // Возвращает значение cookie
    public String getCookies(Response response) {
        return response.cookie(COOKIE_AUTH_SID);
    }

    // Ищем в строке ответа нужный параметр и возвращаем его значение
    public String getFieldValue(String fieldName, Response response) {

        String responseBody = response.body().asString();

        try {
            return objectMapper.readTree(responseBody).get(fieldName).asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }
}
