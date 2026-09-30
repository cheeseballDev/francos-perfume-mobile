package com.example.francosperfumemobile.backend.dtos.branchdto;

import com.google.gson.annotations.SerializedName;

public class BranchDTO {
    @SerializedName("branchId")
    private int branchId;

    @SerializedName("branchLocation")
    private String branchLocation;

    public int getBranchId() {
        return branchId;
    }

    public void setBranchId(int branchId) {
        this.branchId = branchId;
    }

    public String getBranchLocation() {
        return branchLocation;
    }

    public void setBranchLocation(String branchLocation) {
        this.branchLocation = branchLocation;
    }
}
