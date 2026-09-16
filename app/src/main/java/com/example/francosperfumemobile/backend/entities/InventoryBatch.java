package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;
import java.util.Date;

public class InventoryBatch {

    @SerializedName("batchId")
    private int BatchId;

    @SerializedName("deliveryId")
    private int DeliveryId;

    @SerializedName("branchId")
    private int BranchId;

    @SerializedName("expiryDate")
    private String ExpiryDate;

    @SerializedName("createdAt")
    private Date CreatedAt;

    @SerializedName("batchDisplayId")
    private String BatchDisplayId;

    public InventoryBatch() {
    }

    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int batchId) {
        BatchId = batchId;
    }

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
    }

    public int getBranchId() {
        return BranchId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }

    public Date getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(Date createdAt) {
        CreatedAt = createdAt;
    }

    public String getBatchDisplayId() {
        return BatchDisplayId;
    }

    public void setBatchDisplayId(String batchDisplayId) {
        BatchDisplayId = batchDisplayId;
    }
}