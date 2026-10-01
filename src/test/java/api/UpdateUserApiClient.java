package api;

import io.qameta.allure.Step;
import models.user.*;

import static io.restassured.RestAssured.given;
import static specs.user.UpdateUserSpec.*;

public class UpdateUserApiClient {

    @Step("Полное обновление данных пользователя через PUT")
    public SuccessfulUpdateUserResponseModel updateUserWithPut(UpdateUserBodyModel updateUserData, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateUserData)
                .when()
                .put("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(SuccessfulUpdateUserResponseModel.class);
    }

    @Step("Обновление данных пользователя через PATCH")
    public SuccessfulUpdateUserResponseModel updateUserWithPatch(UpdateUserBodyModel updateUserData, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateUserData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(SuccessfulUpdateUserResponseModel.class);
    }

    @Step("Частичное обновление данных пользователя через PATCH")
    public SuccessfulUpdateUserResponseModel partiallyUpdateUserWithPatch(PartialUpdateUserBodyModel updateUserData, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateUserData)
                .when()
                .patch("/users/me/")
                .then()
                .spec(successfulUpdateUserResponseSpec)
                .extract().as(SuccessfulUpdateUserResponseModel.class);
    }

    @Step("Ошибка при частичном обновлении пользователя через PUT")
    public UnsuccessfulPartialUpdateUserResponseModel partiallyUpdateUserWithPutExpectingError(PartialUpdateUserBodyModel updateUserData, String accessToken) {
        return given(updateUserRequestSpec)
                .header("Authorization", "Bearer " + accessToken)
                .body(updateUserData)
                .when()
                .put("/users/me/")
                .then()
                .spec(unsuccessfulPartialUpdateUserResponseSpec)
                .extract().as(UnsuccessfulPartialUpdateUserResponseModel.class);
    }

    @Step("Ошибка при обновлении пользователя без Authorization header")
    public UnauthorizedResponseModel updateUserWithoutAuthorizationHeader(UpdateUserBodyModel updateUserData) {
        return given(updateUserRequestSpec)
                .body(updateUserData)
                .when()
                .put("/users/me/")
                .then()
                .spec(unauthorizedResponseSpec)
                .extract().as(UnauthorizedResponseModel.class);
    }

}