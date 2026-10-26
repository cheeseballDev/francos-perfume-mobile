package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

public class CreateRequestItemDTO {
    @SerializedName("productId")
    private int ProductId;
    @SerializedName("requestedQty")
    private int RequestedQty;

    public CreateRequestItemDTO() {
    }

    public CreateRequestItemDTO(int productId, int requestedQty) {
        this.ProductId = productId;
        this.RequestedQty = requestedQty;
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
}
