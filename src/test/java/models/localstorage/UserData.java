package models.localstorage;

public record UserData (
        Integer id,
        String username,
        String firstName,
        String lastName,
        String email,
        String remoteAddr
) {}
