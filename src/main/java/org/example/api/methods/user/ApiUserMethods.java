package org.example.api.methods.user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.example.Endpoints;
import org.example.api.core.ApiCoreRequests;
import org.example.builders.RequestParamsBuilder;
import org.example.builders.UserJsonBuilder;
import org.example.helpers.DataHelper;

import static org.example.api.core.ApiParams.EMAIL;
import static org.example.api.core.ApiParams.PASSWORD;

public class ApiUserMethods {

    private final ApiCoreRequests apiCoreRequests = new ApiCoreRequests();
    private final DataHelper dataHelper = new DataHelper();

    @Step("Создаём пользователя с дефолтными параметрами")
    public Response createUserWithDefaultParams() {
        String email = dataHelper.generateUniqueEmail();

        String requestBody = new UserJsonBuilder()
                .addFirstName("DefaultFirstName")
                .addLastName("DefaultSecondName")
                .addUsername("DefaultUsername")
                .addEmail(email)
                .addPassword("password")
                .build();

        return sendCreateUserRequest(requestBody);
    }

    @Step("Создаём пользователя с параметрами: '{userData}'")
    public Response createUser(String userData) {
        return apiCoreRequests.makePostRequest(Endpoints.USER, userData);
    }

    @Step("Создаём пользователя с email: {email}")
    public Response createUserWith(String email) {

        String requestBody = new UserJsonBuilder()
                .addFirstName("DefaultFirstName")
                .addLastName("DefaultSecondName")
                .addUsername("DefaultUsername")
                .addEmail(email)
                .addPassword("password")
                .build();

        return sendCreateUserRequest(requestBody);
    }

    @Step("Создаём пользователя с email: {email}, password: {password}")
    public Response createUserWith(String email, String password) {

        String requestBody = new UserJsonBuilder()
                .addFirstName("DefaultFirstName")
                .addLastName("DefaultSecondName")
                .addUsername("DefaultUsername")
                .addEmail(email)
                .addPassword(password)
                .build();

        return sendCreateUserRequest(requestBody);
    }

    @Step("Логинимся пользователем с email: {email}")
    public Response loginUser(String email, String password) {
        String loginBody = new RequestParamsBuilder()
                .add(EMAIL, email)
                .add(PASSWORD, password)
                .build();

        return apiCoreRequests.makePostRequest(Endpoints.LOGIN, loginBody);
    }

    @Step("Запрашиваем данные о пользователем с id={userId}, будучи не залогиненными в системе")
    public Response getUserDataAsNotBeingLogged(String userId) {
        return apiCoreRequests.makeGetRequest(Endpoints.USER + userId);
    }

    @Step("Запрашиваем данные о пользователем с id={userId}, будучи залогиненными в системе")
    public Response getUserDataAsBeingLogged(String userId, String token, String cookie) {
        return apiCoreRequests.makeGetRequest(Endpoints.USER + userId, token, cookie);
    }

    @Step("Обновляем данные пользователя")
    public Response updateUserData(String newUserData, String token, String cookie) {
        return apiCoreRequests.makePutRequest(Endpoints.USER, newUserData, token, cookie);
    }

    private Response sendCreateUserRequest(String requestBody) {
        return apiCoreRequests.makePostRequest(Endpoints.USER, requestBody);
    }
}
