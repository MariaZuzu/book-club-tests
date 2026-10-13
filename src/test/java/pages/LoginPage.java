package pages;

public class LoginPage  extends BasePage {
    private SelenideElement loginContainer = $("[data-testid=login-container]");

    @Step("[UI] Проверка, что открыта страница входа")
    public LoginPage loginPageShouldBeOpened() {
        webdriver().shouldHave(urlContaining("/signin"));
        loginContainer.shouldBe(visible);

        return this;
    }
}
