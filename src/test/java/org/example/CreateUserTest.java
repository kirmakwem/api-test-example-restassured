package org.example;

import io.qameta.allure.Feature;
import io.restassured.response.Response;
import org.example.builders.RequestParamsBuilder;
import org.example.builders.UserJsonBuilder;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.example.CoreTest.FEATURE_USER_CREATE;
import static org.example.api.core.ApiParams.ID;

@Feature(FEATURE_USER_CREATE)
public class CreateUserTest extends TestBase {

    @DisplayName("Пользователь успешно создаётся")
    @Test
    public void userCreatedSuccessfully() {

        Response createUserResponse = apiUserMethods.createUserWithDefaultParams();

        checker.checkResponseCodeEquals(200, createUserResponse);
        checker.checkResponseHasField(ID, createUserResponse);
    }

    @DisplayName("Получаем ошибку при создании юзера без указания всех параметров")
    @Test
    public void createUserWithEmptyData() {

        String errorMessage = "The following required params are missed: email, password, username, firstName, lastName";

        String emptyBody = new RequestParamsBuilder().build();

        Response createUserResponse = apiUserMethods.createUser(emptyBody);

        checker.checkResponseCodeEquals(400, createUserResponse);
        checker.checkErrorMessagesEquals(errorMessage, createUserResponse);
    }

    @DisplayName("Получаем ошибку о создании юзера без указания email")
    @Test
    public void noEmail() {

        String errorMessage = "The following required params are missed: email";

        String  bodyWithoutEmail = new UserJsonBuilder()
                .addFirstName("SOME NAME")
                .addLastName("SOME LAST NAME")
                .addPassword("SOME PASSWORD")
                .addUsername("SOME USERNAME")
                .build();

        Response createUserResponse = apiUserMethods.createUser(bodyWithoutEmail);

        checker.checkResponseCodeEquals(400, createUserResponse);
        checker.checkErrorMessagesEquals(errorMessage, createUserResponse);
    }

    @DisplayName("Получаем ошибку при создании юзера с уже занятым email")
    @Test
    public void emailExistsInSystem() {

        String email = dataHelper.generateUniqueEmail();
        String errorMessage = "Users with email '" + email + "' already exists";

        apiUserMethods.createUserWith(email);

        Response createUserWithExistingEmailResponse = apiUserMethods.createUserWith(email);

        checker.checkResponseCodeEquals(400, createUserWithExistingEmailResponse);
        checker.checkErrorMessagesEquals(errorMessage, createUserWithExistingEmailResponse);
    }

    @DisplayName("Получаем ошибку при создании юзера с некорректной формой email")
    @Test
    public void emailNotValid() {

        String errorMessage = "Invalid email format";

        Response createUserResponse = apiUserMethods.createUserWith("invalidemail@");

        checker.checkResponseCodeEquals(400, createUserResponse);
        checker.checkErrorMessagesEquals(errorMessage, createUserResponse);
    }

    @DisplayName("Получаем ошибку при создании юзера со слишком коротким именем")
    @Test
    public void tooShortUsername() {

        String errorMessage = "The value of 'username' field is too short";
        String email = dataHelper.generateUniqueEmail();

        String bodyWithShortName = new UserJsonBuilder()
                .addFirstName("SOME NAME")
                .addLastName("SOME LAST NAME")
                .addPassword("SOME PASSWORD")
                .addUsername("i")
                .addEmail(email)
                .build();

        Response createUserResponse = apiUserMethods.createUser(bodyWithShortName);

        checker.checkResponseCodeEquals(400, createUserResponse);
        checker.checkErrorMessagesEquals(errorMessage, createUserResponse);
    }
}
