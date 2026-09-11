package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import java.util.List;

public class DisplayInventoryDTO {
    private int ProductId;
    private String ProductDisplayId;
    private String ProductName;
    private String ProductType;
    private String ProductGender;
    private String ProductBarcode;
    private String ProductStatus;
    private double ProductPrice;
    private String ProductImageUrl;
    private String ProductDateCreated;
    private Integer BranchId;
    private String BranchDisplayId;
    private String BranchName;
    private int ProductQuantity;
    private int ProductBatchCount;
    private String StockWarning;
    private List<DisplayBatchDTO> Batches;

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

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getProductGender() {
        return ProductGender;
    }

    public void setProductGender(String productGender) {
        ProductGender = productGender;
    }

    public String getProductBarcode() {
        return ProductBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        ProductBarcode = productBarcode;
    }

    public String getProductStatus() {
        return ProductStatus;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public String getProductDateCreated() {
        return ProductDateCreated;
    }

    public void setProductDateCreated(String productDateCreated) {
        ProductDateCreated = productDateCreated;
    }

    public Integer getBranchId() {
        return BranchId;
    }

    public void setBranchId(Integer branchId) {
        BranchId = branchId;
    }

    public String getBranchDisplayId() {
        return BranchDisplayId;
    }

    public void setBranchDisplayId(String branchDisplayId) {
        BranchDisplayId = branchDisplayId;
    }

    public String getBranchName() {
        return BranchName;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public int getProductBatchCount() {
        return ProductBatchCount;
    }

    public void setProductBatchCount(int productBatchCount) {
        ProductBatchCount = productBatchCount;
    }

    public String getStockWarning() {
        return StockWarning;
    }

    public void setStockWarning(String stockWarning) {
        StockWarning = stockWarning;
    }

    public List<DisplayBatchDTO> getBatches() {
        return Batches;
    }

    public void setBatches(List<DisplayBatchDTO> batches) {
        Batches = batches;
    }
}