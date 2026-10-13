package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ProfilePage extends BasePage {
    private SelenideElement logoutButton = $(".logout-btn");

    String pagePath = "/profile";

    @Step("[UI] Открытие страницы профиля пользователя")
    public ProfilePage openPage() {
        open(pagePath);

        return this;
    }

    @Step("[UI] Нажатие кнопки выхода из аккаунта")
    public LoginPage pressLogoutButton() {
        logoutButton.click();

        return new LoginPage();
    }
}
