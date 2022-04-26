package training.GithoApp.Utilities;

public class Api {
    static final String GITHUB_API_USER_BASE_URL = "https://api.github.com/users/";

    public static String getGithubUserUrl(String username){
        return GITHUB_API_USER_BASE_URL + username;
    }
}
