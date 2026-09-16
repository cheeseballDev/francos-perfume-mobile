package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class CreateRequestItemDTO {
    private int ProductId;
    private int RequestedQty;

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
}