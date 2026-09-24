package api;

import models.logout.LogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutBodyModel;
import models.logout.WithoutRefreshTokenLogoutResponseModel;
import models.logout.WrongReusedRefreshTokenResponseModel;

import static io.restassured.RestAssured.given;
import static specs.logout.LogoutSpec.*;

public class LogoutApiClient {

    public void logoutWithRefreshToken(LogoutBodyModel logoutData) {
        given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(successfulLogoutResponseSpec);
    }

    public WrongReusedRefreshTokenResponseModel logoutAgainWithSameRefreshToken(LogoutBodyModel logoutData) {
        return given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(invalidTokenLogoutResponseSpec)
                .extract().as(WrongReusedRefreshTokenResponseModel.class);
    }

    public WithoutRefreshTokenLogoutResponseModel logoutWithoutRefreshToken(WithoutRefreshTokenLogoutBodyModel logoutData) {
        return given(logoutRequestSpec)
                .body(logoutData)
                .when()
                .post("/auth/logout/")
                .then()
                .spec(withoutRefreshTokenLogoutResponseSpec)
                .extract().as(WithoutRefreshTokenLogoutResponseModel.class);
    }

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