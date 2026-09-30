package com.example.francosperfumemobile.backend.dtos.requestdtos;

import com.google.gson.annotations.SerializedName;

public class RequestProductFilterDTO {
    @SerializedName("productId")
    private int ProductId;
    @SerializedName("productName")
    private String ProductName;
    @SerializedName("productQty")
    private double ProductQty; // using double to match decimal? In C# it's decimal. We'll use double for simplicity; could use BigDecimal but double is fine.

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

    public double getProductQty() {
        return ProductQty;
    }

    public void setProductQty(double productQty) {
        ProductQty = productQty;
    }
}