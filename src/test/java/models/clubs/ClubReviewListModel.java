package models.clubs;

import java.util.List;

/**
 * Список отзывов на клуб
 */
public record ClubReviewListModel(
        Integer count,
        String next,
        String previous,
        List<ClubModel> results
) {}
