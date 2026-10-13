package tests;

import io.qameta.allure.Description;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;


@Feature("Клубы")
public class ClubsTests extends TestBase {

    @Test
    @Description("Публичный GET /clubs/ без авторизации: объект ответа не null, count ≥ 0, results не null и размер списка равен count.")
    @DisplayName("[API] GET /clubs/ — ответ не null, структура валидна")
    @Severity(SeverityLevel.CRITICAL)
    public void getClubsReturns200AndValidStructure() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response).isNotNull();
        assertThat(response.count()).isGreaterThanOrEqualTo(0);
        assertThat(response.results()).isNotNull();
        assertThat(response.results()).hasSize(response.count());
    }

    @Test
    @Description("Инвариант пагинации: для текущей страницы число элементов в results должно совпадать с полем count.")
    @DisplayName("[API] GET /clubs/ — count совпадает с размером results")
    @Severity(SeverityLevel.NORMAL)
    public void getClubsCountMatchesResultsSize() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.results())
                .as("count должно совпадать с размером results")
                .hasSize(response.count());
    }

    @Test
    @Description("Для каждой записи в results проверяются id, книжные поля, владелец, участники, отзывы и дата создания.")
    @DisplayName("[API] GET /clubs/ — у каждого клуба заполнены обязательные поля")
    @Severity(SeverityLevel.NORMAL)
    public void getClubsEachClubHasRequiredFields() {
        ClubsListResponseModel response = api.clubs.getClubs();

        for (ClubModel club : response.results()) {
            assertThat(club.id()).isNotNull().isPositive();
            assertThat(club.bookTitle()).isNotNull();
            assertThat(club.bookAuthors()).isNotNull();
            assertThat(club.publicationYear()).isNotNull();
            assertThat(club.description()).isNotNull();
            assertThat(club.telegramChatLink()).isNotNull();
            assertThat(club.owner()).isNotNull().isPositive();
            assertThat(club.members()).isNotNull();
            assertThat(club.reviews()).isNotNull();
            assertThat(club.created()).isNotNull();
        }
    }

    @Test
    @Description("Базовые поля ответа списка (count и results) присутствуют; ссылки next/previous могут быть null.")
    @DisplayName("[API] GET /clubs/ — поля пагинации присутствуют")
    @Severity(SeverityLevel.MINOR)
    public void getClubsPaginationFieldsPresent() {
        ClubsListResponseModel response = api.clubs.getClubs();

        assertThat(response.count()).isNotNull();
        // next и previous могут быть null при одной странице
        assertThat(response.results()).isNotNull();
    }
}
