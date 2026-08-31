package tests;

import models.login.LoginBodyModel;
import models.login.SuccessfulLoginResponseModel;
import models.login.WrongCredentialsLoginResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static tests.TestData.*;

public class LoginTests extends TestBase {

//    @Test
//    public void successfulLoginTest() {
//        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_PASSWORD);
//
//        SuccessfulLoginResponseModel loginResponse = api.auth.login(loginData);
//
//        String actualAccess = loginResponse.access();
//        String actualRefresh = loginResponse.refresh();
//        assertThat(actualAccess).startsWith(LOGIN_TOKEN_PREFIX);
//        assertThat(actualRefresh).startsWith(LOGIN_TOKEN_PREFIX);
//        assertThat(actualAccess).isNotEqualTo(actualRefresh);
//    }
//
//    @Test
//    public void wrongCredentialsLoginTest() {
//        LoginBodyModel loginData = new LoginBodyModel(LOGIN_USERNAME, LOGIN_WRONG_PASSWORD);
//
//        WrongCredentialsLoginResponseModel loginResponse = api.auth.loginWrongCredentials(loginData);
//
//        String expectedDetailError = LOGIN_WRONG_CREDENTIALS_ERROR;
//        String actualDetailError = loginResponse.detail();
//        assertThat(actualDetailError).isEqualTo(expectedDetailError);
//    }

    // todo add more negative tests

}
