package com.example.francosperfumemobile.backend.entities;

import com.google.gson.annotations.SerializedName;

public class Products {

    @SerializedName("productId")
    private int ProductId;

    @SerializedName("productDisplayId")
    private String ProductDisplayId;

    @SerializedName("productName")
    private String ProductName;

    @SerializedName("productType")
    private String ProductType;

    @SerializedName("productNote")
    private String ProductNote;

    @SerializedName("productGender")
    private String ProductGender;

    @SerializedName("productDateCreated")
    private String ProductDateCreated;

    @SerializedName("productBarcode")
    private String ProductBarcode;

    @SerializedName("productDescription")
    private String ProductDescription;

    @SerializedName("productPrice")
    private double ProductPrice;

    @SerializedName("productImageUrl")
    private String ProductImageUrl;

    @SerializedName("totalStock")
    private int TotalStock;

    public Products() {
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

    public String getProductType() {
        return ProductType;
    }

    public void setProductType(String productType) {
        ProductType = productType;
    }

    public String getProductNote() {
        return ProductNote;
    }

    public void setProductNote(String productNote) {
        ProductNote = productNote;
    }

    public String getProductGender() {
        return ProductGender;
    }

    public void setProductGender(String productGender) {
        ProductGender = productGender;
    }

    public String getProductDateCreated() {
        return ProductDateCreated;
    }

    public void setProductDateCreated(String productDateCreated) {
        ProductDateCreated = productDateCreated;
    }

    public String getProductBarcode() {
        return ProductBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        ProductBarcode = productBarcode;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
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

    public int getTotalStock() {
        return TotalStock;
    }

    public void setTotalStock(int totalStock) {
        TotalStock = totalStock;
    }
}