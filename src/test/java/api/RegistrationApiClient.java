package api;

import io.qameta.allure.Step;
import models.registration.*;

import static io.restassured.RestAssured.given;
import static specs.registration.RegistrationSpec.*;

public class RegistrationApiClient {

    @Step("Успешная регистрация пользователя")
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

    @Step("Ошибка при повторной регистрации существующего пользователя")
    public ExistingUserResponseModel registerExistingUser(RegistrationBodyModel registrationData) {
        return given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);
    }

    @Step("Ошибка при регистрации с неподдерживаемым Content-Type")
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

    @Step("Ошибка при регистрации с пустым password")
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

    @Step("Ошибка при регистрации с пустым username")
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

    @Step("Ошибка при регистрации со слишком длинным password")
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