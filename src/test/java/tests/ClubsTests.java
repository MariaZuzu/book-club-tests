package tests;

import models.clubs.ClubModel;
import models.clubs.ClubsListResponseModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class ClubsTests extends TestBase {

//    @Test
//    public void getClubsReturns200AndValidStructure() {
//        ClubsListResponseModel response = api.clubs.getClubs();
//
//        assertThat(response).isNotNull();
//        assertThat(response.count()).isGreaterThanOrEqualTo(0);
//        assertThat(response.results()).isNotNull();
//        assertThat(response.results()).hasSize(response.count());
//    }

//    @Test
//    public void getClubsCountMatchesResultsSize() {
//        ClubsListResponseModel response = api.clubs.getClubs();
//
//        assertThat(response.results())
//                .as("count должно совпадать с размером results")
//                .hasSize(response.count());
//    }

//    @Test
//    public void getClubsEachClubHasRequiredFields() {
//        ClubsListResponseModel response = api.clubs.getClubs();
//
//        for (ClubModel club : response.results()) {
//            assertThat(club.id()).isNotNull().isPositive();
//            assertThat(club.bookTitle()).isNotNull();
//            assertThat(club.bookAuthors()).isNotNull();
//            assertThat(club.publicationYear()).isNotNull();
//            assertThat(club.description()).isNotNull();
//            assertThat(club.telegramChatLink()).isNotNull();
//            assertThat(club.owner()).isNotNull().isPositive();
//            assertThat(club.members()).isNotNull();
//            assertThat(club.reviews()).isNotNull();
//            assertThat(club.created()).isNotNull();
//        }
//    }

//    @Test
//    public void getClubsPaginationFieldsPresent() {
//        ClubsListResponseModel response = api.clubs.getClubs();
//
//        assertThat(response.count()).isNotNull();
//        // next и previous могут быть null при одной странице
//        assertThat(response.results()).isNotNull();
//    }
}
