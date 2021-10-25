package org.example.api.core;

import io.qameta.allure.Step;
import io.qameta.allure.restassured.AllureRestAssured;
import io.restassured.http.Header;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;
import static org.example.api.core.ApiHeaders.COOKIE_AUTH_SID;
import static org.example.api.core.ApiHeaders.X_CSRF_TOKEN;

public class ApiCoreRequests {

    @Step("Совершаем GET запрос")
    public Response makeGetRequest(String url) {
        return given()
                .filter(new AllureRestAssured())
                .get(url)
                .andReturn();
    }

    @Step("Совершаем GET запрос с токеном и куками")
    public Response makeGetRequest(String url, String token, String cookie) {

        return given()
                .filter(new AllureRestAssured())
                .header(new Header(X_CSRF_TOKEN, token))
                .cookie(COOKIE_AUTH_SID, cookie)
                .get(url)
                .andReturn();
    }

    @Step("Совершаем POST запрос")
    public Response makePostRequest(String url, String body) {

        return given()
                .filter(new AllureRestAssured())
                .body(body)
                .post(url)
                .andReturn();
    }

    @Step("Совершаем PUT запрос с токеном и куками")
    public Response makePutRequest(String url, String body, String token, String cookie) {

        return given()
                .filter(new AllureRestAssured())
                .header(new Header(X_CSRF_TOKEN, token))
                .cookie(COOKIE_AUTH_SID, cookie)
                .body(body)
                .put(url)
                .andReturn();
    }
}
