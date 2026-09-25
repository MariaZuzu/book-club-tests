package tests;

import models.registration.*;
import org.junit.jupiter.api.BeforeEach;
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
    public void successfulRegistrationTest() {

        SuccessfulRegistrationResponseModel registrationResponse =
                step("Успешная регистрация пользователя", () -> {
                    RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
                    return api.registration().registerUser(registrationData);
                });

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
    public void existingUserRegistrationNegativeTest() {

        RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);

        SuccessfulRegistrationResponseModel registrationResponse =
                step("Успешная регистрация пользователя", () ->
                        api.registration().registerUser(registrationData));

        step("Проверка получения username в ответе", () ->
                assertThat(registrationResponse.username()).isEqualTo(username));


        ExistingUserResponseModel secondRegistrationResponse =
                step("Ошибка при повторной регистрации существующего пользователя", () ->
                        api.registration().registerExistingUser(registrationData));

        step("Проверка соответствия полученной ошибки ожидаемой", () ->
                assertThat(secondRegistrationResponse.username().get(0)).isEqualTo(REGISTRATION_EXISTING_USER_ERROR));

    }

    @Test
    public void unsupportedMediaTypeRegistrationNegativeTest() {

        UnsupportedMediaTypeRegistrationBodyModel unsupportedMediaTypeResponseModel =
                step("Ошибка при регистрации с неподдерживаемым Content-Type", () -> {
                    RegistrationBodyModel registrationData = new RegistrationBodyModel(username, password);
                    return api.registration().registerUserWithUnsupportedMediaType(registrationData);
                });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(unsupportedMediaTypeResponseModel.detail()).isEqualTo(EXPECTED_ERROR_UNSUPPORTED_MEDIA_TYPE);
        });
    }

    @Test
    public void emptyPasswordRegistrationNegativeTest() {

        WrongPasswordResponseModel wrongPasswordResponseModel = step("Ошибка при регистрации с пустым password", () -> {
            RegistrationBodyModel registrationData = new RegistrationBodyModel(username, "");
            return api.registration().registerUserWithEmptyPassword(registrationData);
        });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(wrongPasswordResponseModel.password().get(0)).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
        });
    }

    @Test
    public void passwordLongerRequiredLengthRegistrationNegativeTest() {

        WrongPasswordResponseModel wrongPasswordResponseModel =
                step("Ошибка при регистрации со слишком длинным password", () -> {
                    RegistrationBodyModel registrationData = new RegistrationBodyModel(username, tooLongPassword);
                    return api.registration().registerUserWithTooLongPassword(registrationData);
                });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(wrongPasswordResponseModel.password().get(0)).isEqualTo(EXPECTED_ERROR_LONGER_REQUIRED_LENGTH_PASSWORD);
        });
    }

    @Test
    public void emptyUsernameRegistrationNegativeTest() {


        EmptyFieldUsernameResponseModel emptyFieldUsernameResponseModel =
                step("Ошибка при регистрации с пустым username", () -> {
                    RegistrationBodyModel registrationData = new RegistrationBodyModel("", password);

                    return api.registration().registerUserWithEmptyUsername(registrationData);
                });

        step("Проверка соответствия полученной ошибки ожидаемой", () -> {
            assertThat(emptyFieldUsernameResponseModel.username().get(0)).isEqualTo(EXPECTED_ERROR_NOT_BE_BLANK);
        });
    }

}
