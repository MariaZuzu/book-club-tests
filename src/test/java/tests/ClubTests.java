package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.clubs.ClubModel;
import models.clubs.CreateClubBodyModel;
import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.registration.RegistrationBodyModel;
import models.registration.SuccessfulRegistrationResponseModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.ClubPage;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static tests.TestData.faker;

@Feature("Клубы")
public class ClubTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = faker.name().firstName();
        password = "12345";
    }

    @Test
    @Description("Регистрация и логин через API, создание клуба, имитация сессии в localStorage, переход на страницу клуба. " +
            "Ожидается: при попытке выйти из клуба как владелец показывается ошибка «Не удалось покинуть клуб».")
    @DisplayName("[API] Владелец клуба не может покинуть клуб")
    @Severity(SeverityLevel.CRITICAL)
    public void cantLeaveClubAsOwnerTest(){
        SuccessfulLoginResponseModel loginResponse = clubPage.openBlankPageWithNewUser();
        String accessToken = loginResponse.access();

        ClubModel createdClub = api.clubs.createRandomClub(accessToken);
        String clubId = createdClub.id().toString();

        clubPage.openPageById(clubId)
                .pressLeaveClubButton()
                .assertOwnerCannotLeaveClub();
    }

    @Test
    @Disabled("Требуются расширения @WithNewUser / @WithNewClub; без них страница и сессия не подготовлены.")
    @Description("Проверка UI при уже подготовленной странице клуба (данные задаются расширениями JUnit). " +
            "После подтверждения выхода отображается сообщение о невозможности покинуть клуб.")
    @DisplayName("[API] Владелец клуба не может покинуть клуб (расширения JUnit)")
    @Severity(SeverityLevel.NORMAL)
//    @WithNewUser
//    @WithNewClub
    public void cantLeaveClubAsOwnerTest_with_extensions (){
        clubPage.assertOwnerCannotLeaveClub();
    }

    @Test
    @Description("После регистрации пользователя и получения access-токена вызывается POST создания клуба. " +
            "Ответ сравнивается с отправленными полями; владелец клуба совпадает с id пользователя.")
    @DisplayName("[API] Создание клуба")
    @Severity(SeverityLevel.CRITICAL)
    public void createClub_byApiTest() {
        String bookTitle = "API Club " + faker.book().title();
        String bookAuthors = faker.book().author();
        Integer publicationYear = 2020;
        String description = faker.lorem().sentence();
        String telegramChatLink = "https://t.me/qa_guru_" + faker.internet().uuid();

        // register user
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(registrationData);

        // login user
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(loginData);

        String accessToken = loginResponse.access();

        // create club by API
        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        ClubModel createdClub = api.clubs.createClub(accessToken, createClubBody);

        // assertions
        org.assertj.core.api.Assertions.assertThat(createdClub.bookTitle()).isEqualTo(bookTitle);
        org.assertj.core.api.Assertions.assertThat(createdClub.bookAuthors()).isEqualTo(bookAuthors);
        org.assertj.core.api.Assertions.assertThat(createdClub.publicationYear()).isEqualTo(publicationYear);
        org.assertj.core.api.Assertions.assertThat(createdClub.description()).isEqualTo(description);
        org.assertj.core.api.Assertions.assertThat(createdClub.telegramChatLink()).isEqualTo(telegramChatLink);
        org.assertj.core.api.Assertions.assertThat(createdClub.owner()).isEqualTo(registrationResponse.id());
    }

    @Test
    @Disabled
    @Description("Черновик: регистрация/логин по API, создание клуба через UI, открытие клуба и проверка запрета выхода. Тест отключён.")
    @DisplayName("[API] Администратор не может покинуть клуб (через API логин) — отключён")
    @Severity(SeverityLevel.MINOR)
    public void cantLeaveClubAsAdminTest_with_login_by_api(){
        // register user
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.registrationApiClient.registerUser(registrationData);
        // login user
        LoginBodyModel loginData = new LoginBodyModel(username, password);
        SuccessfulLoginResponseModel loginResponse = api.loginApiClient.loginUser(loginData);

        String accessToken = loginResponse.access();
        String refreshToken = loginResponse.refresh();

        String localStorageAuthBody = """
                {
                  "user": {
                    "id": %d,
                    "username": "%s",
                    "firstName": "%s",
                    "lastName": "%s",
                    "email": "%s",
                    "remoteAddr": "%s"
                  },
                  "accessToken": "%s",
                  "refreshToken": "%s",
                  "isAuthenticated": true
                }
                """.formatted(
                registrationResponse.id(),
                registrationResponse.username(),
                registrationResponse.firstName(),
                registrationResponse.lastName(),
                registrationResponse.email(),
                registrationResponse.remoteAddr(),
                accessToken,
                refreshToken
        );

        open("/favicon.ico");
        localStorage().setItem("book_club_auth", localStorageAuthBody);
//        open("/");

        // create club
        // todo create test for navigation from Main page to Create club page
        open("https://book-club.qa.guru/clubs/create");
//        $("[data-testid=create-club-link]").click();
        $("#bookTitle").setValue(username);
        $("#bookAuthors").setValue(username);
        $("#publicationYear").setValue("2020");
        $("#description").setValue(username);
        $("#telegramChatLink").setValue("https://t.me/qa_guru" + username).pressEnter();

        // open club
        $(".clubs-list").$(byText(username))
                .parent().parent().$(".open-btn").click();

        $(".clubs-list").$(byText(username))
                .parent().parent().$(".open-btn").click();

        clubPage.assertOwnerCannotLeaveClub();
    }

    @Test
    @Disabled
    @Description("Черновик: регистрация через UI, создание клуба без вызовов API регистрации, проверка запрета выхода. Тест отключён.")
    @DisplayName("[API] Администратор не может покинуть клуб (без API) — отключён")
    @Severity(SeverityLevel.MINOR)
    public void cantLeaveClubAsAdminTest_without_api(){
        // register user
        open("https://book-club.qa.guru/signup");
        $("[data-testid=username-input]").setValue(username);
        $("[data-testid=password-input]").setValue(password);
        $("[data-testid=confirm-password-input]").setValue(password).pressEnter();
        $("[data-testid=signup-button]").should(disappear);

//        // login user
//        $("[data-testid=username-input]").setValue(username);
//        $("[data-testid=password-input]").setValue(password).pressEnter();
//        $("[data-testid=signin-button]").should(disappear);

        // create club
        // todo create test for navigation from Main page to Create club page
        // open("https://book-club.qa.guru/clubs/create");
        $("[data-testid=create-club-link]").click();
        $("#bookTitle").setValue(username);
        $("#bookAuthors").setValue(username);
        $("#publicationYear").setValue("2020");
        $("#description").setValue(username);
        $("#telegramChatLink").setValue("https://t.me/qa_guru" + username).pressEnter();

        // open club
        $(".clubs-list").$(byText(username))
                .parent().parent().$(".open-btn").click();

        clubPage.assertOwnerCannotLeaveClub();
    }

}
