package app.com.shalan.spacego.Models;

/**
 * Created by noura on 09/08/2017.
 */

public class User {
    private String username;
    private String email;
    private String password;
    private String userProfileImg;

    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }


    public User(String username, String email, String password, String userProfileImg) {
        this.username = username;
        this.email = email;

        this.password = password;
        this.userProfileImg = userProfileImg;
    }

    public User() {

    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserProfileImg() {
        return userProfileImg;
    }

    public void setUserProfileImg(String userProfileImg) {
        this.userProfileImg = userProfileImg;
    }
}
