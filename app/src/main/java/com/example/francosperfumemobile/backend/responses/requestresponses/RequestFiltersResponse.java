package com.example.francosperfumemobile.backend.responses.requestresponses;

import com.example.francosperfumemobile.backend.dtos.requestdtos.*;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RequestFiltersResponse {
    @SerializedName("direction")
    private List<String> direction;
    @SerializedName("stage")
    private List<String> stage;
    @SerializedName("status")
    private List<String> status;

    public List<String> getDirection() {
        return direction;
    }

    public void setDirection(List<String> direction) {
        this.direction = direction;
    }

    public List<String> getStage() {
        return stage;
    }

    public void setStage(List<String> stage) {
        this.stage = stage;
    }

    public List<String> getStatus() {
        return status;
    }

    public void setStatus(List<String> status) {
        this.status = status;
    }
}