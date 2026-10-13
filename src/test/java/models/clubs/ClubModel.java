package models.clubs;

import java.util.List;

/**
 * Модель клуба в ответе GET /clubs/.
 */
public record ClubModel (
        Integer id,
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink,
        Integer owner,
        List<Integer> members,
        List<ClubReviewModel> reviews,
        String created,
        String modified
) {}
