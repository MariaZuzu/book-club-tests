package models.clubs;

/**
 * Тело запроса для создания клуба POST /clubs/.
 */
public record CreateClubBodyModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink
) {}
