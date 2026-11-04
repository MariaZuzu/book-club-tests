package specs.clubs;

import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import static io.restassured.filter.log.LogDetail.ALL;
import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.*;
import static specs.BaseSpec.baseRequestSpec;

public class ClubsSpec {

    public static RequestSpecification clubsRequestSpec = baseRequestSpec;

    public static ResponseSpecification successfulClubsListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/clubs_list_response_schema.json"))
            .expectBody("count", notNullValue())
            .expectBody("count", greaterThanOrEqualTo(0))
            .expectBody("results", notNullValue())
            .build();

    public static ResponseSpecification successfulCreateClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/successful_create_club_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("bookTitle", notNullValue())
            .expectBody("bookAuthors", notNullValue())
            .expectBody("publicationYear", notNullValue())
            .expectBody("description", notNullValue())
            .expectBody("telegramChatLink", notNullValue())
            .build();

    public static ResponseSpecification unauthorizedCreateClubResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath(
                    "schemas/clubs/unauthorized_create_club_response_schema.json"))
            .expectBody("detail", equalTo("Authentication credentials were not provided."))
            .build();

    public static ResponseSpecification successfulGetClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/successful_get_club_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user", notNullValue())
            .expectBody("review", notNullValue())
            .build();

    public static ResponseSpecification successfulGetClubReviewListResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/successful_get_club_review_list_response_schema.json"))
            .expectBody("results.id", everyItem(notNullValue()))
            .expectBody("results.club", everyItem(notNullValue()))
            .expectBody("results.user", everyItem(notNullValue()))
            .expectBody("results.review", everyItem(notNullValue()))
            .build();

    public static ResponseSpecification unauthorizedGetClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/unauthorized_get_club_review_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification successfulCreateClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(201)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/successful_create_club_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("user", notNullValue())
            .expectBody("review", notNullValue())
            .build();

    public static ResponseSpecification successfulPutClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(200)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/successful_get_club_review_response_schema.json"))
            .expectBody("id", notNullValue())
            .expectBody("club", notNullValue())
            .expectBody("review", notNullValue())
            .build();

    public static ResponseSpecification successfulDeleteClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(204)
            .build();

    public static ResponseSpecification unauthorizedPutClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/unauthorized_create_club_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();

    public static ResponseSpecification unauthorizedDeleteClubReviewResponseSpec = new ResponseSpecBuilder()
            .log(ALL)
            .expectStatusCode(401)
            .expectBody(matchesJsonSchemaInClasspath("schemas/clubs/unauthorized_create_club_response_schema.json"))
            .expectBody("detail", notNullValue())
            .build();
}
