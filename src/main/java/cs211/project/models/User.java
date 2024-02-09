package cs211.project.models;

import java.time.LocalDateTime;

public class User {
    private String userId;
    private String name;
    private String username;
    private String password;
    private String role;
    private String userImageResource;
    private LocalDateTime loginDateTime;
    private boolean bannedStatus;


    public User(String userId, String name, String username, String password, String role, String userImageResource, LocalDateTime loginDateTime, boolean bannedStatus) {
        this.userId = userId;
        this.name = name;
        this.username = username;
        this.password = password;
        this.role = role;
        this.userImageResource = userImageResource;
        setLoginDateTime(loginDateTime);
        setBannedStatus(bannedStatus);
    }

    public User(String userId, String name, String username, String password, String userImageResource, LocalDateTime loginDateTime) {
        this(userId, name, username, password, "common", userImageResource, loginDateTime, false);
        setPassword(password);
    }

    public String getUserId() {
        return userId;
    }

    public String getName() {
        return name;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRole() {
        return role;
    }

    public String getUserImageResource() {
        return userImageResource;
    }

    public LocalDateTime getLoginDateTime() {
        return loginDateTime;
    }

    public boolean isBannedStatus() {
        return bannedStatus;
    }


    public void setBannedStatus(boolean banned) {
        this.bannedStatus = banned;
    }

    public void setPassword(String password) {
        this.password = password;
    }


    public void setLoginDateTime(LocalDateTime loginDateTime) {
        this.loginDateTime = loginDateTime;
    }

    public void setUserImageResource(String userImageResource) {
        this.userImageResource = userImageResource;
    }

    public boolean validatePassword(String password) {
        return this.password.equals(password);
    }
    public boolean isValidUsername(String username){
        return this.username.equals(username);
    }
    public boolean isValidId(String id){
        return this.userId.equals(id) ;
    }

}