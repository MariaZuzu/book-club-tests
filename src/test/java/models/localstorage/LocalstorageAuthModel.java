package models.localstorage;

import com.fasterxml.jackson.annotation.JsonProperty;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.json.JsonMapper;

public record LocalstorageAuthModel(BookClubAuth book_club_auth) { //book_club_auth взято из схемы в console

    public record BookClubAuth(
            @JsonProperty("user") User user,
            @JsonProperty("accessToken") String accessToken,
            @JsonProperty("refreshToken") String refreshToken,
            @JsonProperty("isAuthenticated") boolean isAuthenticated   // Указываем точное имя из JSON из console
    ) {
        private static final JsonMapper JSON = JsonMapper.builder().build();

        /// Записываем строку как Json
        public String toJson() {
            try {
                return JSON.writeValueAsString(this);
            } catch (JacksonException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public record User(
            Integer id,
            String username,
            String firstName,
            String lastName,
            String email,
            String remoteAddr
    ) {
    }

}