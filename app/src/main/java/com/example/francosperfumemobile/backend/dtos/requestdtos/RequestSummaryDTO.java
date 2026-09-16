package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class RequestSummaryDTO {
    private int TotalRequests;
    private int PendingRequests;
    private int ApprovedRequests;
    private int RejectedRequests;
    private int CompletedRequests;

    public int getTotalRequests() {
        return TotalRequests;
    }

    public void setTotalRequests(int totalRequests) {
        TotalRequests = totalRequests;
    }

    public int getPendingRequests() {
        return PendingRequests;
    }

    public void setPendingRequests(int pendingRequests) {
        PendingRequests = pendingRequests;
    }

    public int getApprovedRequests() {
        return ApprovedRequests;
    }

    public void setApprovedRequests(int approvedRequests) {
        ApprovedRequests = approvedRequests;
    }

    public int getRejectedRequests() {
        return RejectedRequests;
    }

    public void setRejectedRequests(int rejectedRequests) {
        RejectedRequests = rejectedRequests;
    }

    public int getCompletedRequests() {
        return CompletedRequests;
    }

    public void setCompletedRequests(int completedRequests) {
        CompletedRequests = completedRequests;
    }
}