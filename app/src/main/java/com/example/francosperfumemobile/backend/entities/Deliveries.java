package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class Deliveries {

    @SerializedName("deliveryId")
    private int DeliveryId;

    @SerializedName("deliveryDisplayId")
    private String DeliveryDisplayId;

    @SerializedName("requestId")
    private int RequestId;

    @SerializedName("deliveryStatus")
    private String DeliveryStatus;

    @SerializedName("deliveryDateCreated")
    private Date DeliveryDateCreated;

    @SerializedName("fromBranchId")
    private int FromBranchId;

    @SerializedName("toBranchId")
    private int ToBranchId;

    @SerializedName("fromBranchName")
    private String FromBranchName;

    @SerializedName("toBranchName")
    private String ToBranchName;

    public Deliveries() {
    }

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
    }

    public String getDeliveryDisplayId() {
        return DeliveryDisplayId;
    }

    public void setDeliveryDisplayId(String deliveryDisplayId) {
        DeliveryDisplayId = deliveryDisplayId;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public String getDeliveryStatus() {
        return DeliveryStatus;
    }

    public void setDeliveryStatus(String deliveryStatus) {
        DeliveryStatus = deliveryStatus;
    }

    public Date getDeliveryDateCreated() {
        return DeliveryDateCreated;
    }

    public void setDeliveryDateCreated(Date deliveryDateCreated) {
        DeliveryDateCreated = deliveryDateCreated;
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

    public String getFromBranchName() {
        return FromBranchName;
    }

    public void setFromBranchName(String fromBranchName) {
        FromBranchName = fromBranchName;
    }

    public String getToBranchName() {
        return ToBranchName;
    }

    public void setToBranchName(String toBranchName) {
        ToBranchName = toBranchName;
    }
}