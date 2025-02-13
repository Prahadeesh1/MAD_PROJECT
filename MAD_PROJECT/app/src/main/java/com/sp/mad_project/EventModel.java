package com.sp.mad_project;

public class EventModel {
    private String eventImageUrl;

    public EventModel() {
        // Required empty constructor for Firestore
    }

    public EventModel(String eventImageUrl) {
        this.eventImageUrl = eventImageUrl;
    }

    public String getEventImageUrl() {return eventImageUrl;}

    public void setEventImageUrl(String eventImageUrl){this.eventImageUrl = eventImageUrl;}
}
