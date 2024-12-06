

package com.example.expertmaintenance.Models;

public class Intervention {
    private String id;
    private String title;
    private String clientName;
    private String siteAddress;
    private String siteCity;
    private String startTime;
    private String endTime;
    private boolean isCompleted;

    public Intervention(String id, String title, String clientName, String siteAddress, String siteCity, String startTime, String endTime) {
        this.id = id;
        this.title = title;
        this.clientName = clientName;
        this.siteAddress = siteAddress;
        this.siteCity = siteCity;
        this.startTime = startTime;
        this.endTime = endTime;
        this.isCompleted = false; // default state
    }

    // Getters and Setters for each field
    public String getId() { return id; }
    public String getTitle() { return title; }
    public String getClientName() { return clientName; }
    public String getSiteAddress() { return siteAddress; }
    public String getSiteCity() { return siteCity; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }

    public boolean isCompleted() { return isCompleted; }
    public void setCompleted(boolean completed) { isCompleted = completed; }
}
