package org.example;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.example.builders.UserJsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.CoreTest.FEATURE_USER_AUTH;
import static org.example.api.core.ApiHeaders.SET_COOKIE;
import static org.example.api.core.ApiHeaders.X_CSRF_TOKEN;
import static org.example.api.core.ApiParams.*;

@Feature(FEATURE_USER_AUTH)
public class UserAuthTest extends TestBase {

    @DisplayName("Успешно логинимся при отправке с корректных параметров")
    @Test
    public void loginWithCorrectData() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";

        apiUserMethods.createUserWith(email, password);
        Response loginResponse = apiUserMethods.loginUser(email, password);

        checker.checkResponseCodeEquals(200, loginResponse);
        checker.checkResponseHasHeader(X_CSRF_TOKEN, loginResponse);
        checker.checkResponseHasHeader(SET_COOKIE, loginResponse);
    }

    @DisplayName("Получаем ошибку при логине с некорректными параметрами")
    @Test
    public void loginWithWrongUserData() {

        Response loginResponse = apiUserMethods.loginUser("qw;knjwf", "kldmwekldmweld");

        checker.checkResponseCodeEquals(400, loginResponse);
        checker.checkResponseDoesntHaveHeader(X_CSRF_TOKEN, loginResponse);
        checker.checkResponseDoesntHaveHeader(SET_COOKIE, loginResponse);
    }

    @DisplayName("Получаем корректный ответ при запросе данных НЕавторизованного пользователя по его id")
    @Test
    public void getUnauthorizedUserById() {

        Response createUserResponse = apiUserMethods.createUserWithDefaultParams();
        String userId = responseHelper.getFieldValue(ID, createUserResponse);

        Response getUserResponse = apiUserMethods.getUserDataAsNotBeingLogged(userId);

        checker.checkResponseCodeEquals(200, getUserResponse);
        checker.checkResponseHasField(USERNAME, getUserResponse);
        checker.checkResponseDoesntHaveField(FIRST_NAME, getUserResponse);
        checker.checkResponseDoesntHaveField(LAST_NAME, getUserResponse);
        checker.checkResponseDoesntHaveField(EMAIL, getUserResponse);
        checker.checkResponseDoesntHaveField(ID, getUserResponse);
    }

    @DisplayName("Получаем корректный ответ при запросе данных авторизованного пользователя по его id")
    @Test
    public void getAuthorizedUserById() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";
        apiUserMethods.createUserWith(email, password);

        Response loginResponse = apiUserMethods.loginUser(email, password);
        checker.checkResponseCodeEquals(200, loginResponse);

        String cookie = responseHelper.getCookies(loginResponse);
        String token = responseHelper.getHeaderValue(X_CSRF_TOKEN, loginResponse);
        String userId = responseHelper.getFieldValue(USER_ID, loginResponse);

        Response getUserResponse = apiUserMethods.getUserDataAsBeingLogged(userId, token, cookie);

        checker.checkResponseCodeEquals(200, getUserResponse);
        checker.checkResponseHasField(FIRST_NAME, getUserResponse);
        checker.checkResponseHasField(LAST_NAME, getUserResponse);
        checker.checkResponseHasField(EMAIL, getUserResponse);
        checker.checkResponseHasField(ID, getUserResponse);
    }

    @DisplayName("Параметры пользователя успешно обновляются")
    @Test
    public void updateUserData() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";
        String newFirstName = "NEWFIRSTNAME";
        String newLastName = "NEWLASTNAME";

        apiUserMethods.createUserWith(email, password);

        Response loginResponse = apiUserMethods.loginUser(email, password);
        checker.checkResponseCodeEquals(200, loginResponse);

        String cookie = responseHelper.getCookies(loginResponse);
        String token = responseHelper.getHeaderValue(X_CSRF_TOKEN, loginResponse);
        String userId = responseHelper.getFieldValue(USER_ID, loginResponse);

        String newUserData = new UserJsonBuilder()
                .addFirstName(newFirstName)
                .addLastName(newLastName)
                .build();
        Response updateUserResponse = apiUserMethods.updateUserData(newUserData, token, cookie);
        checker.checkResponseCodeEquals(200, updateUserResponse);

        Response getUserResponse = apiUserMethods.getUserDataAsBeingLogged(userId, token, cookie);

        checker.checkResponseCodeEquals(200, getUserResponse);
        checker.checkFieldValueEquals(newFirstName, FIRST_NAME, getUserResponse);
        checker.checkFieldValueEquals(newLastName, LAST_NAME, getUserResponse);
    }
}
