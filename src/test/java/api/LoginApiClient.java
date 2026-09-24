package api;

import models.login.*;

import static io.restassured.RestAssured.given;
import static specs.login.LoginSpec.*;

public class LoginApiClient {

    public SuccessfulLoginResponseModel loginUser(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(successfulLoginResponseSpec)
                .extract().as(SuccessfulLoginResponseModel.class);
    }

    public InvalidCredentialsLoginResponseModel loginUserWithWrongPassword(LoginBodyModel loginData) {
        return given(loginRequestSpec)
                .body(loginData)
                .when()
                .post("/auth/token/")
                .then()
                .spec(invalidCredentialsLoginResponseSpec)
                .extract().as(InvalidCredentialsLoginResponseModel.class);
    }

    public WithoutRefreshTokenLoginResponseModel updateTokenWithoutRefreshToken(WithoutRefreshTokenLoginBodyModel emptyRefreshToken) {
        return given(loginRequestSpec)
                .body(emptyRefreshToken)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(withoutRefreshTokenResponseSpec)
                .extract().as(WithoutRefreshTokenLoginResponseModel.class);
    }

    public InvalidRefreshTokenResponseModel refreshAccessTokenWithInvalidRefreshToken(InvalidRefreshTokenBodyModel invalidTokenBodyModel) {
        return given(loginRequestSpec)
                .body(invalidTokenBodyModel)
                .when()
                .post("/auth/token/refresh/")
                .then()
                .spec(invalidRefreshTokenResponseSpec)
                .extract().as(InvalidRefreshTokenResponseModel.class);
    }

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