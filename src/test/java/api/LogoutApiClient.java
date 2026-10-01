package api;

import io.qameta.allure.Step;
import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongReusedRefreshTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.logout.LogoutSpec.*;

public class LogoutApiClient {

    @Step("Выход пользователя из системы с refresh token")
    public void logoutWithRefreshToken(LogoutBodyModel logoutData) {
        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    @Step("Повторно выполнить logout с тем же refresh token")
    public WrongReusedRefreshTokenResponseModel logoutAgainWithSameRefreshToken(LogoutBodyModel logoutData) {
        return given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract().as(WrongReusedRefreshTokenResponseModel.class);
    }

    @Step("Выход пользователя из системы без refresh token")
    public WithoutRefreshTokenLogoutResponseModel logoutWithoutRefreshToken(WithoutRefreshTokenLogoutBodyModel logoutData) {
        return given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(withoutRefreshTokenLogoutResponseSpec)
                .extract().as(WithoutRefreshTokenLogoutResponseModel.class);
    }

    @Step("Ошибка при выходе пользователя из системы с access token вместо refresh token")
    public WrongReusedRefreshTokenResponseModel logoutWithAccessTokenInsteadOfRefreshToken(LogoutBodyModel logoutData) {
        return given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract().as(WrongReusedRefreshTokenResponseModel.class);
    }

}