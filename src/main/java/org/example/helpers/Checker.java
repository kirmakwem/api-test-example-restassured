package org.example.helpers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.helpers.ResponseHelper;

import static org.junit.jupiter.api.Assertions.*;

public class Checker {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final ResponseHelper responseHelper = new ResponseHelper();

    @Step("Проверяем, что ответ содержит хэдэр: {headerName}")
    public void checkResponseHasHeader(String headerName, Response response) {
        assertNotNull(response.header(headerName), "Ответ не содержит хэдера " + headerName);
    }

    @Step("Проверяем, что ответ не содержит хэдэр: {headerName}")
    public void checkResponseDoesntHaveHeader(String headerName, Response response) {
        assertNull(response.header(headerName), "Ответ содержит хэдера " + headerName);
    }

    @Step("Проверяем, что ответ содержит поле: {fieldName}")
    public void checkResponseHasField(String fieldName, Response response) {

        String responseBody = response.body().asString();

        try {
            boolean result = objectMapper.readTree(responseBody).has(fieldName);
            assertTrue(result, "Тело ответа не содержит параметр " + fieldName);
        } catch (JsonProcessingException e) {
            fail("Не получилось прочитать тело ответа" + e.getMessage());
        }
    }

    @Step("Проверяем, что ответ НЕ содержит поле: {fieldName}")
    public void checkResponseDoesntHaveField(String fieldName, Response response) {

        String responseBody = response.body().asString();

        try {
            boolean result = objectMapper.readTree(responseBody).has(fieldName);
            assertFalse(result, "Тело ответа не содержит параметр " + fieldName);
        } catch (JsonProcessingException e) {
            fail("Не получилось прочитать тело ответа" + e.getMessage());
        }
    }

    @Step("Проверяем, что полученный код ответа соответствует ожидаемому: {expectedCode}")
    public void checkResponseCodeEquals(int expectedCode, Response response) {
        assertEquals(expectedCode, response.statusCode(), "Код ответа не соответствует ожидаемому");
    }

    @Step("Проверяем, что ответ содержит поле {fieldName}, имеющее значение: {expectedValue}")
    public void checkFieldValueEquals(String expectedValue, String fieldName, Response response) {
        String fieldValue = responseHelper.getFieldValue(fieldName, response);
        assertEquals(expectedValue, fieldValue, "Тело ответа не содержит параметр " + fieldName);
    }

    @Step("Проверяем, что полученное сообщение об ошибке соответствует ожидаемому: {expectedMessage}")
    public void checkErrorMessagesEquals(String expectedMessage, Response response) {
        String responseBody = response.body().asString();
        assertEquals(expectedMessage, responseBody, "Сообщение об ошибке не соответствует ожидаемому");
    }
}
