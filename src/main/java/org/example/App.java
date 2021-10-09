package org.example;

import io.restassured.RestAssured;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

/*
Все запросы идут к API, полное описание которого находится здесь
https://playground.learnqa.ru/api/map
 */
public class App {

    public static void main(String[] args) {
//        simpleGetRequest();
//        getRequestWithQueryParams();
//        postRequestWithBody();
//        getHeader();
//        getCookie();
//        sendCookie();
    }

    private static void simpleGetRequest() {

        Response response = RestAssured.get("https://playground.learnqa.ru/api/hello");

        response.peek();
    }

    private static void getRequestWithQueryParams() {

        Response response = RestAssured
                .given()
                .queryParam("name", "Some Awesome Name")
                .get("https://playground.learnqa.ru/api/hello");

        response.peek();
    }

    private static void postRequestWithBody() {

        String simpleBody = "{\"param\": \"value\"}";

        Response response = RestAssured.given()
                .body(simpleBody)
                .post("https://playground.learnqa.ru/api/method_post_only");

        response.peek();
    }

    private static void getHeader() {

        Response response = RestAssured.post("https://playground.learnqa.ru/api/hello");

        String header = response.getHeader("Content-Type");

        System.out.println(header);
    }

    private static void sendHeader() {

        Response response = RestAssured.given()
                .log().headers()
                .header("Content-Type", "text/html")
//                .header("My header", "My header value")
                .get("https://playground.learnqa.ru/api/hello");
    }

    private static void getCookie() {

        Response response = RestAssured.given()
                .get("https://playground.learnqa.ru/api/get_cookie");

        System.out.println("Полученные cookie: " + response.cookies());

        System.out.println("Значение cookie: " + response.cookie("MyCookie"));
    }

    private static void sendCookie() {

        Response response = RestAssured.given()
                .queryParam("login", "secret_login")
                .queryParam("password", "secret_pass")
                .get("https://playground.learnqa.ru/api/get_auth_cookie");

        System.out.println("Полученные cookie: " + response.cookies());
        String cookie = response.cookie("auth_cookie");

        Response response2 = RestAssured.given()
                .cookie("auth_cookie", cookie)
                .get("https://playground.learnqa.ru/api/check_auth_cookie");

        System.out.println(response2.body());
    }
}
