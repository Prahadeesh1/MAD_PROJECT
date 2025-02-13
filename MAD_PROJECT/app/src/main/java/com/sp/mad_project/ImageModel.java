package com.sp.mad_project;

public class ImageModel {
    private String imageUrl;

    public ImageModel() {
        // Empty constructor needed for Firestore deserialization
    }

    public ImageModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
