package api;

import io.qameta.allure.Step;
import models.login.*;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;

public class LoginApiClient {

    @Step("Авторизация и получение access и refresh token")
    public SuccessfulLoginResponseModel loginUser(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    @Step("Ошибка при авторизации с неверным password")
    public InvalidCredentialsLoginResponseModel loginUserWithWrongPassword(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(invalidCredentialsLoginResponseSpec)
                .extract().as(InvalidCredentialsLoginResponseModel.class);
    }

    @Step("Ошибка при обновлении токена без refresh token")
    public WithoutRefreshTokenLoginResponseModel updateTokenWithoutRefreshToken(WithoutRefreshTokenLoginBodyModel emptyRefreshToken) {
        return given(loginRequestSpec)
                .body(emptyRefreshToken)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(withoutRefreshTokenResponseSpec)
                .extract().as(WithoutRefreshTokenLoginResponseModel.class);
    }

    @Step("Ошибка при обновлении токена с невалидным refresh token")
    public InvalidRefreshTokenResponseModel refreshAccessTokenWithInvalidRefreshToken(InvalidRefreshTokenBodyModel invalidTokenBodyModel) {
        return given(loginRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);
    }

    @Step("Ошибка при обновлении токена с access token вместо refresh token")
    public InvalidRefreshTokenResponseModel refreshAccessTokenWithAccessTokenInsteadOfRefreshToken(InvalidRefreshTokenBodyModel invalidTokenBodyModel) {
        return given(loginRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);
    }
}