package com.sp.mad_project;

public class Users {
    private String userId, username, email, profilepictureUrl;

    public Users() {
        // Empty constructor required for Firestore
    }

    public Users(String userId,String username, String email, String profilepictureUrl) {
        this.userId = userId;
        this.username = username;
        this.email = email;
        this.profilepictureUrl = profilepictureUrl;
    }
    public String getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getEmail() { return email; }
    public String getProfilepictureUrl() { return profilepictureUrl; }
}
