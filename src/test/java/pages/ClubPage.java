package pages;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.text;
//import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class ClubPage extends BasePage {

    private final SelenideElement clubContent = element(".club-content");
    private final SelenideElement leaveButton = $(".leave-btn");
    private final SelenideElement error = $(".error");

    @Step("[UI] Открытие страницы клуба по id: {clubId}")
    public ClubPage openPageById(String clubId) {
        open("/clubs/" + clubId);

        return this;
    }

    @Step("[UI] Нажать «Покинуть клуб» и подтвердить диалог")
    public ClubPage pressLeaveClubButton() {
        leaveButton.click();
        confirm();
        return this;
    }

    @Step("[UI] Владелец не может покинуть клуб: подтверждение и сообщение об ошибке")
    public ClubPage assertOwnerCannotLeaveClub() {
        error.shouldHave(text("Не удалось покинуть клуб"));
        return this;
    }

    @Step("Проверить, что контент клуба отображается")
    public ClubPage checkVisible() {
        clubContent.shouldBe(visible);
        return this;
    }

    @Step("Проверить, что в карточке клуба виден фрагмент текста отзыва: {snippet}")
    public ClubPage checkReviewSnippetVisible(String snippet) {
        clubContent.shouldHave(text(snippet));
        return this;
    }
}
