package com.example.francosperfumemobile.backend.dtos.deliverydtos;

public class ReceiveDeliveryItemDTO {
    private int RequestItemId;
    private int ReceivedQty;
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