package api;

public class ApiClient {

    private final RegistrationApiClient registrationApiClient = new RegistrationApiClient();
    private final LoginApiClient loginApiClient = new LoginApiClient();
    private final LogoutApiClient logoutApiClient = new LogoutApiClient();
    private final UpdateUserApiClient updateUserApiClient = new UpdateUserApiClient();
    public final ClubsApiClient clubs = new ClubsApiClient();
    public final AuthApiClient auth = new AuthApiClient();
    public final UsersApiClient users = new UsersApiClient();

    public RegistrationApiClient registration() {
        return registrationApiClient;
    }

    public LoginApiClient login() {
        return loginApiClient;
    }

    public LogoutApiClient logout() {
        return logoutApiClient;
    }

    public UpdateUserApiClient updateUser(){
        return updateUserApiClient;
    }

}