package com.sp.mad_project;

public class EventModel {
    private String imageUrl;

    public EventModel() {
        // Empty constructor needed for Firestore deserialization
    }

    public EventModel(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
