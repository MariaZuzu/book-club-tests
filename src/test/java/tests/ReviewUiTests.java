package tests;

import com.codeborne.selenide.Selenide;
import models.clubs.ClubModel;
import models.clubs.ClubReviewListModel;
import models.clubs.ClubReviewModel;
import models.localstorage.LocalStorageAuthRequestBody;
import models.localstorage.UserData;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import testdata.ClubsTestData;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

public class ReviewUiTests extends TestBase {

    String username;
    String password;
    String correctUsername;

    @BeforeEach
    public void prepareTestData() {

        username = faker.name().firstName() + System.currentTimeMillis();
        password = "12345";
        correctUsername = "wilkinso";
    }

    @Test
    @DisplayName("Отзыв, созданный через API, отображается на странице клуба")
    public void reviewCreatedViaApiIsVisibleOnClubPageTest() {

        // 1. Регистрация нового пользователя через API
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(new RegistrationBodyModel(username, password));

        // 2. Логин пользователя через API и получение токенов
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(new LoginBodyModel(username, password));
        String accessToken = loginResponse.access();

        // 3. Создание клуба через API
        ClubModel club = api.clubs.createRandomClub(accessToken);

        // 4. Создание отзыва к клубу через API
        models.clubs.ClubReviewWriteBodyModel body = ClubsTestData.randomClubReviewBody(club.id());
        api.clubs.createClubReview(body, accessToken);

        // 5. Вход в UI через localStorage (подкладываем сессию)
        open("/favicon.ico");

        UserData userData = new UserData(registrationResponse.id(), registrationResponse.username(), registrationResponse.firstName(), registrationResponse.lastName(), registrationResponse.email(), registrationResponse.remoteAddr());
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(userData, loginResponse.access(), loginResponse.refresh(), true);
        Selenide.localStorage().setItem("book_club_auth", authBody.toJson());

        // 6. Открытие страницы созданного клуба
        open("/clubs/" + club.id());

        String snippet = body.review().length() > 120 ? body.review().substring(0, 120) : body.review();

        // 7. Проверка, что отзыв отображается на странице клуба
        element(".club-content").shouldBe(visible);
        element(".club-content").shouldHave(text(snippet));
    }

    @Test
    @DisplayName("Успешная проверка удаление отзыва  DELETE /clubs/reviews/{id}/")
    public void successfulCheckDeleteClubReviewTest() {

        // 1. Регистрация нового пользователя через API
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(new RegistrationBodyModel(username, password));

        // 2. Логин пользователя через API и получение токенов
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(new LoginBodyModel(username, password));
        String accessToken = loginResponse.access();

        // 3. Создание клуба через API
        ClubModel club = api.clubs.createRandomClub(accessToken);

        // 4. Создание отзыва к клубу через API
        models.clubs.ClubReviewWriteBodyModel body = ClubsTestData.randomClubReviewBody(club.id());

        //  сохраняем ответ
        ClubReviewModel review = api.clubs.createClubReview(body, accessToken);

        // 5. Удаляем отзыв по review.id(), так как мы сохранили его в переменной review
        api.clubs.deleteClubReview(review.id(), accessToken);

        // 6. Вход в UI через localStorage (подкладываем сессию)
        open("/favicon.ico");

        UserData userData = new UserData(registrationResponse.id(), registrationResponse.username(), registrationResponse.firstName(), registrationResponse.lastName(), registrationResponse.email(), registrationResponse.remoteAddr());
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(userData, loginResponse.access(), loginResponse.refresh(), true);
        Selenide.localStorage().setItem("book_club_auth", authBody.toJson());

        // 7. Открытие страницы созданного клуба ( удаленного клуба )
        open("/clubs/" + club.id());

        // 8. Проверка, что отзыв больше не отображается на странице клуба

        $(".no-reviews").shouldHave(text("Пока нет отзывов. Будьте первым, кто поделится впечатлениями!"));

    }

    @Test
    @DisplayName("Отображение списка отзывов о клубе GET /clubs/reviews/")
    public void reviewsListDisplayOnClubPageNewUser() {

        // 1. Регистрация нового пользователя через API
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(new RegistrationBodyModel(username, password));

        // 2. Логин пользователя через API и получение токенов
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(new LoginBodyModel(username, password));
        String accessToken = loginResponse.access();

        // 3. Создание клуба через API
        ClubModel club = api.clubs.createRandomClub(accessToken);

        // 4. Создание отзыва к клубу через API
        models.clubs.ClubReviewWriteBodyModel body = ClubsTestData.randomClubReviewBody(club.id());
        api.clubs.createClubReview(body, accessToken);

        // 5. Вход в UI через localStorage (подкладываем сессию)
        open("/favicon.ico");

        UserData userData = new UserData(registrationResponse.id(), registrationResponse.username(), registrationResponse.firstName(), registrationResponse.lastName(), registrationResponse.email(), registrationResponse.remoteAddr());
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(userData, loginResponse.access(), loginResponse.refresh(), true);
        Selenide.localStorage().setItem("book_club_auth", authBody.toJson());

        // 6. Открытие страницы созданного клуба
        clubPage.openPageById(club.id().toString())
        // 7. Проверка, что отзыв отображается на странице клуба
                .checkReviewSnippetVisible(body.review());

    }

    @Test
    @DisplayName("Отображение списка отзывов о клубе GET /clubs/reviews/")
    public void reviewsListDisplayOnClubPageNewUser_fix() {

        // 1. Регистрация нового пользователя через API
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(new RegistrationBodyModel(username, password));

        // 2. Логин пользователя через API и получение токенов
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(new LoginBodyModel(username, password));
        String accessToken = loginResponse.access();

        // 3. Создание клуба через API
        ClubModel club = api.clubs.createRandomClub(accessToken);

        // 4. Создание отзыва к клубу через API
        models.clubs.ClubReviewWriteBodyModel body = ClubsTestData.randomClubReviewBody(club.id());
        ClubReviewModel createdReview = api.clubs.createClubReview(body, accessToken);

        // 5. Вход в UI через localStorage (подкладываем сессию)
        open("/favicon.ico");

        UserData userData = new UserData(registrationResponse.id(), registrationResponse.username(), registrationResponse.firstName(), registrationResponse.lastName(), registrationResponse.email(), registrationResponse.remoteAddr());
        LocalStorageAuthRequestBody authBody = new LocalStorageAuthRequestBody(userData, loginResponse.access(), loginResponse.refresh(), true);
        Selenide.localStorage().setItem("book_club_auth", authBody.toJson());

        // 6. Открытие страницы созданного клуба
        clubPage.openPageById(club.id().toString())
        // 7. Проверка, что отзыв отображается на странице клуба
                .checkReviewSnippetVisible(body.review());
    }

    @Test
    @DisplayName("Просмотр списка отзывов под существующим пользователем")
    public void reviewsListDisplayOnClubPageOldUser() {
        username = "MashaMasha26";
        password = "MashaMasha2026";

        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(new LoginBodyModel(username, password));
        String accessToken = loginResponse.access();

        ClubReviewListModel getClubReviewList = api.clubs.getClubReviewList(2, 1, 10, accessToken);

        String expectedText = getClubReviewList.results().get(0).review();

        clubPage.openPageById("2")
                .checkReviewSnippetVisible(expectedText);
    }
}

