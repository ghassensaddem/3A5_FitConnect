package com.esprit.models;

public class EventP {
    private String summary;
    private String start;
    private String end;

    public EventP(String summary, String start, String end) {
        this.summary = summary;
        this.start = start;
        this.end = end;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end;
    }
}
