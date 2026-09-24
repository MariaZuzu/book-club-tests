package api;

import models.user.*;

import static io.restassured.RestAssured.given;
import static specs.user.UpdateUserSpec.*;

public class UpdateUserApiClient {

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