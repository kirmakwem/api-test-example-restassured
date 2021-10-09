package org.example;

import io.restassured.response.Response;
import org.example.api.ApiParams;
import org.example.builders.RequestParamsBuilder;
import org.example.builders.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.HashMap;

import static org.example.api.ApiHeaders.SET_COOKIE;
import static org.example.api.ApiHeaders.X_CSRF_TOKEN;
import static org.example.api.ApiParams.*;
import static org.junit.jupiter.api.Assertions.*;

public class UserAuthTest extends TestBase {

    private static final String UNEXPECTED_STATUS_CODE_MESSAGE = "Код ответа не соответствует ожидаемому";
    private static final String RESPONSE_BODY_DOESNT_CONTAIN_PARAM = "Тело ответа не содержит параметр ";

    @DisplayName("Успешно логинимся при отправке с корректных параметров")
    @Test
    public void loginWithCorrectData() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";

        dataHelper.createDefaultUser(email, password);

        String loginBody = new RequestParamsBuilder()
                .add(EMAIL, email)
                .add(PASSWORD, password)
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.LOGIN, loginBody);

        assertEquals(200, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        assertTrue(checker.responseHasToken(response),
                "Ответ НЕ содержит хэдера " + X_CSRF_TOKEN);

        assertTrue(checker.responseHasCookie(response),
                "Ответ НЕ содержит хэдера " + SET_COOKIE);
    }

    @DisplayName("Получаем ошибку при логине с некорректными параметрами")
    @Test
    public void loginWithWrongUserData() {

        String loginParams = new RequestParamsBuilder()
                .add(USERNAME, "qw;knjwf")
                .add(PASSWORD, "kldmwekldmweld")
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.LOGIN, loginParams);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        assertFalse(checker.responseHasToken(response),
                "Ответ содержит хэдер " + X_CSRF_TOKEN);

        assertFalse(checker.responseHasCookie(response),
                "Ответ содержит хэдер " + SET_COOKIE);
    }

    @DisplayName("Получаем корректный ответ при запросе данных НЕавторизованного пользователя по его id")
    @Test
    public void getUnauthorizedUserById() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";

        Response createUser = dataHelper.createDefaultUser(email, password);
        String createUserString = responseHelper.getResponseBody(createUser);
        String userId = responseHelper.getFieldValue(createUserString, ID);

        Response getUserResponse = apiMethods.makeGetRequest(Endpoints.USER + "/" + userId);
        String responseAsString = responseHelper.getResponseBody(getUserResponse);

        assertEquals(200, getUserResponse.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        assertTrue(checker.responseHasField(responseAsString, USERNAME),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + USERNAME);
    }

    @DisplayName("Получаем корректный ответ при запросе данных авторизованного пользователя по его id")
    @Test
    public void getAuthorizedUserById() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";

        dataHelper.createDefaultUser(email, password);

        String loginBody = new RequestParamsBuilder()
                .add(ApiParams.EMAIL, email)
                .add(ApiParams.PASSWORD, password)
                .build();

        Response loginResponse = apiMethods.makePostRequest(Endpoints.LOGIN , loginBody);
        String loginResponseString = responseHelper.getResponseBody(loginResponse);
        String cookie = responseHelper.getCookies(loginResponse);
        String token = responseHelper.getXcsrfToken(loginResponse);
        String userId = responseHelper.getFieldValue(loginResponseString, USER_ID);

        Response getUserResponse = apiMethods.makeGetRequest(Endpoints.USER + "/" + userId, token, cookie);
        String responseAsString = responseHelper.getResponseBody(getUserResponse);

        assertEquals(200, getUserResponse.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        assertTrue(checker.responseHasField(responseAsString, FIRST_NAME),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + FIRST_NAME);

        assertTrue(checker.responseHasField(responseAsString, LAST_NAME),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + LAST_NAME);

        assertTrue(checker.responseHasField(responseAsString, EMAIL),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + EMAIL);

        assertTrue(checker.responseHasField(responseAsString, ID),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + ID);
    }

    @DisplayName("Параметры пользователя успешно обновляются")
    @Test
    public void updateUserData() {

        String email = dataHelper.generateUniqueEmail();
        String password = "password";
        String newFirstName = "NEWFIRSTNAME";
        String newLastName = "NEWLASTNAME";

        dataHelper.createDefaultUser(email, password);

        String loginBody = new RequestParamsBuilder()
                .add(ApiParams.EMAIL, email)
                .add(ApiParams.PASSWORD, password)
                .build();

        Response loginResponse = apiMethods.makePostRequest(Endpoints.LOGIN , loginBody);
        assertEquals(200, loginResponse.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        String loginResponseString = responseHelper.getResponseBody(loginResponse);
        String cookie = responseHelper.getCookies(loginResponse);
        String token = responseHelper.getXcsrfToken(loginResponse);
        String userId = responseHelper.getFieldValue(loginResponseString, USER_ID);

        String newUserData = new UserBuilder()
                .withFirstName(newFirstName)
                .withLastName(newLastName)
                .build();

        Response response = apiMethods.makePutRequest(Endpoints.USER, newUserData, token, cookie);
        assertEquals(200, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        Response getResponse = apiMethods.makeGetRequest(Endpoints.USER + "/" + userId, token, cookie);
        String getResponseString = responseHelper.getResponseBody(getResponse);

        assertEquals(200, getResponse.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);

        assertEquals(newFirstName, responseHelper.getFieldValue(getResponseString, FIRST_NAME),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + FIRST_NAME);

        assertEquals(newLastName, responseHelper.getFieldValue(getResponseString, LAST_NAME),
                RESPONSE_BODY_DOESNT_CONTAIN_PARAM + LAST_NAME);
    }
}
