package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public abstract class DisplayBatchDTO implements Parcelable {
    @SerializedName("batchId")
    private int BatchId;

    @SerializedName("batchDisplayId")
    private String BatchDisplayId;

    @SerializedName("batchItemId")
    private int BatchItemId;

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("productName")
    private String ProductName;

    @SerializedName("branchId")
    private int BranchId;

    @SerializedName("branchLocation")
    private String BranchLocation;

    @SerializedName("quantity")
    private int Quantity;

    @SerializedName("createdAt")
    private String CreatedAt;

    @SerializedName("expiryDate")
    private String ExpiryDate;


    public int getBatchId() {
        return BatchId;
    }

    public void setBatchId(int batchId) {
        BatchId = batchId;
    }

    public String getBatchDisplayId() {
        return BatchDisplayId;
    }

    public void setBatchDisplayId(String batchDisplayId) {
        BatchDisplayId = batchDisplayId;
    }

    public int getBatchItemId() {
        return BatchItemId;
    }

    public void setBatchItemId(int batchItemId) {
        BatchItemId = batchItemId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getBranchId() {
        return BranchId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }

    public String getBranchLocation() {
        return BranchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        BranchLocation = branchLocation;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }

    public String getExpiryDate() {
        return ExpiryDate;
    }

    public void setExpiryDate(String expiryDate) {
        ExpiryDate = expiryDate;
    }
}