package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class DisplayInventoryDTO {
    @SerializedName("productId")
    private int productId;
    @SerializedName("productDisplayId")
    private String productDisplayId;
    @SerializedName("productName")
    private String productName;
    @SerializedName("productType")
    private String productType;
    @SerializedName("productGender")
    private String productGender;
    @SerializedName("productBarcode")
    private String productBarcode;
    @SerializedName("productStatus")
    private String productStatus;
    @SerializedName("productPrice")
    private double productPrice;
    @SerializedName("productImageUrl")
    private String productImageUrl;
    @SerializedName("productDateCreated")
    private String productDateCreated;
    @SerializedName("branchId")
    private Integer branchId;
    @SerializedName("branchDisplayId")
    private String branchDisplayId;
    @SerializedName("branchName")
    private String branchName;
    @SerializedName("productQuantity")
    private int productQuantity;
    @SerializedName("productBatchCount")
    private int productBatchCount;
    @SerializedName("stockWarning")
    private String stockWarning;
    @SerializedName("batches")
    private List<DisplayBatchDTO> batches;

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public String getProductDisplayId() {
        return productDisplayId;
    }

    public void setProductDisplayId(String productDisplayId) {
        this.productDisplayId = productDisplayId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductType() {
        return productType;
    }

    public void setProductType(String productType) {
        this.productType = productType;
    }

    public String getProductGender() {
        return productGender;
    }

    public void setProductGender(String productGender) {
        this.productGender = productGender;
    }

    public String getProductBarcode() {
        return productBarcode;
    }

    public void setProductBarcode(String productBarcode) {
        this.productBarcode = productBarcode;
    }

    public String getProductStatus() {
        return productStatus;
    }

    public void setProductStatus(String productStatus) {
        this.productStatus = productStatus;
    }

    public double getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(double productPrice) {
        this.productPrice = productPrice;
    }

    public String getProductImageUrl() {
        return productImageUrl;
    }

    public void setProductImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
    }

    public String getProductDateCreated() {
        return productDateCreated;
    }

    public void setProductDateCreated(String productDateCreated) {
        this.productDateCreated = productDateCreated;
    }

    public Integer getBranchId() {
        return branchId;
    }

    public void setBranchId(Integer branchId) {
        this.branchId = branchId;
    }

    public String getBranchDisplayId() {
        return branchDisplayId;
    }

    public void setBranchDisplayId(String branchDisplayId) {
        this.branchDisplayId = branchDisplayId;
    }

    public String getBranchName() {
        return branchName;
    }

    public void setBranchName(String branchName) {
        this.branchName = branchName;
    }

    public int getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(int productQuantity) {
        this.productQuantity = productQuantity;
    }

    public int getProductBatchCount() {
        return productBatchCount;
    }

    public void setProductBatchCount(int productBatchCount) {
        this.productBatchCount = productBatchCount;
    }

    public String getStockWarning() {
        return stockWarning;
    }

    public void setStockWarning(String stockWarning) {
        this.stockWarning = stockWarning;
    }

    public List<DisplayBatchDTO> getBatches() {
        return batches;
    }

    public void setBatches(List<DisplayBatchDTO> batches) {
        this.batches = batches;
    }
}