package models.clubs;

import java.util.List;

/**
 * Ответ GET /clubs/ — пагинированный список клубов.
 */
public record ClubsListResponseModel (
        Integer count,
        String next,
        String previous,
        List<ClubModel> results
) {}
