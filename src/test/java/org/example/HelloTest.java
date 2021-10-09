package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class HelloTest {

    @DisplayName("Получаем 'Привет'")
    @Test
    public void getHello() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();

        assertEquals(200, response.statusCode(), "Код ответа не соответствует ожидаемому");
        assertEquals("{\"answer\":\"Hello, someone\"}", response.body().asString(),
                "JSON ответа не соответствует ожидаемому");
    }
}
