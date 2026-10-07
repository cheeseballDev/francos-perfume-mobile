package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

public class DisplayRequestDTO {
    @SerializedName("requestId")
    private int RequestId;

    @SerializedName("requestDisplayId")
    private String RequestDisplayId;
    @SerializedName("deliveryDisplayId")
    private String DeliveryDisplayId;

    @SerializedName("fromBranchId")
    private int FromBranchId;

    @SerializedName("toBranchId")
    private int ToBranchId;

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

    @SerializedName("requestStage")
    private String RequestStage;
    @SerializedName("requestDirection")
    private String RequestDirection;


    @SerializedName("requestMessage")
    private String RequestMessage;

    @SerializedName("requestComment")
    private String RequestComment;

    @SerializedName("requestDateSubmitted")
    private String RequestDateSubmitted;

    @SerializedName("itemCount")
    private int ItemCount;


    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public String getRequestDisplayId() {
        return RequestDisplayId;
    }

    public void setRequestDisplayId(String requestDisplayId) {
        RequestDisplayId = requestDisplayId;
    }
    public String getDeliveryDisplayId() {
        return DeliveryDisplayId;
    }

    public void setDeliveryDisplayId(String deliveryDisplayId) {
        DeliveryDisplayId = deliveryDisplayId;
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

    public String getRequestStage() {
        return RequestStage;
    }

    public void setRequestStage(String requestStage) {
        RequestStage = requestStage;
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

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }
}