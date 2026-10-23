package com.example.francosperfumemobile.backend.responses.KPIResponses;

import com.google.gson.annotations.SerializedName;

public class RequestsInUserStageKPIResponse {
    @SerializedName("requestsInStage")
    private int requestsInStage;

    public int getRequestsInStage() { return requestsInStage; }
}
