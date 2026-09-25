package tests;

import models.login.*;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulLoginTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData =
                    new RegistrationBodyModel(username, password);
            api.registration().registerUser(registrationData);
        });

        SuccessfulLoginResponseModel loginResponse =
                step("Авторизация и получение access и refresh token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(username, password);
            return api.login().loginUser(loginData);
        });
            String expectedTokenPart = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";

        step("Проверка соответствия полученных данных ожидаемым", () -> {
            assertThat(loginResponse.access()).startsWith(expectedTokenPart);
            assertThat(loginResponse.refresh()).startsWith(expectedTokenPart);
            assertThat(loginResponse.access()).isNotEqualTo(loginResponse.refresh());
        });
    }

    @Test
    public void invalidCredentialsLoginTest() {

        InvalidCredentialsLoginResponseModel loginResponse =
                step("Ошибка при авторизации с неверным password", () -> {
            LoginBodyModel loginData = new LoginBodyModel(username, wrongPassword);
            return api.login().loginUserWithWrongPassword(loginData);
        });

            step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(loginResponse.detail()).isEqualTo(EXPECTED_ERROR_INVALID_USERNAME_OR_PASSWORD);
        });
    }

    @Test
    public void emptyRefreshTokenLoginNegativeTest() {

        WithoutRefreshTokenLoginResponseModel emptyRefreshResponseModel =
                step("Ошибка при обновлении токена без refresh token", () -> {
            WithoutRefreshTokenLoginBodyModel emptyRefreshToken = new WithoutRefreshTokenLoginBodyModel();
            return api.login().updateTokenWithoutRefreshToken(emptyRefreshToken);
        });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(emptyRefreshResponseModel.refresh().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
        });
    }

    @Test
    public void invalidRefreshTokenLoginNegativeTest() {

        InvalidRefreshTokenResponseModel loginResponse =
                step("Ошибка при обновлении токена с невалидным refresh token", () -> {
            InvalidRefreshTokenBodyModel invalidTokenBodyModel = new InvalidRefreshTokenBodyModel(EXPECTED_ERROR_INVALID_REFRESH_TOKEN);
            return api.login().refreshAccessTokenWithInvalidRefreshToken(invalidTokenBodyModel);
        });

            step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(loginResponse.detail()).isEqualTo(EXPECTED_ERROR_VALID_TOKEN);
            assertThat(loginResponse.code()).isEqualTo(EXPECTED_TOKEN_NOT_VALID_CODE);
        });
    }

    @Test
    public void accessTokenInsteadRefreshTokenLoginNegativeTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData =
                    new RegistrationBodyModel(username, password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизация и получение access token", () -> {
            LoginBodyModel loginData = new LoginBodyModel(username, password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        InvalidRefreshTokenBodyModel invalidTokenBodyModel =
                step("Ошибка при обновлении токена с access token вместо refresh token", () ->
                    new InvalidRefreshTokenBodyModel(accessToken));

            InvalidRefreshTokenResponseModel refreshTokenResponse =
                    api.login().refreshAccessTokenWithAccessTokenInsteadOfRefreshToken(invalidTokenBodyModel);

        String actualDetailInvalidRefreshToken = refreshTokenResponse.detail();
        String actualCodeInvalidRefreshToken = refreshTokenResponse.code();

            step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(actualDetailInvalidRefreshToken).isEqualTo(EXPECTED_ERROR_WRONG_TOKEN_TYPE);
            assertThat(actualCodeInvalidRefreshToken).isEqualTo(EXPECTED_TOKEN_NOT_VALID_CODE);
        });
    }

}
