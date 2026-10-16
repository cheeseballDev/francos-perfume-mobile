package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InventoryFilterResponse {
    @SerializedName("productType")
    private List<String> productTypes;
    @SerializedName("productGender")
    private List<String> productGenders;
    @SerializedName("branch")
    private List<String> branches;

    public List<String> getProductTypes() {
        return productTypes;
    }

    public void setProductTypes(List<String> productTypes) {
        this.productTypes = productTypes;
    }

    public List<String> getProductGenders() {
        return productGenders;
    }

    public void setProductGenders(List<String> productGenders) {
        this.productGenders = productGenders;
    }

    public List<String> getBranches() {
        return branches;
    }

    public void setBranches(List<String> branches) {
        this.branches = branches;
    }
}