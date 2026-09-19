package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import com.example.francosperfumemobile.backend.dtos.inventorydtos.BranchDTO;
import com.google.gson.annotations.SerializedName;
import java.util.List;

public class InventoryFilterResponse {
    @SerializedName("productTypes")
    private List<String> productTypes;
    @SerializedName("productGenders")
    private List<String> productGenders;
    @SerializedName("branches")
    private List<BranchDTO> branches;

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

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }
}