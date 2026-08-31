package tests;

import models.registration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static specs.registration.RegistrationSpec.*;
import static tests.TestData.*;

public class RegistrationTests extends TestBase {

    String username;
    String password;

    @BeforeEach
    public void prepareTestData() {
        // оставляем генерацию данных в тесте, чтобы каждый запуск был с новыми пользователями
        username = "user_" + System.currentTimeMillis();
        password = "pass_" + System.currentTimeMillis();
    }

    @Test
    public void successfulRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertThat(registrationResponse.id()).isGreaterThan(0);
        assertThat(registrationResponse.username()).isEqualTo(username);
        assertThat(registrationResponse.firstName()).isEqualTo("");
        assertThat(registrationResponse.lastName()).isEqualTo("");
        assertThat(registrationResponse.email()).isEqualTo("");

        assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
    }

    @Test
    public void existingUserWrongRegistrationTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel firstRegistrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(successfulRegistrationResponseSpec)
                .extract()
                .as(SuccessfulRegistrationResponseModel.class);

        assertThat(firstRegistrationResponse.username()).isEqualTo(username);

        ExistingUserResponseModel secondRegistrationResponse = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(existingUserRegistrationResponseSpec)
                .extract()
                .as(ExistingUserResponseModel.class);

        String expectedError = REGISTRATION_EXISTING_USER_ERROR;
        String actualError = secondRegistrationResponse.username().get(0);
        assertThat(actualError).isEqualTo(expectedError);
    }

    @Test
    public void unsupportedMediaTypeRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        UnsupportedMediaTypeRegistrationBodyModel unsupportedMediaTypeResponseModel =
                given(unsupportedMediaTypeRegistrationRequestSpec)
                        .body(registrationData)
                        .when()
                        .post("/users/register/")
                        .then()
                        .spec(unsupportedMediaTypeRegistrationResponseSpec)
                        .extract()
                        .as(UnsupportedMediaTypeRegistrationBodyModel.class);

        String actualError = unsupportedMediaTypeResponseModel.detail();
        assertThat(actualError).isEqualTo(EXPECTED_ERROR_UNSUPPORTED_MEDIA_TYPE);
    }

    @Test
    public void emptyPasswordRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, "");

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
    }

    @Test
    public void passwordLongerRequiredLengthRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, tooLongPassword);

        WrongPasswordResponseModel wrongPasswordResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongPasswordResponseSpecification)
                .extract()
                .as(WrongPasswordResponseModel.class);

        String actualError = wrongPasswordResponseModel.password().get(0);
        assertThat(actualError).isEqualTo(EXPECTED_ERROR_LONGER_REQUIRED_LENGTH_PASSWORD);

    }

    @Test
    public void emptyUsernameRegistrationNegativeTest() {
        RegistrationBodyModel registrationData = new RegistrationBodyModel("", password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = given(registrationRequestSpec)
                .body(registrationData)
                .when()
                .post("/users/register/")
                .then()
                .spec(wrongUsernameResponseSpecification)
                .extract()
                .as(EmptyFieldUsernameResponseModel.class);

        String actualError = emptyFieldUsernameResponseModel.username().get(0);
        assertThat(actualError).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
    }

}
