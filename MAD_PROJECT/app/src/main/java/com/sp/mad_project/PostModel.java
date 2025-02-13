package com.sp.mad_project;

public class PostModel {
    private String imageUrl;

    // ✅ No-argument constructor required by Firebase
    public PostModel() {}

    public PostModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

}
