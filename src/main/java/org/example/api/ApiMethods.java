package org.example.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.config.RestAssuredConfig;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static io.restassured.config.EncoderConfig.encoderConfig;
import static org.example.api.ApiHeaders.COOKIE_AUTH_SID;
import static org.example.api.ApiHeaders.X_CSRF_TOKEN;

/*
Класс для хранения методов, делающих запросы к API.
 */
public class ApiMethods {

    // Делаем запрос методом Get
    public Response makeGetRequest(String url) {
        return RestAssured.get(url).andReturn();
    }

    // Делаем запрос методом Get с хедерами Cookie и x-csrf-token
    public Response makeGetRequest(String url, String token, String cookie) {

        return given()
                .header(new Header(X_CSRF_TOKEN, token))
                .cookie(COOKIE_AUTH_SID, cookie)
                .get(url)
                .andReturn();
    }

    // Совершаем POST с json телом
    public Response makePostRequest(String url, String body) {

        return given()
                .body(body)
                .post(url)
                .andReturn();
    }

    // Совершаем POST с json телом, хедерами Cookie и x-csrf-token
    public Response makePutRequest(String url, String body, String token, String cookie) {

        return given()
                .header(new Header(X_CSRF_TOKEN, token))
                .cookie(COOKIE_AUTH_SID, cookie)
                .body(body)
                .put(url)
                .andReturn();
    }
}
