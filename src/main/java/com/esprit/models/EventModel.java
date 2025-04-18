package com.esprit.models;

public class EventModel {
    private final String title;
    private final String startTime;

    public EventModel(String title, String startTime) {
        this.title = title;
        this.startTime = startTime;
    }

    public String getTitle() { return title; }
    public String getStartTime() { return startTime; }
}
