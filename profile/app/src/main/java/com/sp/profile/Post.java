package com.sp.profile;

public class Post {
    private int imageResId; // This stores the image resource ID

    public Post(int imageResId) {
        this.imageResId = imageResId;
    }

    public int getImageResId() {
        return imageResId;
    }

    public void setImageResId(int imageResId) {
        this.imageResId = imageResId;
    }
}
