package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;

public class Inventory {

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("branchId")
    private int BranchId;

    @SerializedName("branchDisplayId")
    private String BranchDisplayId;

    @SerializedName("branchName")
    private String BranchName;

    @SerializedName("productQuantity")
    private int ProductQuantity;

    @SerializedName("productBatchCount")
    private int ProductBatchCount;

    @SerializedName("productDisplayId")
    private String ProductDisplayId;

    @SerializedName("productName")
    private String ProductName;

    @SerializedName("productType")
    private String ProductType;

    @SerializedName("productGender")
    private String ProductGender;

    @SerializedName("productBarcode")
    private String ProductBarcode;

    @SerializedName("productStatus")
    private String ProductStatus;

    @SerializedName("productPrice")
    private double ProductPrice;

    @SerializedName("productImageUrl")
    private String ProductImageUrl;

    @SerializedName("productDateCreated")
    private String ProductDateCreated;

    public Inventory() {
    }

    public int getProductId() {
        return ProductId;
    }

    public int getBranchId() {
        return BranchId;
    }

    public String getBranchDisplayId() {
        return BranchDisplayId;
    }

    public String getBranchName() {
        return BranchName;
    }

    public int getProductQuantity() {
        return ProductQuantity;
    }

    public int getProductBatchCount() {
        return ProductBatchCount;
    }

    public String getProductDisplayId() {
        return ProductDisplayId;
    }

    public String getProductName() {
        return ProductName;
    }

    public String getProductType() { return ProductType; }

    public String getProductGender() {
        return ProductGender;
    }

    public String getProductBarcode() {
        return ProductBarcode;
    }

    public String getProductStatus() {
        return ProductStatus;
    }

    public double getProductPrice() {
        return ProductPrice;
    }

    public String getProductImageUrl() {
        return ProductImageUrl;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public void setBranchId(int branchId) {
        BranchId = branchId;
    }

    public void setBranchDisplayId(String branchDisplayId) {
        BranchDisplayId = branchDisplayId;
    }

    public void setBranchName(String branchName) {
        BranchName = branchName;
    }

    public void setProductQuantity(int productQuantity) {
        ProductQuantity = productQuantity;
    }

    public void setProductBatchCount(int productBatchCount) {
        ProductBatchCount = productBatchCount;
    }

    public void setProductDisplayId(String productDisplayId) {
        ProductDisplayId = productDisplayId;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public void setProductGender(String productGender) {
        ProductGender = productGender;
    }

    public void setProductBarcode(String productBarcode) {
        ProductBarcode = productBarcode;
    }

    public void setProductStatus(String productStatus) {
        ProductStatus = productStatus;
    }

    public void setProductPrice(double productPrice) {
        ProductPrice = productPrice;
    }

    public void setProductImageUrl(String productImageUrl) {
        ProductImageUrl = productImageUrl;
    }

    public void setProductDateCreated(String productDateCreated) {
        ProductDateCreated = productDateCreated;
    }

    public String getProductDateCreated() {
        return ProductDateCreated;
    }
}
