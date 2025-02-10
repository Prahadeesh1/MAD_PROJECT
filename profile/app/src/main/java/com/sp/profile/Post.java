package com.sp.profile;

public class Post {
    private String imageUrl;

    // ✅ No-argument constructor required by Firebase
    public Post() {}

    public Post(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

