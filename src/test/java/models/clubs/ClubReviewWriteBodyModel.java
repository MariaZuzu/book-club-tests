package models.clubs;

/**
 * Тело POST /clubs/reviews/ и PUT /clubs/reviews/{id}/ (поле club обязательно для PUT).
 */
public record ClubReviewWriteBodyModel(
        int club,
        String review,
        int assessment,
        int readPages
) {
}