package helpers;

import com.codeborne.selenide.Selenide;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.qameta.allure.Step;
import models.localstorage.LocalstorageAuthModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.SuccessfulRegistrationResponseModel;

public class AuthHelper {

    private static final String STORAGE_KEY = "book_club_auth";
    private static final ObjectMapper mapper = new ObjectMapper();

    // ====================== Основные публичные методы ======================

    @Step("Авторизация через API + localStorage (после регистрации)")
    public static void loginAndSetLocalStorage(SuccessfulRegistrationResponseModel regResponse,
                                               SuccessfulLoginResponseModel loginResponse) {

        var auth = new LocalstorageAuthModel.BookClubAuth(
                new LocalstorageAuthModel.User(
                        regResponse.id(),
                        regResponse.username(),
                        regResponse.firstName(),
                        regResponse.lastName(),
                        regResponse.email(),
                        regResponse.remoteAddr()
                ),
                loginResponse.access(),
                loginResponse.refresh(),
                true
        );

        setToLocalStorage(auth);        // ← один универсальный метод
    }

    @Step("Авторизация через API + localStorage для ранее зарегистрированного пользователя {username}")
    public static void loginAndSetLocalStorage(SuccessfulLoginResponseModel loginResponse,
                                               String username) {

        var auth = new LocalstorageAuthModel.BookClubAuth(
                new LocalstorageAuthModel.User(
                        null,
                        username,
                        "",
                        "",
                        username + "@test.com",
                        ""
                ),
                loginResponse.access(),
                loginResponse.refresh(),
                true
        );

        setToLocalStorage(auth);        // ← тот же самый метод
    }


    // ====================== Универсальный метод сохранения ======================

    /**
     * Один метод, который отвечает только за сохранение в localStorage.
     * Используется обоими публичными методами.
     */
    private static void setToLocalStorage(LocalstorageAuthModel.BookClubAuth auth) {
        try {
            String json = mapper.writeValueAsString(auth);

            Selenide.localStorage().setItem(STORAGE_KEY, json);
            Selenide.refresh();                    // обязательно!

            System.out.println("✅ localStorage 'book_club_auth' успешно установлен");
        } catch (Exception e) {
            throw new RuntimeException("Ошибка при сохранении auth в localStorage", e);
        }
    }
}
