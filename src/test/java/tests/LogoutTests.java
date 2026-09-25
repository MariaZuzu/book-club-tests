package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongReusedRefreshTokenResponseModel;
import models.registration.RegistrationBodyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static tests.TestData.*;


public class LogoutTests extends TestBase {

    TestData td = new TestData();

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulLogoutTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);

        step("Регистрация пользователя", () -> {
            api.registration().registerUser(registrationData);
        });

        String refreshToken = step("Авторизация и получение токена", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);

            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.refresh();
        });

        step("Выход пользователя из системы с refresh token", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            api.logout().logoutWithRefreshToken(logoutData);
        });
    }

    @Test
    public void logoutWithReusedRefreshTokenShouldReturn401Test() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);

        step("Регистрация пользователя", () ->
            api.registration().registerUser(registrationData));

        String refreshToken = step("Авторизация и получение токена", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.refresh();
        });

        step("Выход пользователя из системы с refresh token", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            api.logout().logoutWithRefreshToken(logoutData);
        });

        WrongReusedRefreshTokenResponseModel logoutResponse =
                step("Повторно выполнить logout с тем же refresh token", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(refreshToken);
            return api.logout().logoutAgainWithSameRefreshToken(logoutData);
        });

            step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(logoutResponse.detail()).isEqualTo(EXPECTED_ERROR_TOKEN_IS_BLACKLISTED);
            assertThat(logoutResponse.code()).isEqualTo(EXPECTED_TOKEN_NOT_VALID_CODE);
        });
    }

    @Test
    public void logoutWithoutRefreshTokenNegativeTest() {

        WithoutRefreshTokenLogoutResponseModel logoutResponse =
                step("Выход пользователя из системы без refresh token", () -> {
            WithoutRefreshTokenLogoutBodyModel logoutData = new WithoutRefreshTokenLogoutBodyModel();
            return api.logout().logoutWithoutRefreshToken(logoutData);
        });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(logoutResponse.refresh().get(0)).isEqualTo(EXPECTED_REQUIRED_FIELD);
        });
    }

    @Test
    public void accessTokenInsteadOfRefreshTokenNegativeTest() {

        step("Регистрация пользователя", () -> {
            RegistrationBodyModel registrationData = new RegistrationBodyModel(td.username, td.password);
            api.registration().registerUser(registrationData);
        });

        String accessToken = step("Авторизация и получение токена", () -> {
            LoginBodyModel loginData = new LoginBodyModel(td.username, td.password);
            SuccessfulLoginResponseModel loginResponse = api.login().loginUser(loginData);
            return loginResponse.access();
        });

        WrongReusedRefreshTokenResponseModel logoutResponse =
                step("Ошибка при выходе пользователя из системы с access token вместо refresh token", () -> {
            LogoutBodyModel logoutData = new LogoutBodyModel(accessToken);
            return api.logout().logoutWithAccessTokenInsteadOfRefreshToken(logoutData);
        });

            step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(logoutResponse.detail()).isEqualTo(EXPECTED_ERROR_WRONG_TOKEN_TYPE);
            assertThat(logoutResponse.code()).isEqualTo(EXPECTED_TOKEN_NOT_VALID_CODE);
        });
    }

}

