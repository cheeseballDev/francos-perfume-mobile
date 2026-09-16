package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;

public class DeliveryItems {

    @SerializedName("deliveryItemId")
    private int DeliveryItemId;

    @SerializedName("deliveryId")
    private int DeliveryId;

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("productName")
    private String ProductName;

    @SerializedName("quantity")
    private int Quantity;

    @SerializedName("status")
    private String Status;

    @SerializedName("batchDisplayId")
    private String BatchDisplayId;

    public DeliveryItems() {
    }

    public int getDeliveryItemId() {
        return DeliveryItemId;
    }

    public void setDeliveryItemId(int deliveryItemId) {
        DeliveryItemId = deliveryItemId;
    }

    public int getDeliveryId() {
        return DeliveryId;
    }

    public void setDeliveryId(int deliveryId) {
        DeliveryId = deliveryId;
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

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getBatchDisplayId() {
        return BatchDisplayId;
    }

    public void setBatchDisplayId(String batchDisplayId) {
        BatchDisplayId = batchDisplayId;
    }
}