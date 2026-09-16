package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Requests {

    @SerializedName("requestId")
    private int RequestId;

    @SerializedName("requestDisplayId")
    private String RequestDisplayId;

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

    @SerializedName("requestStatus")
    private String RequestStatus;

    @SerializedName("requestStage")
    private String RequestStage;

    @SerializedName("requestMessage")
    private String RequestMessage;

    @SerializedName("requestComment")
    private String RequestComment;

    @SerializedName("requestDateSubmitted")
    private Date RequestDateSubmitted;

    @SerializedName("itemCount")
    private int ItemCount;

    public Requests() {
    }

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

    public Date getRequestDateSubmitted() {
        return RequestDateSubmitted;
    }

    public void setRequestDateSubmitted(Date requestDateSubmitted) {
        RequestDateSubmitted = requestDateSubmitted;
    }

    public int getItemCount() {
        return ItemCount;
    }

    public void setItemCount(int itemCount) {
        ItemCount = itemCount;
    }
}