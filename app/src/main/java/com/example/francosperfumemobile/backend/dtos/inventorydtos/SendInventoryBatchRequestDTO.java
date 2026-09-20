package com.example.francosperfumemobile.backend.dtos.inventorydtos;

public class SendInventoryBatchRequestDTO {
    private int ProductId;
    private int BranchId;

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public int getBranchId() {
        return BranchId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }
}