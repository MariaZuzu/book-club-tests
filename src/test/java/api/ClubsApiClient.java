package api;

import io.qameta.allure.Step;
import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import models.clubs.CreateClubBodyModel;
import net.datafaker.Faker;

import static io.restassured.RestAssured.given;
import static specs.clubs.ClubsSpec.*;

public class ClubsApiClient {

    @Step("[API] Получение списка клубов GET /clubs/")
    public ClubsListResponseModel getClubs() {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/")
                .then()
                .spec(successfulClubsListResponseSpec)
                .extract()
                .as(ClubsListResponseModel.class);
    }

    @Step("[API] Создание клуба POST /clubs/")
    public ClubModel createClub(String accessToken, CreateClubBodyModel body) {
        return given(clubsRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(successfulCreateClubResponseSpec)
                .extract()
                .as(ClubModel.class);
    }

    @Step("[API] Создание рандомного клуба POST /clubs/")
    public ClubModel createRandomClub(String accessToken) {
        Faker faker = new Faker();
        String bookTitle = faker.book().title();
        String bookAuthors = faker.book().author();
        Integer publicationYear = 2009;
        String description = faker.lorem().sentence();
        String telegramChatLink = "https://t.me/qa_guru";

        CreateClubBodyModel createClubBody = new CreateClubBodyModel(
                bookTitle,
                bookAuthors,
                publicationYear,
                description,
                telegramChatLink
        );

        return createClub(accessToken, createClubBody);
    }

    @Step("[API] Попытка создать клуб без авторизации POST /clubs/")
    public void createClubWithoutAuth(CreateClubBodyModel body) {
        given(clubsRequestSpec)
                .body(body)
                .when()
                .post("/clubs/")
                .then()
                .spec(unauthorizedCreateClubResponseSpec);
    }
}
