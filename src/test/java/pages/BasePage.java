package pages;

import api.ApiClient;
import io.qameta.allure.Step;
import models.localstorage.LocalStorageAuthRequestBody;
import models.localstorage.UserData;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import net.datafaker.Faker;

import static com.codeborne.selenide.Selenide.localStorage;
import static com.codeborne.selenide.Selenide.open;
import static tests.TestData.*;

public class BasePage {
    ApiClient api = new ApiClient();

    @Step("[UI] Регистрация пользователя, установка сессии и открытие страницы:")
    public SuccessfulLoginResponseModel openBlankPageWithNewUser() {
        Faker faker = new Faker();
        String username = faker.name().firstName() + faker.name().lastName();
        String password = "12345";

        // register user
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.users.register(registrationData);

        // login user
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        UserData userData = new UserData(
                registrationResponse.id(),
                registrationResponse.username(),
                registrationResponse.firstName(),
                registrationResponse.lastName(),
                registrationResponse.email(),
                registrationResponse.remoteAddr());
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(
                userData,
                loginResponse.access(),
                loginResponse.refresh(),
                true);

        openFaviconAndSetLocalStorage("book_club_auth", authBody.toJson());

        return loginResponse;
    }


    @Step("[UI] Открытие страницы с существующим пользователем")
    public void openBlankPageWithExistingUser() {
        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        UserData userData = new UserData(
                Integer.parseInt(LOGIN_ID),
                LOGIN_USERNAME,
                "",
                "",
                "",
                ""
        );
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(
                userData,
                accessToken,
                refreshToken,
                true
        );
        openFaviconAndSetLocalStorage("book_club_auth", authBody.toJson());
    }

    @Step("[UI] Открытие /favicon.ico и установка данных в localstorage")
    public void openFaviconAndSetLocalStorage(String key, String value) {
        openFavicon();
        setLocalStorage(key, value);
    }

    @Step("[UI] Установка данных в localstorage")
    public void setLocalStorage(String key, String value) {
        localStorage().setItem(key, value);
    }

    @Step("[UI] Открытие /favicon.ico")
    public void openFavicon () {
        open("/favicon.ico");
    }
}
