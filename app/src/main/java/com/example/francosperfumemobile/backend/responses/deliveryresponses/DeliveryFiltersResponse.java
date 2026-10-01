package com.example.francosperfumemobile.backend.responses.deliveryresponses;

import com.example.francosperfumemobile.backend.dtos.deliverydtos.*;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DeliveryFiltersResponse {

    @SerializedName("fromBranches")
    private List<String> fromBranches;
    @SerializedName("toBranches")
    private List<String> toBranches;
    @SerializedName("status")
    private List<String> status;

    public List<String> getFromBranches() {
        return fromBranches;
    }

    public List<String> getToBranches() {
        return toBranches;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setFromBranches(List<String> fromBranches) {
        this.fromBranches = fromBranches;
    }

    public void setToBranches(List<String> toBranches) {
        this.toBranches = toBranches;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }

}