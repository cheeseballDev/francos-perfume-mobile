package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DisplayRequestDetailsDTO {
    @SerializedName("requestId")
    private int RequestId;
    @SerializedName("fromBranchId")
    private int FromBranchId;
    @SerializedName("toBranchId")
    private int ToBranchId;
    @SerializedName("requestDisplayId")
    private String RequestDisplayId;
    @SerializedName("requestedFrom")
    private String RequestedFrom;
    @SerializedName("deliveredTo")
    private String DeliveredTo;
    @SerializedName("employeeDisplayId")
    private String EmployeeDisplayId;
    @SerializedName("employeeName")
    private String EmployeeName;
    @SerializedName("requestStatus")
    private String RequestStatus;
    @SerializedName("requestDirection")
    private String RequestDirection;
    @SerializedName("requestMessage")
    private String RequestMessage;
    @SerializedName("requestComment")
    private String RequestComment;
    @SerializedName("requestDateSubmitted")
    private String RequestDateSubmitted;
    @SerializedName("items")
    private List<DisplayRequestItemDetailsDTO> Items;
    @SerializedName("approvals")
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
    public String getEmployeeName() {
        return EmployeeName;
    }

    public void setEmployeeName(String employeeName) {
        EmployeeName = employeeName;
    }

    public String getRequestStatus() {
        return RequestStatus;
    }

    public void setRequestStatus(String requestStatus) {
        RequestStatus = requestStatus;
    }
    public String getRequestDirection() {
        return RequestDirection;
    }

    public void setRequestDirection(String requestDirection) {
        RequestDirection = requestDirection;
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

    public String getRequestDateSubmitted() {
        return RequestDateSubmitted;
    }

    public void setRequestDateSubmitted(String requestDateSubmitted) {
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