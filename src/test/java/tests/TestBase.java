package tests;

import api.ApiClient;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import io.restassured.RestAssured;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import pages.ClubPage;
import pages.LoginPage;
import pages.ProfilePage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static com.codeborne.selenide.Selenide.webdriver;

public class TestBase {

    Faker faker = new Faker();
    protected static final ApiClient api = new ApiClient();

    ClubPage clubPage = new ClubPage();
    LoginPage loginPage = new LoginPage();
    ProfilePage profilePage = new ProfilePage();

    @BeforeAll
    public static void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
        RestAssured.basePath = "/api/v1";

        Configuration.baseUrl = "https://book-club.qa.guru";
        Configuration.browserSize = "1920x1080";
    }

    @BeforeEach
    void addListener() {
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterEach
    void addAttachments() {
        if(webdriver().driver().hasWebDriverStarted()) {
//            Attach.screenshotAs("Last screenshot");
//            Attach.pageSource();
//            Attach.browserConsoleLogs();
////          Attach.addVideo();
            closeWebDriver();
        }
    }
}
