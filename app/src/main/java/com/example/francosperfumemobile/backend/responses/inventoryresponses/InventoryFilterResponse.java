package com.example.francosperfumemobile.backend.responses.inventoryresponses;

import java.util.List;

public class InventoryFilterResponse {
    private List<String> ProductTypes;
    private List<String> ProductGenders;
    private List<Integer> Branches;
    public List<String> getProductTypes() {
        return ProductTypes;
    }

    public void setProductTypes(List<String> productTypes) {
        this.ProductTypes = productTypes;
    }

    public List<String> getProductGenders() {
        return ProductGenders;
    }

    public void setProductGenders(List<String> productGenders) { this.ProductGenders = productGenders; }

    public List<Integer> getBranches() {
        return Branches;
    }

    public void setBranches(List<Integer> branches) {
        this.Branches = branches;
    }
}