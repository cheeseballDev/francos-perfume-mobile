package com.example.francosperfumemobile.model;

public class TimelineItem {
    private String title;
    private String subtitle;
    private String status;
    private boolean isSkipped;

    public TimelineItem(String title, String subtitle, String status) {
        this.title = title;
        this.subtitle = subtitle;
        this.status = status;
        this.isSkipped = false;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getStatus() { return status; }
    public boolean isSkipped() { return isSkipped; }
    public void setSkipped(boolean skipped) { isSkipped = skipped; }
}