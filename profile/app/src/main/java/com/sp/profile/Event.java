package com.sp.profile;

public class Event {
    private String eventImageUrl;

    public Event() {
        // Required empty constructor for Firestore
    }

    public Event(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventImageUrl() {
        return eventImageUrl;
    }
}
