package training.GithoApp.models;

public class GithubUser {
    private String name;
    private String biography;
    private double followersCount;
    private double followingCount;
    private String avatarUrl;
    private String location;

    public String getName() {
        return name;
    }

    public String getBiography() {
        return biography;
    }

    public double getFollowersCount() {
        return followersCount;
    }

    public double getFollowingCount() {
        return followingCount;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getLocation() {
        return location;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public void setFollowersCount(double followersCount) {
        this.followersCount = followersCount;
    }

    public void setFollowingCount(double followingCount) {
        this.followingCount = followingCount;
    }

    public void setAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
