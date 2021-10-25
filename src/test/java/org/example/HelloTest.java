package org.example;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Feature;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.RestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;
import org.example.builders.RequestParamsBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static io.restassured.RestAssured.given;
import static org.example.CoreTest.FEATURE_HELLO;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Feature(FEATURE_HELLO)
public class HelloTest extends TestBase {

    @Test
    public void getHelloDirty() {
        Response response = RestAssured
                .get("https://playground.learnqa.ru/api/hello")
                .andReturn();

        assertEquals(200, response.statusCode(), "Код ответа не соответствует ожидаемому");
        assertEquals("{\"answer\":\"Hello, someone\"}", response.body().asString(),
                "JSON ответа не соответствует ожидаемому");
    }

    @Test
    public void getAuthorizedUserByIdDirty() {

        //++++++++Формируем тело запроса на создание юзера++++++++
        long currentTime = new Date().getTime();
        String email = currentTime + "@some.com";

        String createUserBody = "{" +
                "\"firstName\": \"DefaultFirstName\"," +
                "\"lastName\": \"DefaultFirstName\"," +
                "\"username\": \"DefaultFirstName\"," +
                "\"email\":" + "\"" + email + "\","  +
                "\"password\": \"password\"" +
                "}";
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //+++++Отправляем запрос и проверяем, что он успешный+++++
        Response createUserResponse = given()
                .body(createUserBody)
                .post("https://playground.learnqa.ru/api/user")
                .andReturn();

        assertEquals(200, createUserResponse.statusCode());
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //+++++Формируем тело запроса и логинимся в систему+++++++
        String loginBody = "{" +
                "\"email\":" + "\"" + email + "\","  +
                "\"password\": \"password\"" +
                "}";

        Response loginResponse = given()
                .body(loginBody)
                .post("https://playground.learnqa.ru/api/user/login")
                .andReturn();

        assertEquals(200, loginResponse.statusCode());
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //+++++++Вытаскиваем нужные параметры из ответа++++++++++
        String cookie = loginResponse.cookie("auth_sid");
        String token = loginResponse.header("x-csrf-token");

        String userId;
        try {
            userId = new ObjectMapper().readTree(loginResponse.body().asString()).get("user_id").asText();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            userId = null;
        }
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++


        //+++++++Запрашиваем данные о пользователе++++++++++++++++
        Response getUserDataResponse = given()
                .header(new Header("x-csrf-token", token))
                .cookie("auth_sid", cookie)
                .get("https://playground.learnqa.ru/api/user/" + userId)
                .andReturn();

        assertEquals(200, getUserDataResponse.statusCode());
        //++++++++++++++++++++++++++++++++++++++++++++++++++++++++
    }
}
