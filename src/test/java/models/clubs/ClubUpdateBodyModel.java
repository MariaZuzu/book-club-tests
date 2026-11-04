package models.clubs;

public record ClubUpdateBodyModel(
        String bookTitle,
        String bookAuthors,
        int publicationYear,
        String description,
        String telegramChatLink
) {
}