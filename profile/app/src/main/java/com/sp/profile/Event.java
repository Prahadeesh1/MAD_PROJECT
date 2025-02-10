package com.sp.profile;

public class Event {
    private int imageResId; // This stores the image resource ID

    public Event(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
