package tests;

import net.datafaker.Faker;

public class TestData {

    public static final String LOGIN_USERNAME = "user8";
    public static final String LOGIN_PASSWORD = "user8";
    public static final String LOGIN_WRONG_PASSWORD = "qaguru1234";

    public static final String LOGIN_TOKEN_PREFIX = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9";
    public static final String LOGIN_WRONG_CREDENTIALS_ERROR = "Invalid username or password.";

    public static final String REGISTRATION_EXISTING_USER_ERROR =
            "A user with that username already exists.",
            REGISTRATION_IP_REGEXP =
            "^((25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)\\.){3}"
                    + "(25[0-5]|2[0-4]\\d|1\\d\\d|[1-9]?\\d)$",
            EXPECTED_ERROR_UNSUPPORTED_MEDIA_TYPE = "Unsupported media type \"text/plain; charset=ISO-8859-1\" in request.",
            EXPECTED_ERROR_NOT_BE_BLANK = "This field may not be blank.",
            EXPECTED_ERROR_LONGER_REQUIRED_LENGTH_PASSWORD = "Ensure this field has no more than 128 characters.";

    public static Faker faker = new Faker();

    public static final String
//            wrongPassword = password + "1",
            tooLongPassword = "a".repeat(129);

}