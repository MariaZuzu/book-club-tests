package models.localstorage;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;
import tools.jackson.core.JacksonException;

public record LocalStorageAuthRequestBody (
        @JsonProperty("user") UserData user,
        @JsonProperty("accessToken") String access,
        @JsonProperty("refreshToken") String refresh,
        @JsonProperty("isAuthenticated") boolean authenticated
) {
    private static final JsonMapper JSON = JsonMapper.builder().build();

    public String toJson() {
        try {
            return JSON.writeValueAsString(this);
        } catch (JacksonException | JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
