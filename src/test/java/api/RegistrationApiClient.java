package api;

import models.registration.*;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;

public class RegistrationApiClient {

    public SuccessfulRegistrationResponseModel registerUser(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);
    }

    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
//                .spec(wrongExistingUserRegistrationResponseSpec)
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    public UnsupportedMediaTypeRegistrationBodyModel registerUserWithUnsupportedMediaType(RegistrationBodyModel registrationData) {
        return given(unsupportedMediaTypeRegistrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(unsupportedMediaTypeRegistrationResponseSpec)
                .extract()
                .as(UnsupportedMediaTypeRegistrationBodyModel.class);
    }

    public EmptyFieldUsernameResponseModel registerUserWithEmptyUsername(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongUsernameResponseSpecification)
                .extract()
                .as(EmptyFieldUsernameResponseModel.class);
    }

    public WrongPasswordResponseModel registerUserWithEmptyPassword(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);
    }

    public WrongPasswordResponseModel registerUserWithTooLongPassword(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);
    }
}