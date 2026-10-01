package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.user.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;
import static tests.TestData.password;

public class UpdateUserTests extends TestBase {
    TestData td = new TestData();

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    @DisplayName("Полное обновление данных пользователя через PUT")
    public void successfulFullUpdateUserWithPutTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        api.registration().registerUser(registrationData);

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName,
                td.lastName, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.updateUser().updateUserWithPut(updateUserData, accessToken);

        step("Проверка соответствия обновленных данных ожидаемым", () -> {
            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(responseUpdateUser.username()).isEqualTo(td.username);
            assertThat(responseUpdateUser.firstName()).isEqualTo(td.firstName);
            assertThat(responseUpdateUser.lastName()).isEqualTo(td.lastName);
            assertThat(responseUpdateUser.email()).isEqualTo(td.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Обновление данных пользователя через PATCH")
    public void successfulFullUpdateUserWithPatchTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        api.registration().registerUser(registrationData);

        String accessToken = step("Авторизоваться и получить access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName, td.lastName, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.updateUser().updateUserWithPatch(updateUserData, accessToken);

        step("Проверка соответствия обновленных данных ожидаемым", () -> {
            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(responseUpdateUser.username()).isEqualTo(td.username);
            assertThat(responseUpdateUser.firstName()).isEqualTo(td.firstName);
            assertThat(responseUpdateUser.lastName()).isEqualTo(td.lastName);
            assertThat(responseUpdateUser.email()).isEqualTo(td.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Частичное обновление данных пользователя через PATCH")
    public void successfulPartialUpdateUserWithPatchTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        api.registration().registerUser(registrationData);

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        PartialUpdateUserBodyModel updateUserData = new PartialUpdateUserBodyModel(td.username, td.email);
        SuccessfulUpdateUserResponseModel responseUpdateUser = api.updateUser().partiallyUpdateUserWithPatch(updateUserData, accessToken);

        step("Проверка соответствия обновленных данных ожидаемым", () -> {
            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(responseUpdateUser.username()).isEqualTo(td.username);
            assertThat(responseUpdateUser.email()).isEqualTo(td.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    @DisplayName("Ошибка при частичном обновлении пользователя через PUT")
    public void partialUpdateUserWithPutNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
        api.registration().registerUser(registrationData);

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel responseLogin = api.login().loginUser(loginData);
            return responseLogin.access();
        });

        PartialUpdateUserBodyModel updateUserData = new PartialUpdateUserBodyModel(td.username, td.email);
        UnsuccessfulPartialUpdateUserResponseModel responseUpdateUser =
                api.updateUser().partiallyUpdateUserWithPutExpectingError(updateUserData, accessToken);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(responseUpdateUser.firstName().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
            assertThat(responseUpdateUser.lastName().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
            assertThat(responseUpdateUser.username()).isNull();
            assertThat(responseUpdateUser.email()).isNull();
        });
    }

    @Test
    @DisplayName("Ошибка при обновлении пользователя без Authorization header")
    public void withoutRequiredAuthorizationHeaderUpdateUserNegativeTest() {

        UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName,
                td.lastName, td.email);
        UnauthorizedResponseModel responseUpdateUser = api.updateUser().updateUserWithoutAuthorizationHeader(updateUserData);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(responseUpdateUser.detail()).isEqualTo(EXPECTED_UNAUTHORIZED_ERROR);
        });
    }
}
