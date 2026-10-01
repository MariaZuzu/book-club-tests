package tests;

import models.registration.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static org.assertj.core.api.Assertions.assertThat;
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
    @DisplayName("Успешная регистрация пользователя")
    public void successfulRegistrationTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.registration().registerUser(registrationData);

        step("Проверка соответствия обновленных данных ожидаемым", () -> {
            assertThat(registrationResponse.id()).isGreaterThan(0);
            assertThat(registrationResponse.username()).isEqualTo(username);
            assertThat(registrationResponse.firstName()).isEqualTo("");
            assertThat(registrationResponse.lastName()).isEqualTo("");
            assertThat(registrationResponse.email()).isEqualTo("");
            assertThat(registrationResponse.remoteAddr()).matches(REGISTRATION_IP_REGEXP);
        });
    }

    @Test
    @DisplayName("Ошибка при повторной регистрация пользователя")
    public void existingUserRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        SuccessfulRegistrationResponseModel registrationResponse = api.registration().registerUser(registrationData);

        step("Проверка получения username в ответе", () ->
                assertThat(registrationResponse.username()).isEqualTo(username));

        ExistingUserResponseModel secondRegistrationResponse = api.registration().registerExistingUser(registrationData);

        step("Проверка соответствия полученной ошибки ожидаемой", () ->
                assertThat(secondRegistrationResponse.username().get(0)).isEqualTo(REGISTRATION_EXISTING_USER_ERROR));

    }

    @Test
    @DisplayName("Ошибка при регистрации с неподдерживаемым Content-Type")
    public void unsupportedMediaTypeRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
        UnsupportedMediaTypeRegistrationBodyModel unsupportedMediaTypeResponseModel =
                api.registration().registerUserWithUnsupportedMediaType(registrationData);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(unsupportedMediaTypeResponseModel.detail()).isEqualTo(EXPECTED_ERROR_UNSUPPORTED_MEDIA_TYPE);
        });
    }

    @Test
    @DisplayName("Ошибка при регистрации с пустым password")
    public void emptyPasswordRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, "");
        WrongPasswordResponseModel wrongPasswordResponseModel = api.registration().registerUserWithEmptyPassword(registrationData);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(wrongPasswordResponseModel.password().get(0)).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
        });
    }

    @Test
    @DisplayName("Ошибка при регистрации со слишком длинным password")
    public void passwordLongerRequiredLengthRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, tooLongPassword);
        WrongPasswordResponseModel wrongPasswordResponseModel = api.registration().registerUserWithTooLongPassword(registrationData);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(wrongPasswordResponseModel.password().get(0)).isEqualTo(EXPECTED_ERROR_LONGER_REQUIRED_LENGTH_PASSWORD);
        });
    }

    @Test
    @DisplayName("Ошибка при регистрации с пустым username")
    public void emptyUsernameRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel("", password);

        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel = api.registration().registerUserWithEmptyUsername(registrationData);

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(emptyFieldUsernameResponseModel.username().get(0)).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
        });
    }

}
