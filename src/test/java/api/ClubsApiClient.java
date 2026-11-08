package api;

import io.qameta.allure.Step;
import models.clubs.*;
import net.datafaker.Faker;
import org.junit.jupiter.api.Disabled;

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
        String bookTitle = faker.book().title() + System.currentTimeMillis();
        String bookAuthors = faker.book().author();
        Integer publicationYear = 2010;
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

    @Step("Получить отзыв по id с авторизацией")
    public ClubReviewModel getClubReviewById(int reviewId, String accessToken) {
        return given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .get("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(successfulGetClubReviewResponseSpec)
                .extract().as(ClubReviewModel.class);
    }

    @Step("Получить отзыв по id без авторизации")
    public ClubReviewModel getClubReviewByIdWithoutAuth(int reviewId) {
        return given(clubsRequestSpec)
                .when()
                .get("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(successfulGetClubReviewResponseSpec)
                .extract().as(ClubReviewModel.class);
    }

    @Step("Получить список отзывов о клубе c авторизацией GET /clubs/reviews/")
    public ClubReviewListModel getClubReviewList(int clubId, int pageNumber, int pageSize, String accessToken) {
        return given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .queryParam("club", clubId)
                .queryParam("page", pageNumber)
                .queryParam("page_size", pageSize)
                .when()
                .get("/clubs/reviews/")
                .then()
                .spec(successfulGetClubReviewListResponseSpec)
                .extract().as(ClubReviewListModel.class);
    }

    @Step("Создать отзыв о клубе POST /clubs/reviews/")
    public ClubReviewModel createClubReview(ClubReviewWriteBodyModel body, String accessToken) {
        return given(clubsRequestSpec)
                .body(body)
                .auth().oauth2(accessToken)
                .when()
                .post("/clubs/reviews/")
                .then()
                .spec(successfulCreateClubReviewResponseSpec)
                .extract().as(ClubReviewModel.class);
    }

    @Step("Обновить отзыв методом PUT /clubs/reviews/{id}/")
    public ClubReviewModel updateClubReviewPut(int reviewId, ClubReviewWriteBodyModel body, String accessToken) {
        return given(clubsRequestSpec)
                .body(body)
                .auth().oauth2(accessToken)
                .when()
                .put("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(successfulPutClubReviewResponseSpec)
                .extract().as(ClubReviewModel.class);
    }

    @Step("Удалить отзыв DELETE /clubs/reviews/{id}/")
    public void deleteClubReview(int reviewId, String accessToken) {
        given(clubsRequestSpec)
                .auth().oauth2(accessToken)
                .when()
                .delete("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(successfulDeleteClubReviewResponseSpec);
    }

    @Disabled
    @Step("Попытаться обновить отзыв PUT без авторизации")
    public UnauthorizedClubCreateResponseModel updateClubReviewPutWithoutAuth(int reviewId, ClubReviewWriteBodyModel body) {
        return given(clubsRequestSpec)
                .body(body)
                .when()
                .put("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(unauthorizedPutClubReviewResponseSpec)
                .extract().as(UnauthorizedClubCreateResponseModel.class);
    }

    @Disabled
    @Step("Попытаться удалить отзыв без авторизации")
    public UnauthorizedClubCreateResponseModel deleteClubReviewWithoutAuth(int reviewId) {
        return given(clubsRequestSpec)
                .when()
                .delete("/clubs/reviews/" + reviewId + "/")
                .then()
                .spec(unauthorizedDeleteClubReviewResponseSpec)
                .extract().as(UnauthorizedClubCreateResponseModel.class);
    }

}
