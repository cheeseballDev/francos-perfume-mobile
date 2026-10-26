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
    @SerializedName("requestItemId")
    private String RequestItemId;

    // Filled in on the phone while receiving; transient = never sent or read by Gson
    private transient Integer receivedQty;
    private transient String receiveRemark;
    private transient boolean receivedChecked = true;

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
    public String getRequestItemId() {
        return RequestItemId;
    }

    public void setRequestItemId(String requestItemId) {
        RequestItemId = requestItemId;
    }

    /** Received amount typed by the user; starts at the requested quantity. */
    public int getReceivedQty() {
        return receivedQty != null ? receivedQty : Quantity;
    }

    public void setReceivedQty(int receivedQty) {
        this.receivedQty = receivedQty;
    }

    public String getReceiveRemark() {
        return receiveRemark;
    }

    public void setReceiveRemark(String receiveRemark) {
        this.receiveRemark = receiveRemark;
    }

    public boolean isReceivedChecked() {
        return receivedChecked;
    }

    public void setReceivedChecked(boolean receivedChecked) {
        this.receivedChecked = receivedChecked;
    }

    /** requestItemId as a number, or null when the backend didn't send one. */
    public Integer getRequestItemIdAsInt() {
        if (RequestItemId == null) return null;
        try {
            return Integer.parseInt(RequestItemId.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
