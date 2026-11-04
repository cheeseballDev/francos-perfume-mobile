package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.branchdto.BranchDTO;
import com.example.francosperfumemobile.backend.dtos.requestdtos.*;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestFiltersResponse {

    @SerializedName("requestStatus")
    private List<String> requestStatus;

    @SerializedName("branches")
    private List<BranchDTO> branches;

    @SerializedName("fromBranches")
    private List<String> fromBranches;

    @SerializedName("toBranches")
    private List<String> toBranches;

    @SerializedName("direction")
    private List<String> direction;

    @SerializedName("requestStages")
    private List<String> stages;

    @SerializedName("products")
    private List<RequestProductFilterDTO> products;

    public List<String> getRequestStatus() {
        return requestStatus;
    }

    public void setRequestStatus(List<String> requestStatus) {
        this.requestStatus = requestStatus;
    }

    public List<BranchDTO> getBranches() {
        return branches;
    }

    public void setBranches(List<BranchDTO> branches) {
        this.branches = branches;
    }

    public List<String> getDirection() {
        return direction;
    }

    public void setDirection(List<String> direction) {
        this.direction = direction;
    }

    public List<String> getStages() {
        return stages;
    }

    public void setStages(List<String> stages) {
        this.stages = stages;
    }

    public List<RequestProductFilterDTO> getProducts() {
        return products;
    }

    public void setProducts(List<RequestProductFilterDTO> products) {
        this.products = products;
    }

    public List<String> getFromBranches() {
        return fromBranches;
    }

    public void setFromBranches(List<String> fromBranches) {
        this.fromBranches = fromBranches;
    }

    public List<String> getToBranches() {
        return toBranches;
    }

    public void setToBranches(List<String> toBranches) {
        this.toBranches = toBranches;
    }
}
