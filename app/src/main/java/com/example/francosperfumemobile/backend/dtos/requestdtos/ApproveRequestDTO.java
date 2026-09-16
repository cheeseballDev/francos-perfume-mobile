package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class ApproveRequestDTO {
    private String Remarks;
    private int ProductId;
    private int RequestedItemId;
    private boolean IsApproved;
    private Integer RequestedItemQty;

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getRequestedItemId() {
        return RequestedItemId;
    }

    public void setRequestedItemId(int requestedItemId) {
        RequestedItemId = requestedItemId;
    }

    public boolean isIsApproved() {
        return IsApproved;
    }

    public void setIsApproved(boolean isApproved) {
        IsApproved = isApproved;
    }

    public Integer getRequestedItemQty() {
        return RequestedItemQty;
    }

    public void setRequestedItemQty(Integer requestedItemQty) {
        RequestedItemQty = requestedItemQty;
    }
}