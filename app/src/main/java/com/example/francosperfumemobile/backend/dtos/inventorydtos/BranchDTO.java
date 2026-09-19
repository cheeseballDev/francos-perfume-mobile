package com.example.francosperfumemobile.backend.dtos.inventorydtos;

import com.google.gson.annotations.SerializedName;

public class BranchDTO {
    @SerializedName("branchId")
    private int branchId;
    @SerializedName("branchDisplayId")
    private String branchDisplayId;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchDisplayId() {
        return branchDisplayId;
    }

    public void setBranchDisplayId(String branchDisplayId) {
        this.branchDisplayId = branchDisplayId;
    }
}
