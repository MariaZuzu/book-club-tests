package testdata;

import models.clubs.ClubCreateBodyModel;
import models.clubs.ClubPatchBodyModel;
import models.clubs.ClubReviewWriteBodyModel;
import models.clubs.ClubUpdateBodyModel;
import net.datafaker.Faker;

public class ClubsTestData {

    public static final String LOGIN_USERNAME = "wilkinso";
    public static final String LOGIN_PASSWORD = "12345";


    public static final Faker FAKER = new Faker();

    public static ClubCreateBodyModel randomClubCreateBody() {
        return new ClubCreateBodyModel(
                "QA Club " + FAKER.number().digits(4),
                FAKER.book().author(),
                2020 + FAKER.number().numberBetween(0, 6),
                FAKER.lorem().sentence(),
                "https://t.me/qa_guru_bookclub"
        );
    }

    public static ClubUpdateBodyModel randomClubPutBody() {
        return new ClubUpdateBodyModel(
                "PUT Club " + FAKER.number().digits(4),
                FAKER.book().author(),
                2018 + FAKER.number().numberBetween(0, 8),
                FAKER.lorem().sentence(),
                "https://t.me/qa_guru_bookclub"
        );
    }

    public static ClubPatchBodyModel randomClubPatchBody() {
        return new ClubPatchBodyModel(
                "PATCH Club " + FAKER.number().digits(4),
                FAKER.lorem().sentence()
        );
    }

    public static ClubReviewWriteBodyModel randomClubReviewBody(int clubId) {
        return new ClubReviewWriteBodyModel(
                clubId,
                FAKER.lorem().paragraph(2),
                FAKER.number().numberBetween(1, 5),
                FAKER.number().numberBetween(1, 500)
        );
    }
}
