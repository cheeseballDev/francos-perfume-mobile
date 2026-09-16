package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;

public class RequestItems {

    @SerializedName("requestItemId")
    private int RequestItemId;

    @SerializedName("requestId")
    private int RequestId;

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("requestedQty")
    private int RequestedQty;

    @SerializedName("receivedQty")
    private int ReceivedQty;

    @SerializedName("itemStatus")
    private String ItemStatus;

    @SerializedName("remarks")
    private String Remarks;

    public RequestItems() {
    }

    public int getRequestItemId() {
        return RequestItemId;
    }

    public void setRequestItemId(int requestItemId) {
        RequestItemId = requestItemId;
    }

    public int getRequestId() {
        return RequestId;
    }

    public void setRequestId(int requestId) {
        RequestId = requestId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getRequestedQty() {
        return RequestedQty;
    }

    public void setRequestedQty(int requestedQty) {
        RequestedQty = requestedQty;
    }

    public int getReceivedQty() {
        return ReceivedQty;
    }

    public void setReceivedQty(int receivedQty) {
        ReceivedQty = receivedQty;
    }

    public String getItemStatus() {
        return ItemStatus;
    }

    public void setItemStatus(String itemStatus) {
        ItemStatus = itemStatus;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}