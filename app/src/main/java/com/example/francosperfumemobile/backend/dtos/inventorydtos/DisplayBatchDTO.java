package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import android.os.Parcelable;

import java.io.Serializable;

public abstract class DisplayBatchDTO implements Parcelable {
    private int BatchId;
    private String BatchDisplayId;
    private int BatchItemId;
    private int ProductId;
    private String ProductName;
    private int BranchId;
    private String BranchLocation;
    private int Quantity;
    private String CreatedAt;
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