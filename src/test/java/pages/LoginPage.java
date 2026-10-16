package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class LoginPage  extends BasePage {
    private SelenideElement loginContainer = $("[data-testid=login-container]");

    @Step("[UI] Проверка, что открыта страница входа")
    public LoginPage loginPageShouldBeOpened() {
        webdriver().shouldHave(urlContaining("/signin"));
        loginContainer.shouldBe(visible);

        return this;
    }
}
