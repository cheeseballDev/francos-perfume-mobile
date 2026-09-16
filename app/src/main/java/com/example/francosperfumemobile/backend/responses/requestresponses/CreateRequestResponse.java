package com.example.francosperfumemobile.backend.responses.requestresponses;

public class CreateRequestResponse {
    private int requestId;
    private String requestDisplayId;
    private String requestStatus;

    public int getRequestId() { return requestId; }
    public void setRequestId(int requestId) { this.requestId = requestId; }
    public String getRequestDisplayId() { return requestDisplayId; }
    public void setRequestDisplayId(String requestDisplayId) { this.requestDisplayId = requestDisplayId; }
    public String getRequestStatus() { return requestStatus; }
    public void setRequestStatus(String requestStatus) { this.requestStatus = requestStatus; }
}