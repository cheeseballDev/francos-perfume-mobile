package com.example.francosperfumemobile.backend.dtos.deliverydtos;

import com.google.gson.annotations.SerializedName;

public class ReceiveDeliveryItemDTO {
    @SerializedName("requestItemId")
    private int RequestItemId;
    @SerializedName("receivedQty")
    private int ReceivedQty;
    @SerializedName("remarks")
    private String Remarks;

    public int getRequestItemId() {
        return RequestItemId;
    }

    public void setRequestItemId(int requestItemId) {
        RequestItemId = requestItemId;
    }

    public int getReceivedQty() {
        return ReceivedQty;
    }

    public void setReceivedQty(int receivedQty) {
        ReceivedQty = receivedQty;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }
}