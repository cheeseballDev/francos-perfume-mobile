package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import com.google.gson.annotations.SerializedName;

public class DeliveryItemDTO {
    @SerializedName("deliveryItemId")
    private int DeliveryItemId;
    @SerializedName("productId")
    private int ProductId;
    @SerializedName("productName")
    private String ProductName;
    @SerializedName("quantity")
    private int Quantity;
    @SerializedName("status")
    private String Status;

    public int getDeliveryItemId() {
        return DeliveryItemId;
    }

    public void setDeliveryItemId(int deliveryItemId) {
        DeliveryItemId = deliveryItemId;
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
}