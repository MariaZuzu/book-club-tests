package models.clubs;

public record ClubCreateBodyModel(
        String bookTitle,
        String bookAuthors,
        Integer publicationYear,
        String description,
        String telegramChatLink
) {
}