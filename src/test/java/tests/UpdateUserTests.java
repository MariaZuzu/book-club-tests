package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.user.*;
import org.junit.jupiter.api.BeforeEach;
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
    public void successfulFullUpdateUserWithPutTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        SuccessfulUpdateUserResponseModel responseUpdateUser =
                step("Полное обновление данных пользователя через PUT", () -> {
                    UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName,
                            td.lastName, td.email);
                    return api.updateUser().updateUserWithPut(updateUserData, accessToken);
                });

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
    public void successfulFullUpdateUserWithPatchTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData =
                    new RegistrationBodyModel(td.username, td.password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизоваться и получить access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        SuccessfulUpdateUserResponseModel responseUpdateUser =
                step("Обновление данных пользователя через PATCH", () -> {
                    UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName,
                            td.lastName, td.email);
                    return api.updateUser().updateUserWithPatch(updateUserData, accessToken);
                });

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
    public void successfulPartialUpdateUserWithPatchTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData =
                    new RegistrationBodyModel(td.username, td.password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        SuccessfulUpdateUserResponseModel responseUpdateUser =
                step("Частичное обновление данных пользователя через PATCH", () -> {
                    PartialUpdateUserBodyModel updateUserData =
                            new PartialUpdateUserBodyModel(td.username, td.email);
                    return api.updateUser().partiallyUpdateUserWithPatch(updateUserData, accessToken);
                });

        step("Проверка соответствия обновленных данных ожидаемым", () -> {
            assertThat(responseUpdateUser.id()).isPositive();
            assertThat(responseUpdateUser.username()).isEqualTo(td.username);
            assertThat(responseUpdateUser.email()).isEqualTo(td.email);
            assertThat(responseUpdateUser.remoteAddr()).isNotBlank();
        });
    }

    @Test
    public void partialUpdateUserWithPutNegativeTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData =
                    new RegistrationBodyModel(td.username, td.password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel responseLogin = api.login().loginUser(loginData);
            return responseLogin.access();
        });

        UnsuccessfulPartialUpdateUserResponseModel responseUpdateUser =
                step("Ошибка при частичном обновлении пользователя через PUT", () -> {
                    PartialUpdateUserBodyModel updateUserData = new PartialUpdateUserBodyModel(td.username, td.email);
                    return api.updateUser().partiallyUpdateUserWithPutExpectingError(updateUserData, accessToken);
                });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(responseUpdateUser.firstName().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
            assertThat(responseUpdateUser.lastName().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
            assertThat(responseUpdateUser.username()).isNull();
            assertThat(responseUpdateUser.email()).isNull();
        });
    }

    @Test
    public void withoutRequiredAuthorizationHeaderUpdateUserNegativeTest() {

        UnauthorizedResponseModel responseUpdateUser =
                step("Проверить ошибку при обновлении пользователя без Authorization header", () -> {
                    UpdateUserBodyModel updateUserData = new UpdateUserBodyModel(td.username, td.firstName,
                            td.lastName, td.email);
                    return api.updateUser().updateUserWithoutAuthorizationHeader(updateUserData);
                });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(responseUpdateUser.detail()).isEqualTo(EXPECTED_UNAUTHORIZED_ERROR);
        });
    }
}
