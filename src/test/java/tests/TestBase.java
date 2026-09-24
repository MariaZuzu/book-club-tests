package tests;

import api.ApiClient;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;

public class TestBase {

    protected static final ApiClient api = new ApiClient();


    @BeforeEach
    public void setUp() {
        RestAssured.baseURI = "https://book-club.qa.guru";
        RestAssured.basePath = "/api/v1";
    }
}
