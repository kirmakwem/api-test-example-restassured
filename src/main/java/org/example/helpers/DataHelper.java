package org.example.helpers;

import io.restassured.response.Response;
import org.example.Endpoints;
import org.example.builders.UserBuilder;
import org.example.api.ApiMethods;

import java.util.Date;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;

/*
Класс хранит методы для создания часто используемых тестовых данных.
 */
public class DataHelper {

    private final ApiMethods apiMethods = new ApiMethods();

    // Генерация уникального email на основе текущего timestamp
    public String generateUniqueEmail() {
        long currentTime = new Date().getTime();
        return currentTime + "@some.com";
    }

    // Создание пользователя с дефолтными firstName, lastName и userName
    public Response createDefaultUser(String email, String password) {

        String requestBody = new UserBuilder()
                .withFirstName("DefaultFirstName")
                .withLastName("DefaultSecondName")
                .withUsername("DefaultUsername")
                .withEmail(email)
                .withPassword(password)
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, requestBody);
        assertEquals(200, response.statusCode());

        return response;
    }
}
