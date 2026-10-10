package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

public class DisplayRequestItemDetailsDTO {
    @SerializedName("requestItemId")
    private int RequestItemId;
    @SerializedName("productId")
    private int ProductId;
    @SerializedName("productDisplayId")
    private String ProductDisplayId;
    @SerializedName("productName")
    private String ProductName;
    @SerializedName("requestedQty")
    private int RequestedQty;
    @SerializedName("receivedQty")
    private int ReceivedQty;
    @SerializedName("itemStatus")
    private String ItemStatus;
    @SerializedName("remarks")
    private String Remarks;

    public int getRequestItemId() {
        return RequestItemId;
    }

    public void setRequestItemId(int requestItemId) {
        RequestItemId = requestItemId;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getProductDisplayId() {
        return ProductDisplayId;
    }

    public void setProductDisplayId(String productDisplayId) {
        ProductDisplayId = productDisplayId;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
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