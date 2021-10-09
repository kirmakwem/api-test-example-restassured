package org.example;

import io.restassured.response.Response;
import org.example.builders.RequestParamsBuilder;
import org.example.builders.UserBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.api.ApiParams.ID;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CreateUserTest extends TestBase {

    private static final String UNEXPECTED_STATUS_CODE_MESSAGE = "Код ответа не соответствует ожидаемому";

    @DisplayName("Пользователь успешно создаётся")
    @Test
    public void userCreatedSuccessfully() {

        String email = dataHelper.generateUniqueEmail();

        String bodyWithValidData = new UserBuilder()
                .withFirstName("SOME NAME")
                .withLastName("SOME LAST NAME")
                .withPassword("SOME PASSWORD")
                .withUsername("SOME USERNAME")
                .withEmail(email)
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, bodyWithValidData);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(200, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertTrue(checker.responseHasField(responseBodyString, ID),
                "Тело ответа не содержит параметр " + ID);
    }

    @DisplayName("Получаем ошибку при создании юзера без указания всех параметров")
    @Test
    public void createUserWithEmptyData() {

        String errorMessage = "The following required params are missed: email, password, username, firstName, lastName";

        String emptyBody = new RequestParamsBuilder().build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, emptyBody);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertEquals(errorMessage, responseBodyString,
                "Текст сообщения об отсутствии всех параметров не соответствует ожидаемому");
    }

    @DisplayName("Получаем ошибку о создании юзера без указания email")
    @Test
    public void noEmail() {

        String errorMessage = "The following required params are missed: email";

        String  bodyWithoutEmail = new UserBuilder()
                .withFirstName("SOME NAME")
                .withLastName("SOME LAST NAME")
                .withPassword("SOME PASSWORD")
                .withUsername("SOME USERNAME")
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, bodyWithoutEmail);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertEquals(errorMessage, responseBodyString,
                "Текст сообщения об отсутствии параметра email не соответствует ожидаемому");
    }

    @DisplayName("Получаем ошибку при создании юзера с уже занятым email")
    @Test
    public void emailExistsInSystem() {

        String email = dataHelper.generateUniqueEmail();
        String errorMessage = "Users with email '" + email + "' already exists";

        dataHelper.createDefaultUser(email, "password");

        String bodyWithAlreadyExistingEmail = new UserBuilder()
                .withFirstName("SOME NAME")
                .withLastName("SOME LAST NAME")
                .withPassword("SOME PASSWORD")
                .withUsername("SOME USERNAME")
                .withEmail(email)
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, bodyWithAlreadyExistingEmail);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertEquals(errorMessage, responseBodyString,
                "Текст сообщения о занятости указанного email не соответствует ожидаемому");
    }

    @DisplayName("Получаем ошибку при создании юзера с некорректной формой email")
    @Test
    public void emailNotValid() {

        String errorMessage = "Invalid email format";

        String  bodyWithInvalidEmail = new UserBuilder()
                .withFirstName("SOME NAME")
                .withLastName("SOME LAST NAME")
                .withPassword("SOME PASSWORD")
                .withUsername("SOME USERNAME")
                .withEmail("invalidemail@")
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, bodyWithInvalidEmail);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertEquals(errorMessage, responseBodyString,
                "Текст сообщения о неверном формате email не соответствует ожидаемому");
    }

    @DisplayName("Получаем ошибку при создании юзера со слишком коротким именем")
    @Test
    public void tooShortUsername() {

        String errorMessage = "The value of 'username' field is too short";
        String email = dataHelper.generateUniqueEmail();

        String bodyWithShortName = new UserBuilder()
                .withFirstName("SOME NAME")
                .withLastName("SOME LAST NAME")
                .withPassword("SOME PASSWORD")
                .withUsername("i")
                .withEmail(email)
                .build();

        Response response = apiMethods.makePostRequest(Endpoints.USER, bodyWithShortName);
        String responseBodyString = responseHelper.getResponseBody(response);

        assertEquals(400, response.statusCode(), UNEXPECTED_STATUS_CODE_MESSAGE);
        assertEquals(errorMessage, responseBodyString,
                "Текст сообщения о слишком коротком username не соответствует ожидаемому");
    }
}
