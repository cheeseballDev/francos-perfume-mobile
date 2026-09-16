package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class DisplayRequestItemDetailsDTO {
    private int RequestItemId;
    private int ProductId;
    private String ProductDisplayId;
    private String ProductName;
    private int RequestedQty;
    private int ReceivedQty;
    private String ItemStatus;
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