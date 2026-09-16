package com.example.francosperfumemobile.backend.dtos.requestdtos;

import java.util.List;

public class DisplayRequestDetailsDTO {
    private int RequestId;
    private int FromBranchId;
    private int ToBranchId;
    private String RequestDisplayId;
    private String RequestedFrom;
    private String DeliveredTo;
    private String EmployeeDisplayId;
    private String RequestStatus;
    private String RequestMessage;
    private String RequestComment;
    private java.util.Date RequestDateSubmitted;
    private List<DisplayRequestItemDetailsDTO> Items;
    private List<DisplayRequestApprovalDetailsDTO> Approvals;

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public int getFromBranchId() {
        return FromBranchId;
    }

    public void setFromBranchId(int fromBranchId) {
        FromBranchId = fromBranchId;
    }

    public int getToBranchId() {
        return ToBranchId;
    }

    public void setToBranchId(int toBranchId) {
        ToBranchId = toBranchId;
    }

    public String getRequestDisplayId() {
        return RequestDisplayId;
    }

    public void setRequestDisplayId(String requestDisplayId) {
        RequestDisplayId = requestDisplayId;
    }

    public String getRequestedFrom() {
        return RequestedFrom;
    }

    public void setRequestedFrom(String requestedFrom) {
        RequestedFrom = requestedFrom;
    }

    public String getDeliveredTo() {
        return DeliveredTo;
    }

    public void setDeliveredTo(String deliveredTo) {
        DeliveredTo = deliveredTo;
    }

    public String getEmployeeDisplayId() {
        return EmployeeDisplayId;
    }

    public void setEmployeeDisplayId(String employeeDisplayId) {
        EmployeeDisplayId = employeeDisplayId;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }

    public String getRequestMessage() {
        return RequestMessage;
    }

    public void setRequestMessage(String requestMessage) {
        RequestMessage = requestMessage;
    }

    public String getRequestComment() {
        return RequestComment;
    }

    public void setRequestComment(String requestComment) {
        RequestComment = requestComment;
    }

    public java.util.Date getRequestDateSubmitted() {
        return RequestDateSubmitted;
    }

    public void setRequestDateSubmitted(java.util.Date requestDateSubmitted) {
        RequestDateSubmitted = requestDateSubmitted;
    }

    public List<DisplayRequestItemDetailsDTO> getItems() {
        return Items;
    }

    public void setItems(List<DisplayRequestItemDetailsDTO> items) {
        Items = items;
    }

    public List<DisplayRequestApprovalDetailsDTO> getApprovals() {
        return Approvals;
    }

    public void setApprovals(List<DisplayRequestApprovalDetailsDTO> approvals) {
        Approvals = approvals;
    }
}