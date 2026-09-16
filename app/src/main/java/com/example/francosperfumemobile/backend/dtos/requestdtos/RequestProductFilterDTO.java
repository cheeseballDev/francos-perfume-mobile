package com.example.francosperfumemobile.backend.dtos.requestdtos;

public class RequestProductFilterDTO {
    private int ProductId;
    private String ProductName;
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